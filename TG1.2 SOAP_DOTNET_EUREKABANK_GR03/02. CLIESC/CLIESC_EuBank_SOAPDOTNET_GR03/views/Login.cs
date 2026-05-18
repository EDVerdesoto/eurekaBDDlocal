using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using CLIESC_EuBank_SOAPDOTNET_GR03.LoginService; // Correct namespace for LoginService

namespace CLIESC_EuBank_SOAPDOTNET_GR03.views
{
    public partial class Login : Form
    {
        public Login()
        {
            InitializeComponent();
            // MessageBox.Show("Login form initialized"); // Debug removed
        }
        [DllImport("user32.DLL", EntryPoint = "ReleaseCapture")]
        private extern static void ReleaseCapture();
        [DllImport("user32.DLL", EntryPoint = "SendMessage")]
        private extern static void SendMessage(System.IntPtr hWnd, int wMsg, int wParam, int IParam);

        private void header_MouseDown(object sender, MouseEventArgs e)
        {
            ReleaseCapture();
            SendMessage(this.Handle, 0x112, 0xf012, 0);
        }

        private void btn_exit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void min_button_Click(object sender, EventArgs e)
        {
            this.WindowState = FormWindowState.Minimized;
        }

        private void lblUser_Click(object sender, EventArgs e)
        {

        }

        private void Login_Load(object sender, EventArgs e)
        {

        }

        // Added login button click event to consume the LoginService
        private async void btn_Ingresar_Click(object sender, EventArgs e)
        {

        }

        private async Task btn_Ingresar_Click_1Async(object sender, EventArgs e)
        {
            try
            {
                // Validate inputs
                if (string.IsNullOrWhiteSpace(tbx_user.Text) || string.IsNullOrWhiteSpace(txb_password.Text))
                {
                    MessageBox.Show("Por favor, ingrese usuario y contraseña.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                var client = new LoginServiceClient();

                // Call the service
                bool resultado = await client.LoginAsync(tbx_user.Text, txb_password.Text);

                if (resultado)
                {
                    // Successful login, open Home form with user
                    this.Hide();
                    var home = new Home();
                    home.User = tbx_user.Text;
                    home.ShowDialog();
                    this.Close();
                }
                else
                {
                    MessageBox.Show("Usuario o contraseña incorrectos", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                // More detailed error message
                MessageBox.Show($"Error al conectar con el servicio: {ex.Message}\n\nDetalles: {ex.InnerException?.Message ?? "Sin detalles adicionales"}\n\nStackTrace: {ex.StackTrace}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
