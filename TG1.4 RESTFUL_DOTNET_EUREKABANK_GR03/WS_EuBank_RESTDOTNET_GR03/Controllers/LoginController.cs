using System.Linq;
using System.Web.Http;
using WS_EuBank_RESTDOTNET_GR03.DTOs;

namespace WS_EuBank_RESTDOTNET_GR03.Controllers
{
    [RoutePrefix("api")]
    public class LoginController : ApiController
    {
        [HttpPost]
        [Route("login")]
        public IHttpActionResult Login([FromBody] LoginRequest request)
        {
            if (request == null || string.IsNullOrEmpty(request.Usuario) || string.IsNullOrEmpty(request.Clave))
                return BadRequest("Usuario y clave son requeridos. \n");

            using (var context = new EurekaBankContext())
            {
                var exito = context.Usuarios.Any(u =>
                    u.VchEmplUsuario == request.Usuario &&
                    u.VchEmplClave == request.Clave &&
                    u.VchEmplEstado == "ACTIVO");
                return Ok(new { success = exito });
            }
        }

        public class LoginRequest
        {
            public string Usuario { get; set; }
            public string Clave { get; set; }
        }
    }
}
