namespace CLICON_EuBank_RESTDOTNET_GR03.Models
{
    public class LoginResponse { public bool success { get; set; } }

    public class CuentaData
    {
        public string codigo { get; set; } = "";
        public string moneda { get; set; } = "";
        public decimal saldo { get; set; }
        public string estado { get; set; } = "";
        public string nombreCliente { get; set; } = "";
        public string emailCliente { get; set; } = "";
        public string telefonoCliente { get; set; } = "";
    }

    public class MovimientoData
    {
        public string codigoCuenta { get; set; } = "";
        public int numero { get; set; }
        public DateTime fecha { get; set; }
        public string tipo { get; set; } = "";
        public string referencia { get; set; } = "";
        public decimal importe { get; set; }
        public decimal saldoActual { get; set; }
        public string nombreCliente { get; set; } = "";
    }

    public class ResultadoResponse { public int codigo { get; set; } public string mensaje { get; set; } = ""; }
}
