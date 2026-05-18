using CLIWEB_EuBank_RESTDOTNET_GR03.Services;
using Microsoft.AspNetCore.Mvc;

namespace CLIWEB_EuBank_RESTDOTNET_GR03.Controllers
{
    public class DashboardController : Controller
    {
        private readonly EuBankApiService _apiService;

        public DashboardController(EuBankApiService apiService)
        {
            _apiService = apiService;
        }

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
        public async Task<IActionResult> GetCuentas()
        {
            var cuentas = await _apiService.GetCuentasAsync();
            return Json(cuentas);
        }

        [HttpGet]
        public async Task<IActionResult> GetTiposMovimiento()
        {
            var tipos = await _apiService.GetTiposMovimientoAsync();
            return Json(tipos);
        }

        [HttpGet]
        public async Task<IActionResult> GetMovimientos(string cuenta)
        {
            if (string.IsNullOrEmpty(cuenta))
            {
                return Json(new { error = "Debe especificar una cuenta" });
            }

            var movimientos = await _apiService.GetMovimientosAsync(cuenta);
            return Json(movimientos);
        }

        [HttpGet]
        public async Task<IActionResult> GetTodosMovimientos()
        {
            var movimientos = await _apiService.GetMovimientosAsync(null);
            return Json(movimientos);
        }

        [HttpPost]
        public async Task<IActionResult> CrearMovimiento([FromBody] CrearMovimientoRequest request)
        {
            if (string.IsNullOrEmpty(request.Tipo) || string.IsNullOrEmpty(request.CuentaOrigen) || request.Importe <= 0)
            {
                return Json(new { success = false, message = "Por favor complete todos los campos requeridos" });
            }

            if (request.Tipo == "TRANSFERENCIA" && string.IsNullOrEmpty(request.CuentaDestino))
            {
                return Json(new { success = false, message = "Para transferencias se requiere la cuenta destino" });
            }

            var result = await _apiService.CrearMovimientoAsync(request.Tipo, request.CuentaOrigen, request.CuentaDestino ?? "", request.Importe);

            if (result != null && result.codigo == 1)
            {
                return Json(new { success = true, message = result.mensaje });
            }

            return Json(new { success = false, message = result?.mensaje ?? "Error al crear el movimiento" });
        }

        public class CrearMovimientoRequest
        {
            public string Tipo { get; set; } = string.Empty;
            public string CuentaOrigen { get; set; } = string.Empty;
            public string CuentaDestino { get; set; } = string.Empty;
            public decimal Importe { get; set; }
        }
    }
}
