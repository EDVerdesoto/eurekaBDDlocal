using Microsoft.AspNetCore.Mvc;

namespace CLIWEB_EuBank_SOAPDOTNET_GR03.Controllers
{
    public class DashboardController : Controller
    {
        public IActionResult Index()
        {
            if (HttpContext.Session.GetString("Usuario") == null)
            {
                return RedirectToAction("Login", "Auth");
            }
            return View();
        }

        [HttpGet]
        public IActionResult TestService()
        {
            return Json(new { status = "ok", message = "Servicio disponible" });
        }

        [HttpGet]
        public IActionResult GetCuentas()
        {
            var cuentas = new[]
            {
                new { codigo = "00100001", nombreCliente = "Juan Pérez", emailCliente = "juan@email.com", telefonoCliente = "999-111-001", moneda = "01", saldo = 15000.50m, estado = "ACTIVO" },
                new { codigo = "00100002", nombreCliente = "María García", emailCliente = "maria@email.com", telefonoCliente = "999-222-002", moneda = "02", saldo = 8500.75m, estado = "ACTIVO" },
                new { codigo = "00100003", nombreCliente = "Carlos López", emailCliente = "carlos@email.com", telefonoCliente = "999-333-003", moneda = "01", saldo = 3200.00m, estado = "INACTIVO" },
                new { codigo = "00100004", nombreCliente = "Ana Martínez", emailCliente = "ana@email.com", telefonoCliente = "999-444-004", moneda = "02", saldo = 22000.00m, estado = "ACTIVO" },
                new { codigo = "00100005", nombreCliente = "Pedro Sánchez", emailCliente = "pedro@email.com", telefonoCliente = "999-555-005", moneda = "01", saldo = 500.25m, estado = "ACTIVO" },
                new { codigo = "00100006", nombreCliente = "Laura Díaz", emailCliente = "laura@email.com", telefonoCliente = "999-666-006", moneda = "02", saldo = 12400.00m, estado = "INACTIVO" },
                new { codigo = "00100007", nombreCliente = "Roberto Torres", emailCliente = "roberto@email.com", telefonoCliente = "999-777-007", moneda = "01", saldo = 7800.30m, estado = "ACTIVO" },
                new { codigo = "00100008", nombreCliente = "Sofía Ruiz", emailCliente = "sofia@email.com", telefonoCliente = "999-888-008", moneda = "02", saldo = 31000.00m, estado = "ACTIVO" }
            };
            return Json(cuentas);
        }

        [HttpGet]
        public IActionResult GetTiposMovimiento()
        {
            var tipos = new[] { "Apertura de Cuenta", "Deposito", "Retiro", "Cancelar Cuenta", "Transferencia" };
            return Json(tipos);
        }

        [HttpGet]
        public IActionResult GetMovimientos(string cuenta)
        {
            if (string.IsNullOrEmpty(cuenta))
            {
                return Json(new { error = "Debe especificar una cuenta" });
            }

            var movimientos = ObtenerMovimientos().Where(m => m.CodigoCuenta == cuenta).ToArray();

            var result = movimientos.Select(m => new
            {
                codigoCuenta = m.CodigoCuenta,
                numero = m.Numero,
                fecha = m.Fecha.ToString("yyyy-MM-ddTHH:mm:ss"),
                tipo = m.Tipo,
                referencia = m.Referencia,
                importe = m.Importe,
                saldoActual = m.SaldoActual
            }).ToArray();

            return Json(result);
        }

        [HttpGet]
        public IActionResult GetTodosMovimientos()
        {
            var movimientos = ObtenerMovimientos();
            var result = movimientos.Select(m => new
            {
                codigoCuenta = m.CodigoCuenta,
                numero = m.Numero,
                fecha = m.Fecha.ToString("yyyy-MM-ddTHH:mm:ss"),
                tipo = m.Tipo,
                referencia = m.Referencia,
                importe = m.Importe,
                saldoActual = m.SaldoActual
            }).ToArray();

            return Json(result);
        }

        [HttpPost]
        public IActionResult CrearMovimiento([FromBody] CrearMovimientoRequest request)
        {
            if (string.IsNullOrEmpty(request.Tipo) || string.IsNullOrEmpty(request.CuentaOrigen) || request.Importe <= 0)
            {
                return Json(new { success = false, message = "Por favor complete todos los campos requeridos" });
            }

            if (request.Tipo == "TRANSFERENCIA" && string.IsNullOrEmpty(request.CuentaDestino))
            {
                return Json(new { success = false, message = "Para transferencias se requiere la cuenta destino" });
            }

            return Json(new { success = true, message = "Movimiento creado exitosamente" });
        }

        public class CrearMovimientoRequest
        {
            public string Tipo { get; set; } = string.Empty;
            public string CuentaOrigen { get; set; } = string.Empty;
            public string CuentaDestino { get; set; } = string.Empty;
            public decimal Importe { get; set; }
        }

        private class MovimientoData
        {
            public string CodigoCuenta { get; set; } = string.Empty;
            public int Numero { get; set; }
            public DateTime Fecha { get; set; }
            public string Tipo { get; set; } = string.Empty;
            public string Referencia { get; set; } = string.Empty;
            public decimal Importe { get; set; }
            public decimal SaldoActual { get; set; }
        }

        private static List<MovimientoData> ObtenerMovimientos()
        {
            return new List<MovimientoData>
            {
                new MovimientoData { CodigoCuenta = "00100001", Numero = 1, Fecha = new DateTime(2025, 1, 15, 10, 30, 0), Tipo = "Apertura de Cuenta", Referencia = "Apertura inicial", Importe = 10000.00m, SaldoActual = 10000.00m },
                new MovimientoData { CodigoCuenta = "00100001", Numero = 2, Fecha = new DateTime(2025, 2, 20, 14, 0, 0), Tipo = "Deposito", Referencia = "Depósito nómina", Importe = 5000.00m, SaldoActual = 15000.00m },
                new MovimientoData { CodigoCuenta = "00100001", Numero = 3, Fecha = new DateTime(2025, 3, 10, 9, 15, 0), Tipo = "Retiro", Referencia = "Retiro cajero", Importe = -1000.00m, SaldoActual = 14000.00m },
                new MovimientoData { CodigoCuenta = "00100001", Numero = 4, Fecha = new DateTime(2025, 4, 5, 11, 45, 0), Tipo = "Transferencia", Referencia = "Pago servicios", Importe = -500.50m, SaldoActual = 13499.50m },
                new MovimientoData { CodigoCuenta = "00100002", Numero = 1, Fecha = new DateTime(2025, 2, 1, 8, 0, 0), Tipo = "Apertura de Cuenta", Referencia = "Apertura", Importe = 5000.00m, SaldoActual = 5000.00m },
                new MovimientoData { CodigoCuenta = "00100002", Numero = 2, Fecha = new DateTime(2025, 3, 15, 16, 30, 0), Tipo = "Deposito", Referencia = "Transferencia recibida", Importe = 3500.75m, SaldoActual = 8500.75m },
                new MovimientoData { CodigoCuenta = "00100004", Numero = 1, Fecha = new DateTime(2025, 1, 20, 12, 0, 0), Tipo = "Apertura de Cuenta", Referencia = "Apertura", Importe = 20000.00m, SaldoActual = 20000.00m },
                new MovimientoData { CodigoCuenta = "00100004", Numero = 2, Fecha = new DateTime(2025, 5, 1, 10, 0, 0), Tipo = "Deposito", Referencia = "Depósito ahorro", Importe = 2000.00m, SaldoActual = 22000.00m },
            };
        }
    }
}
