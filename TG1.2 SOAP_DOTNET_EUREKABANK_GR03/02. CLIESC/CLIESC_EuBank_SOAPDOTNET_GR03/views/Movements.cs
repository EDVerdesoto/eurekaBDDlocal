using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CLIESC_EuBank_SOAPDOTNET_GR03.MovimientosService; // Correct namespace for MovimientosService

namespace CLIESC_EuBank_SOAPDOTNET_GR03.views
{
    public partial class Movements : Form
    {
        public string User { get; set; } // Property to store the user

        public Movements()
        {
            InitializeComponent();
        }

        private async Task LoadMovements()
        {
            try
            {
                var client = new MovimientosServiceClient();
                var cuentas = await client.GetDatosCuentasAsync();

                var todosMovimientos = new List<MovimientoData>();

                foreach (var cuenta in cuentas)
                {
                    try
                    {
                        var movimientosCuenta = await client.GetMovimientosAsync(cuenta.Codigo);
                        todosMovimientos.AddRange(movimientosCuenta);
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show($"Error obteniendo movimientos de cuenta {cuenta.Codigo}: {ex.Message}", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    }
                }

                // Ordenar por fecha descendente
                var movimientosOrdenados = todosMovimientos
                    .OrderByDescending(m => m.Fecha)
                    .ToArray();

                GW_Movements.DataSource = movimientosOrdenados;
                GW_Movements.Refresh();
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
                // Assuming the modal collects Tipo, CuentaOrigen, CuentaDestino, Importe
                // You need to modify frmModalPersonalizado to have fields for these and return them
                DialogResult resultado = miModal.ShowDialog();

                if (resultado == DialogResult.OK)
                {
                    // Get data from modal, e.g., miModal.Tipo, etc.
                    // Then call the service
                    CrearMovimiento(miModal.Tipo, miModal.CuentaOrigen, miModal.CuentaDestino, miModal.Importe);
                }
            }
        }

        private async void CrearMovimiento(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var client = new MovimientosServiceClient();
                var resultado = await client.ProcesarMovimientoAsync(tipo, cuentaOrigen, cuentaDestino ?? "", importe);

                if (resultado.Codigo == 1)
                {
                    MessageBox.Show("Movimiento creado exitosamente", "Éxito", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    // Refresh movements
                    await LoadMovements();
                }
                else
                {
                    MessageBox.Show(resultado.Mensaje, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
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

        private async void Movements_Load_1(object sender, EventArgs e)
        {
            await LoadMovements();
        }
    }
}
