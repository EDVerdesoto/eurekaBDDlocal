using System;
using System.Data;
using System.Windows.Forms;
using CLIESC_EuBank_RESTDOTNET_GR03.Services;

namespace CLIESC_EuBank_RESTDOTNET_GR03.views
{
    public partial class AccountClient : Form
    {
        private readonly EuBankApiService _apiService = new EuBankApiService();
        public string User { get; set; }

        public AccountClient()
        {
            InitializeComponent();
        }

        private async void AccountClient_Load_1(object sender, EventArgs e)
        {
            await CargarCuentas();
        }

        private async System.Threading.Tasks.Task CargarCuentas()
        {
            try
            {
                GW_Clients.DataSource = null;
                GW_Clients.Rows.Clear();

                var cuentas = await _apiService.GetCuentasAsync();
                if (cuentas != null && cuentas.Count > 0)
                {
                    var dataTable = new DataTable();
                    dataTable.Columns.Add("Codigo", typeof(string));
                    dataTable.Columns.Add("Cliente", typeof(string));
                    dataTable.Columns.Add("Email", typeof(string));
                    dataTable.Columns.Add("Telefono", typeof(string));
                    dataTable.Columns.Add("Moneda", typeof(string));
                    dataTable.Columns.Add("Saldo", typeof(decimal));
                    dataTable.Columns.Add("Estado", typeof(string));

                    foreach (var c in cuentas)
                    {
                        string moneda = c.Moneda ?? "";
                        dataTable.Rows.Add(
                            c.Codigo ?? "",
                            c.NombreCliente ?? "",
                            c.EmailCliente ?? "",
                            c.TelefonoCliente ?? "",
                            NombreMoneda(moneda),
                            c.Saldo,
                            c.Estado ?? ""
                        );
                    }

                    GW_Clients.DataSource = dataTable;
                    if (GW_Clients.Columns["Saldo"] != null)
                    {
                        GW_Clients.Columns["Saldo"].DefaultCellStyle.Format = "N2";
                        GW_Clients.Columns["Saldo"].DefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleRight;
                    }
                }
                else
                {
                    MessageBox.Show("No se encontraron cuentas", "Informacion", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al cargar las cuentas:\n{ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private async void GW_Clients_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                var codigoCuenta = GW_Clients.Rows[e.RowIndex].Cells["Codigo"].Value?.ToString();
                if (!string.IsNullOrEmpty(codigoCuenta))
                {
                    await MostrarDetallesCuenta(codigoCuenta);
                }
            }
        }

        private async System.Threading.Tasks.Task MostrarDetallesCuenta(string codigoCuenta)
        {
            try
            {
                var cuenta = await _apiService.GetCuentaAsync(codigoCuenta);
                if (cuenta != null)
                {
                    string moneda = cuenta.Moneda ?? "";
                    string mensaje = $"Detalles de la Cuenta:\n\n" +
                                   $"Codigo: {codigoCuenta}\n" +
                                   $"Cliente: {cuenta.NombreCliente}\n" +
                                   $"Email: {cuenta.EmailCliente}\n" +
                                   $"Telefono: {cuenta.TelefonoCliente}\n" +
                                   $"Moneda: {NombreMoneda(moneda)}\n" +
                                   $"Saldo: {cuenta.Saldo:N2}\n" +
                                   $"Estado: {cuenta.Estado}";

                    MessageBox.Show(mensaje, "Informacion de la Cuenta", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al obtener detalles:\n{ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private string NombreMoneda(string moneda)
        {
            switch (moneda)
            {
                case "01": return "Soles";
                case "02": return "Dolares";
                default: return moneda;
            }
        }
    }
}
