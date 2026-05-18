using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using WS_EuBank_RESTDOTNET_GR03.DTOs;

namespace WS_EuBank_RESTDOTNET_GR03.Controllers
{
    [RoutePrefix("api")]
    public class MovimientosController : ApiController
    {
        private const string EmpleadoDefault = "0001";
        private const string TipoDeposito = "003";
        private const string TipoRetiro = "004";
        private const string TipoTransferenciaSalida = "009";
        private const string TipoTransferenciaIngreso = "008";

        [HttpGet]
        [Route("movimientos")]
        public IHttpActionResult GetMovimientos(string cuenta = null)
        {
            using (var context = new EurekaBankContext())
            {
                var query = from m in context.Movimientos
                            join c in context.Cuentas on m.ChrCuenCodigo equals c.ChrCuenCodigo
                            join cl in context.Clientes on c.ChrClieCodigo equals cl.ChrClieCodigo
                            join tm in context.TiposMovimiento on m.ChrTipoCodigo equals tm.ChrTipoCodigo
                            select new { m, c, cl, tm };

                if (!string.IsNullOrWhiteSpace(cuenta) && cuenta != "?")
                {
                    query = query.Where(x => x.m.ChrCuenCodigo == cuenta);
                }

                var movimientosConExtras = query.OrderBy(x => x.m.DttMoviFecha).ThenBy(x => x.m.IntMoviNumero)
                    .Select(x => new
                    {
                        Data = new MovimientoDTO
                        {
                            Numero = x.m.IntMoviNumero,
                            Fecha = x.m.DttMoviFecha,
                            Tipo = x.tm.VchTipoDescripcion + (x.tm.ChrTipoCodigo == "008" ? " Entrada" : x.tm.ChrTipoCodigo == "009" ? " Salida" : ""),
                            Importe = x.m.DecMoviImporte * (x.tm.VchTipoAccion == "INGRESO" ? 1 : -1),
                            Referencia = x.m.ChrCuenReferencia,
                            CodigoCuenta = x.m.ChrCuenCodigo,
                            NombreCliente = x.cl.VchClieNombre + " " + x.cl.VchCliePaterno + " " + x.cl.VchClieMaterno
                        },
                        TipoAccion = x.tm.VchTipoAccion,
                        SaldoCuenta = x.c.DecCuenSaldo
                    }).ToList();

                var grupos = movimientosConExtras.GroupBy(me => me.Data.CodigoCuenta);
                foreach (var grupo in grupos)
                {
                    var lista = grupo.OrderBy(me => me.Data.Fecha).ThenBy(me => me.Data.Numero).ToList();
                    decimal saldoActual = lista.Last().SaldoCuenta - lista.Sum(me => me.Data.Importe);

                    foreach (var me in lista)
                    {
                        saldoActual += me.Data.Importe;
                        me.Data.SaldoActual = saldoActual;
                    }
                }

                var resultado = movimientosConExtras.Select(me => me.Data)
                    .OrderByDescending(md => md.Fecha).ThenByDescending(md => md.Numero).ToList();
                return Ok(resultado);
            }
        }

        [HttpGet]
        [Route("tiposmovimiento")]
        public IHttpActionResult GetTiposMovimiento()
        {
            return Ok(new List<string> { "DEPOSITO", "RETIRO", "TRANSFERENCIA" });
        }

        [HttpPost]
        [Route("movimientos/crear")]
        public IHttpActionResult CrearMovimiento([FromBody] CrearMovimientoRequest request)
        {
            if (request == null)
                return BadRequest("Datos del movimiento requeridos.");

            if (request.Importe <= 0)
                return Ok(new ResultadoDTO(-1, "El monto debe ser mayor a cero."));

            switch (request.Tipo.ToUpper())
            {
                case "DEPOSITO":
                    return Ok(Deposito(request.CuentaOrigen, request.Importe)
                        ? new ResultadoDTO(1, "Deposito realizado exitosamente.")
                        : new ResultadoDTO(-1, "Error al realizar el deposito."));
                case "RETIRO":
                    return Ok(Retiro(request.CuentaOrigen, request.Importe)
                        ? new ResultadoDTO(1, "Retiro realizado exitosamente.")
                        : new ResultadoDTO(-1, "Error al realizar el retiro. Verifique saldo suficiente."));
                case "TRANSFERENCIA":
                    if (string.IsNullOrEmpty(request.CuentaDestino))
                        return Ok(new ResultadoDTO(-1, "Cuenta destino requerida para transferencia."));
                    return Ok(Transferencia(request.CuentaOrigen, request.CuentaDestino, request.Importe)
                        ? new ResultadoDTO(1, "Transferencia realizada exitosamente.")
                        : new ResultadoDTO(-1, "Error al realizar la transferencia. Verifique cuentas y saldo."));
                default:
                    return Ok(new ResultadoDTO(-1, "Tipo de movimiento no valido."));
            }
        }

        private bool Deposito(string cuenta, decimal monto)
        {
            using (var context = new EurekaBankContext())
            {
                using (var transaction = context.Database.BeginTransaction())
                {
                    try
                    {
                        var c = context.Cuentas.FirstOrDefault(cu => cu.ChrCuenCodigo == cuenta);
                        if (c == null) return false;
                        int nextNum = c.IntCuenContMov + 1;
                        c.DecCuenSaldo += monto;
                        c.IntCuenContMov = nextNum;
                        var mov = new Movimiento
                        {
                            ChrCuenCodigo = cuenta,
                            IntMoviNumero = nextNum,
                            DttMoviFecha = DateTime.Now.Date,
                            ChrEmplCodigo = EmpleadoDefault,
                            ChrTipoCodigo = TipoDeposito,
                            DecMoviImporte = monto
                        };
                        context.Movimientos.Add(mov);
                        context.SaveChanges();
                        transaction.Commit();
                        return true;
                    }
                    catch
                    {
                        transaction.Rollback();
                        return false;
                    }
                }
            }
        }

        private bool Retiro(string cuenta, decimal monto)
        {
            using (var context = new EurekaBankContext())
            {
                using (var transaction = context.Database.BeginTransaction())
                {
                    try
                    {
                        var c = context.Cuentas.FirstOrDefault(cu => cu.ChrCuenCodigo == cuenta);
                        if (c == null || c.DecCuenSaldo < monto) return false;
                        int nextNum = c.IntCuenContMov + 1;
                        c.DecCuenSaldo -= monto;
                        c.IntCuenContMov = nextNum;
                        var mov = new Movimiento
                        {
                            ChrCuenCodigo = cuenta,
                            IntMoviNumero = nextNum,
                            DttMoviFecha = DateTime.Now.Date,
                            ChrEmplCodigo = EmpleadoDefault,
                            ChrTipoCodigo = TipoRetiro,
                            DecMoviImporte = monto
                        };
                        context.Movimientos.Add(mov);
                        context.SaveChanges();
                        transaction.Commit();
                        return true;
                    }
                    catch
                    {
                        transaction.Rollback();
                        return false;
                    }
                }
            }
        }

        private bool Transferencia(string cuentaOrigen, string cuentaDestino, decimal monto)
        {
            if (cuentaOrigen == cuentaDestino) return false;
            using (var context = new EurekaBankContext())
            {
                using (var transaction = context.Database.BeginTransaction())
                {
                    try
                    {
                        var origen = context.Cuentas.FirstOrDefault(cu => cu.ChrCuenCodigo == cuentaOrigen);
                        var destino = context.Cuentas.FirstOrDefault(cu => cu.ChrCuenCodigo == cuentaDestino);
                        if (origen == null || destino == null || origen.DecCuenSaldo < monto || destino.VchCuenEstado != "ACTIVO")
                            return false;

                        int nextNumOri = origen.IntCuenContMov + 1;
                        int nextNumDest = destino.IntCuenContMov + 1;

                        origen.DecCuenSaldo -= monto;
                        origen.IntCuenContMov = nextNumOri;
                        destino.DecCuenSaldo += monto;
                        destino.IntCuenContMov = nextNumDest;

                        var movOri = new Movimiento
                        {
                            ChrCuenCodigo = cuentaOrigen,
                            IntMoviNumero = nextNumOri,
                            DttMoviFecha = DateTime.Now.Date,
                            ChrEmplCodigo = EmpleadoDefault,
                            ChrTipoCodigo = TipoTransferenciaSalida,
                            DecMoviImporte = monto,
                            ChrCuenReferencia = cuentaDestino
                        };
                        var movDest = new Movimiento
                        {
                            ChrCuenCodigo = cuentaDestino,
                            IntMoviNumero = nextNumDest,
                            DttMoviFecha = DateTime.Now.Date,
                            ChrEmplCodigo = EmpleadoDefault,
                            ChrTipoCodigo = TipoTransferenciaIngreso,
                            DecMoviImporte = monto,
                            ChrCuenReferencia = cuentaOrigen
                        };
                        context.Movimientos.Add(movOri);
                        context.Movimientos.Add(movDest);
                        context.SaveChanges();
                        transaction.Commit();
                        return true;
                    }
                    catch
                    {
                        transaction.Rollback();
                        return false;
                    }
                }
            }
        }

        public class CrearMovimientoRequest
        {
            public string Tipo { get; set; }
            public string CuentaOrigen { get; set; }
            public string CuentaDestino { get; set; }
            public decimal Importe { get; set; }
        }
    }
}
