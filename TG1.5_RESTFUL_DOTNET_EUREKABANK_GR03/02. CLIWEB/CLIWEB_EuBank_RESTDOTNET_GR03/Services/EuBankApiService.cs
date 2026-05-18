using System.Net.Http.Json;

namespace CLIWEB_EuBank_RESTDOTNET_GR03.Services
{
    public class EuBankApiService
    {
        private readonly HttpClient _httpClient;

        public EuBankApiService(HttpClient httpClient, IConfiguration configuration)
        {
            _httpClient = httpClient;
            var baseUrl = configuration["EurekaBankApi:BaseUrl"] ?? "https://dnrestsoto.dr00p3r.top/api/";
            _httpClient.BaseAddress = new Uri(baseUrl);
        }

        public async Task<bool> LoginAsync(string usuario, string clave)
        {
            try
            {
                var response = await _httpClient.PostAsJsonAsync("login", new { usuario, clave });
                if (response.IsSuccessStatusCode)
                {
                    var result = await response.Content.ReadFromJsonAsync<LoginResponse>();
                    return result?.success ?? false;
                }
            }
            catch { }
            return false;
        }

        public async Task<List<CuentaData>> GetCuentasAsync()
        {
            try
            {
                return await _httpClient.GetFromJsonAsync<List<CuentaData>>("cuentas") ?? [];
            }
            catch { return []; }
        }

        public async Task<List<MovimientoData>> GetMovimientosAsync(string cuenta)
        {
            try
            {
                var url = string.IsNullOrEmpty(cuenta) ? "movimientos" : $"movimientos?cuenta={cuenta}";
                return await _httpClient.GetFromJsonAsync<List<MovimientoData>>(url) ?? [];
            }
            catch { return []; }
        }

        public async Task<List<string>> GetTiposMovimientoAsync()
        {
            try
            {
                return await _httpClient.GetFromJsonAsync<List<string>>("tiposmovimiento") ?? [];
            }
            catch { return []; }
        }

        public async Task<ResultadoResponse?> CrearMovimientoAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var response = await _httpClient.PostAsJsonAsync("movimientos/crear", new
                {
                    tipo,
                    cuentaOrigen,
                    cuentaDestino,
                    importe
                });
                if (response.IsSuccessStatusCode)
                {
                    return await response.Content.ReadFromJsonAsync<ResultadoResponse>();
                }
            }
            catch { }
            return null;
        }

        public class LoginResponse { public bool success { get; set; } }
        public class ResultadoResponse { public int codigo { get; set; } public string mensaje { get; set; } = ""; }

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
        }
    }
}
