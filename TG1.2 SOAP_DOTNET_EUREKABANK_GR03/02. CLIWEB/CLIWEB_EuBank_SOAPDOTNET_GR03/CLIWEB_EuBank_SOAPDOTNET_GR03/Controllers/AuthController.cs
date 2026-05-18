using Microsoft.AspNetCore.Mvc;
using ServiceReference1;

namespace CLIWEB_EuBank_SOAPDOTNET_GR03.Controllers
{
    public class AuthController : Controller
    {
        public IActionResult Login()
        {
            // Si ya est� autenticado, redirigir al dashboard
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
                var client = new LoginServiceClient();
                bool resultado = await client.LoginAsync(usuario, clave);

                if (resultado)
                {
                    // Guardar el usuario en la sesi�n
                    HttpContext.Session.SetString("Usuario", usuario);
                    return RedirectToAction("Index", "Dashboard");
                }
                else
                {
                    ViewBag.Error = "Usuario o contrase�a incorrectos";
                    return View();
                }
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
