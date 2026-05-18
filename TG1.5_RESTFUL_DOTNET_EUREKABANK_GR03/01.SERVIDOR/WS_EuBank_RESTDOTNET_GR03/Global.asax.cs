using System.Web.Http;

namespace WS_EuBank_RESTDOTNET_GR03
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            GlobalConfiguration.Configure(WebApiConfig.Register);
        }
    }
}
