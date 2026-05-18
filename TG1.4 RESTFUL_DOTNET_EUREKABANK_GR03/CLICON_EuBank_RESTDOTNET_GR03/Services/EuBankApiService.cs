using System.Net.Http.Json;
using CLICON_EuBank_RESTDOTNET_GR03.Models;

namespace CLICON_EuBank_RESTDOTNET_GR03.Services
{
    public class EuBankApiService
    {
        private readonly HttpClient _http;

        public EuBankApiService()
        {
            var baseUrl = Environment.GetEnvironmentVariable("EUREKABANK_REST_API_URL")
                ?? "https://localhost:44342/api/";
            _http = new HttpClient { BaseAddress = new Uri(baseUrl) };
        }

        public async Task<bool> LoginAsync(string usuario, string clave)
        {
            try
            {
                var response = await _http.PostAsJsonAsync("login", new { usuario, clave });
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
            try { return await _http.GetFromJsonAsync<List<CuentaData>>("cuentas") ?? []; }
            catch { return []; }
        }

        public async Task<List<MovimientoData>> GetMovimientosAsync(string? cuenta)
        {
            try
            {
                var url = string.IsNullOrEmpty(cuenta) ? "movimientos" : $"movimientos?cuenta={cuenta}";
                return await _http.GetFromJsonAsync<List<MovimientoData>>(url) ?? [];
            }
            catch { return []; }
        }

        public async Task<List<string>> GetTiposMovimientoAsync()
        {
            try { return await _http.GetFromJsonAsync<List<string>>("tiposmovimiento") ?? []; }
            catch { return ["DEPOSITO", "RETIRO", "TRANSFERENCIA"]; }
        }

        public async Task<ResultadoResponse?> CrearMovimientoAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var response = await _http.PostAsJsonAsync("movimientos/crear", new { tipo, cuentaOrigen, cuentaDestino, importe });
                if (response.IsSuccessStatusCode)
                    return await response.Content.ReadFromJsonAsync<ResultadoResponse>();
            }
            catch { }
            return null;
        }
    }
}
