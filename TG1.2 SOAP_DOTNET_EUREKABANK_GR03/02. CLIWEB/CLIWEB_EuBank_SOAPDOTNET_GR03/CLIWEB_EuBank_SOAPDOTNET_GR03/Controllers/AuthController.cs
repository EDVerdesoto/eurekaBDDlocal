using CLIWEB_EuBank_SOAPDOTNET_GR03.Services;
using Microsoft.AspNetCore.Mvc;

namespace CLIWEB_EuBank_SOAPDOTNET_GR03.Controllers
{
    public class AuthController : Controller
    {
        private readonly IEurekaBankSoapService _eurekaBankService;

        public AuthController(IEurekaBankSoapService eurekaBankService)
        {
            _eurekaBankService = eurekaBankService;
        }

        public IActionResult Login()
        {
            if (HttpContext.Session.GetString("Usuario") != null)
            {
                return RedirectToAction("Index", "Dashboard");
            }

            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Login(string usuario, string clave)
        {
            try
            {
                bool resultado = await _eurekaBankService.LoginAsync(usuario, clave);

                if (resultado)
                {
                    HttpContext.Session.SetString("Usuario", usuario);
                    return RedirectToAction("Index", "Dashboard");
                }

                ViewBag.Error = "Usuario o contrasena incorrectos";
                return View();
            }
            catch (Exception ex)
            {
                ViewBag.Error = $"Error al conectar con el servicio: {ex.Message}";
                return View();
            }
        }

        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Login");
        }
    }
}
