using CLIWEB_EuBank_RESTDOTNET_GR03.Models;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;

namespace CLIWEB_EuBank_RESTDOTNET_GR03.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
