using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WS_EuBank_SOAPDOTNET_GR03
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Login" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Login.svc or Login.svc.cs at the Solution Explorer and start debugging.
    public class LoginService : ILoginService
    {
        public bool Login(string usuario, string clave)
        {
            using (var context = new EurekaBankContext())
            {
                return context.Usuarios.Any(u => u.VchEmplUsuario == usuario && u.VchEmplClave == clave && u.VchEmplEstado == "ACTIVO");
            }
        }
    }
}
