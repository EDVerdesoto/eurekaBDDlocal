using System;
using System.Windows.Forms;

namespace CLIESC_EuBank_RESTDOTNET_GR03
{
    internal static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new views.Login());
        }
    }
}
