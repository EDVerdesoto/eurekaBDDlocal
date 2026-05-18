using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using WS_EuBank_RESTDOTNET_GR03.DTOs;

namespace WS_EuBank_RESTDOTNET_GR03.Controllers
{
    [RoutePrefix("api")]
    public class CuentasController : ApiController
    {
        [HttpGet]
        [Route("cuentas")]
        public IHttpActionResult GetCuentas()
        {
            using (var context = new EurekaBankContext())
            {
                var cuentas = (from c in context.Cuentas
                               join cl in context.Clientes on c.ChrClieCodigo equals cl.ChrClieCodigo
                               select new CuentaDTO
                               {
                                   Codigo = c.ChrCuenCodigo,
                                   Moneda = c.ChrMoneCodigo,
                                   Saldo = c.DecCuenSaldo,
                                   Estado = c.VchCuenEstado,
                                   NombreCliente = cl.VchClieNombre + " " + cl.VchCliePaterno + " " + cl.VchClieMaterno,
                                   EmailCliente = cl.VchClieEmail,
                                   TelefonoCliente = cl.VchClieTelefono
                               }).ToList();
                return Ok(cuentas);
            }
        }

        [HttpGet]
        [Route("cuentas/{codigo}")]
        public IHttpActionResult GetCuenta(string codigo)
        {
            using (var context = new EurekaBankContext())
            {
                var cuenta = (from c in context.Cuentas
                              join cl in context.Clientes on c.ChrClieCodigo equals cl.ChrClieCodigo
                              where c.ChrCuenCodigo == codigo
                              select new CuentaDTO
                              {
                                  Codigo = c.ChrCuenCodigo,
                                  Moneda = c.ChrMoneCodigo,
                                  Saldo = c.DecCuenSaldo,
                                  Estado = c.VchCuenEstado,
                                  NombreCliente = cl.VchClieNombre + " " + cl.VchCliePaterno + " " + cl.VchClieMaterno,
                                  EmailCliente = cl.VchClieEmail,
                                  TelefonoCliente = cl.VchClieTelefono
                              }).FirstOrDefault();

                if (cuenta == null)
                    return NotFound();
                return Ok(cuenta);
            }
        }
    }
}
