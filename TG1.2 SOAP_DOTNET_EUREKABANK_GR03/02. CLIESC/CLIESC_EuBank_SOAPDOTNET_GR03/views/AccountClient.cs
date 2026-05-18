using System;
using System.Windows.Forms;
using CLIESC_EuBank_SOAPDOTNET_GR03.MovimientosService; // Correct namespace for MovimientosService


namespace CLIESC_EuBank_SOAPDOTNET_GR03.views
{
    public partial class AccountClient : Form
    {
        public string User { get; set; } // Property to store the user

        public AccountClient()
        {
            InitializeComponent();
        }

        private async void AccountClient_Load_1(object sender, EventArgs e)
        {
            try
            {
                var client = new MovimientosServiceClient();
                var cuentas = await client.GetDatosCuentasAsync();
                GW_Clients.DataSource = cuentas;
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al obtener cuentas: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}