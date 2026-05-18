using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using CLIESC_EuBank_RESTDOTNET_GR03.Services;

namespace CLIESC_EuBank_RESTDOTNET_GR03.views
{
    public partial class Movements : Form
    {
        private readonly EuBankApiService _apiService = new EuBankApiService();
        public string User { get; set; }

        public Movements()
        {
            InitializeComponent();
        }

        private async void Movements_Load_1(object sender, EventArgs e)
        {
            await LoadMovements();
        }

        private async Task LoadMovements()
        {
            try
            {
                GW_Movements.DataSource = null;
                GW_Movements.Rows.Clear();

                var cuentas = await _apiService.GetCuentasAsync();
                var todosMovimientos = new List<EuBankApiService.MovimientoData>();
                foreach (var cuenta in cuentas)
                {
                    var movs = await _apiService.GetMovimientosAsync(cuenta.Codigo);
                    todosMovimientos.AddRange(movs);
                }

                var dataTable = new DataTable();
                dataTable.Columns.Add("Cuenta", typeof(string));
                dataTable.Columns.Add("Numero", typeof(int));
                dataTable.Columns.Add("Fecha", typeof(DateTime));
                dataTable.Columns.Add("Tipo", typeof(string));
                dataTable.Columns.Add("Importe", typeof(decimal));
                dataTable.Columns.Add("Saldo Actual", typeof(decimal));

                foreach (var m in todosMovimientos.OrderByDescending(x => x.Fecha))
                {
                    dataTable.Rows.Add(
                        m.CodigoCuenta ?? "",
                        m.Numero,
                        m.Fecha,
                        m.Tipo ?? "",
                        m.Importe,
                        m.SaldoActual
                    );
                }

                GW_Movements.DataSource = dataTable;
                if (GW_Movements.Columns["Importe"] != null)
                    GW_Movements.Columns["Importe"].DefaultCellStyle.Format = "N2";
                if (GW_Movements.Columns["Saldo Actual"] != null)
                    GW_Movements.Columns["Saldo Actual"].DefaultCellStyle.Format = "N2";
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al obtener movimientos: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void btn_Ingresar_Click(object sender, EventArgs e)
        {
            using (frmModalPersonalizado miModal = new frmModalPersonalizado())
            {
                DialogResult resultado = miModal.ShowDialog();
                if (resultado == DialogResult.OK)
                {
                    CrearMovimiento(miModal.Tipo, miModal.CuentaOrigen, miModal.CuentaDestino, miModal.Importe);
                }
            }
        }

        private async void CrearMovimiento(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var resultado = await _apiService.CrearMovimientoAsync(tipo, cuentaOrigen, cuentaDestino ?? "", importe);
                if (resultado != null)
                {
                    if (resultado.codigo == 1)
                    {
                        MessageBox.Show("Movimiento creado exitosamente", "Exito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                        await LoadMovements();
                    }
                    else
                    {
                        MessageBox.Show(resultado.mensaje ?? "", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al crear movimiento: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
        private void GW_Movements_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
        }
    }
}
