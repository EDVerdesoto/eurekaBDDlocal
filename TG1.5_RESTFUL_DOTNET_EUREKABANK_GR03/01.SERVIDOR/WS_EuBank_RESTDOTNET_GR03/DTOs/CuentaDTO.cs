namespace WS_EuBank_RESTDOTNET_GR03.DTOs
{
    public class CuentaDTO
    {
        public string Codigo { get; set; }
        public string Moneda { get; set; }
        public decimal Saldo { get; set; }
        public string Estado { get; set; }
        public string NombreCliente { get; set; }
        public string EmailCliente { get; set; }
        public string TelefonoCliente { get; set; }
    }
}
