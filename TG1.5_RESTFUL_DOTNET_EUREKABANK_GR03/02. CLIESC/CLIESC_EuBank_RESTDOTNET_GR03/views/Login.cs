using System;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using System.Windows.Forms;
using CLIESC_EuBank_RESTDOTNET_GR03.Services;

namespace CLIESC_EuBank_RESTDOTNET_GR03.views
{
    public partial class Login : Form
    {
        private readonly EuBankApiService _apiService = new EuBankApiService();

        public Login()
        {
            InitializeComponent();
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

        private async void btn_Ingresar_Click(object sender, EventArgs e)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(tbx_user.Text) || string.IsNullOrWhiteSpace(txb_password.Text))
                {
                    MessageBox.Show("Por favor, ingrese usuario y contrasena.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return;
                }

                bool resultado = await _apiService.LoginAsync(tbx_user.Text, txb_password.Text);

                if (resultado)
                {
                    this.Hide();
                    var home = new Home();
                    home.User = tbx_user.Text;
                    home.ShowDialog();
                    this.Close();
                }
                else
                {
                    MessageBox.Show("Usuario o contrasena incorrectos", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al conectar con el servicio: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
        private async Task btn_Ingresar_Click_1Async(object sender, EventArgs e)
        {
            btn_Ingresar_Click(sender, e);
            await Task.CompletedTask;
        }

        private void lblUser_Click(object sender, EventArgs e)
        {
        }
    }
}
