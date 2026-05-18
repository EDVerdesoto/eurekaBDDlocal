using System;

namespace WS_EuBank_RESTDOTNET_GR03.DTOs
{
    public class MovimientoDTO
    {
        public int Numero { get; set; }
        public DateTime Fecha { get; set; }
        public string Tipo { get; set; }
        public decimal Importe { get; set; }
        public string Referencia { get; set; }
        public string CodigoCuenta { get; set; }
        public string NombreCliente { get; set; }
        public decimal SaldoActual { get; set; }
    }
}
