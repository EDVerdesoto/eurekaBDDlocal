using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CLIESC_EuBank_SOAPDOTNET_GR03.views
{
    public partial class frmModalPersonalizado : Form
    {

        private Dictionary<string, Image> misImagenes = new Dictionary<string, Image>();

        public frmModalPersonalizado()
        {
            InitializeComponent();
        }

        [DllImport("user32.DLL", EntryPoint = "ReleaseCapture")]
        private extern static void ReleaseCapture();
        [DllImport("user32.DLL", EntryPoint = "SendMessage")]
        private extern static void SendMessage(System.IntPtr hWnd, int wMsg, int wParam, int IParam);

        private void ldPanelRound1_MouseDown(object sender, MouseEventArgs e)
        {
            ReleaseCapture();
            SendMessage(this.Handle, 0x112, 0xf012, 0);
        }

        private void frmModalPersonalizado_Load(object sender, EventArgs e)
        {
            pbVisor.SizeMode = PictureBoxSizeMode.Zoom;

            // Load images from project's Resources (add images to Resources first)
            // Replace 'gato', 'perro', 'paisaje' with the actual resource names you used.
            misImagenes.Add("retiro", Properties.Resources.Retiro);
            misImagenes.Add("deposito", Properties.Resources.Depósito);
            misImagenes.Add("transferencia", Properties.Resources.Transferencia);

            foreach (string tipoMovimiento in misImagenes.Keys)
            {
                cbx_movimiento.Items.Add(tipoMovimiento);
            }

            cbx_movimiento.OnSelectedIndexChanged += cbx_movimiento_OnSelectedIndexChanged;

            // Example: show the first image in the PictureBox
            if (cbx_movimiento.Items.Count > 0)
            {
                cbx_movimiento.SelectedIndex = 0;
                // Initially hide Cuenta Destino since default is "retiro"
                label4.Visible = false;
                txt_cuentadestino.Visible = false;
            }
        }

        private void cbx_movimiento_OnSelectedIndexChanged(object sender, EventArgs e)
        {
            string seleccion = cbx_movimiento.SelectedItem.ToString();

            // 2. Buscar la imagen correspondiente en el diccionario
            if (misImagenes.ContainsKey(seleccion))
            {
                // 3. Asignar la imagen al PictureBox
                pbVisor.Image = misImagenes[seleccion];
            }
            else
            {
                // (Opcional) Por si algo falla, limpia la imagen
                pbVisor.Image = null;
            }

            // Show/hide Cuenta Destino based on selection
            if (seleccion == "transferencia")
            {
                label4.Visible = true;
                txt_cuentadestino.Visible = true;
            }
            else
            {
                label4.Visible = false;
                txt_cuentadestino.Visible = false;
                txt_cuentadestino.Text = ""; // Clear the field
            }
        }

        // Properties to expose the input values
        public string Tipo
        {
            get
            {
                var sel = cbx_movimiento.SelectedItem?.ToString();
                switch (sel)
                {
                    case "retiro":
                        return "RETIRO";
                    case "deposito":
                        return "DEPOSITO";
                    case "transferencia":
                        return "TRANSFERENCIA";
                    default:
                        return "";
                }
            }
        }

        public string CuentaOrigen => tbx_cuentaorigen.Text;

        public string CuentaDestino => txt_cuentadestino.Text;

        public decimal Importe => decimal.TryParse(txt_monto.Text, out var importe) ? importe : 0;

        // Button click events
        private void button1_Click(object sender, EventArgs e)
        {
            // OK button
            this.DialogResult = DialogResult.OK;
            this.Close();
        }

        private void btn_cancelar_Click(object sender, EventArgs e)
        {
            // Cancel button
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }
    }
}
