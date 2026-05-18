namespace WS_EuBank_RESTDOTNET_GR03.DTOs
{
    public class ResultadoDTO
    {
        public int Codigo { get; set; }
        public string Mensaje { get; set; }

        public ResultadoDTO() { }

        public ResultadoDTO(int codigo, string mensaje)
        {
            Codigo = codigo;
            Mensaje = mensaje;
        }
    }
}
