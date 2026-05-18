using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web.Script.Serialization;

namespace CLIESC_EuBank_RESTDOTNET_GR03.Services
{
    public class EuBankApiService
    {
        private readonly HttpClient _http;
        private readonly JavaScriptSerializer _json = new JavaScriptSerializer();

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
                var content = new StringContent(_json.Serialize(new { usuario, clave }), Encoding.UTF8, "application/json");
                var response = await _http.PostAsync("login", content);
                if (response.IsSuccessStatusCode)
                {
                    var json = await response.Content.ReadAsStringAsync();
                    var result = _json.Deserialize<LoginResponse>(json);
                    return result != null && result.success;
                }
            }
            catch { }
            return false;
        }

        public async Task<List<CuentaData>> GetCuentasAsync()
        {
            try
            {
                var json = await _http.GetStringAsync("cuentas");
                return _json.Deserialize<List<CuentaData>>(json) ?? new List<CuentaData>();
            }
            catch { return new List<CuentaData>(); }
        }

        public async Task<CuentaData> GetCuentaAsync(string codigo)
        {
            try
            {
                var json = await _http.GetStringAsync($"cuentas/{codigo}");
                return _json.Deserialize<CuentaData>(json);
            }
            catch { return null; }
        }

        public async Task<List<MovimientoData>> GetMovimientosAsync(string cuenta)
        {
            try
            {
                var url = string.IsNullOrEmpty(cuenta) ? "movimientos" : $"movimientos?cuenta={cuenta}";
                var json = await _http.GetStringAsync(url);
                return _json.Deserialize<List<MovimientoData>>(json) ?? new List<MovimientoData>();
            }
            catch { return new List<MovimientoData>(); }
        }

        public async Task<ResultadoResponse> CrearMovimientoAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var data = new { tipo, cuentaOrigen, cuentaDestino, importe };
                var content = new StringContent(_json.Serialize(data), Encoding.UTF8, "application/json");
                var response = await _http.PostAsync("movimientos/crear", content);
                if (response.IsSuccessStatusCode)
                {
                    var json = await response.Content.ReadAsStringAsync();
                    return _json.Deserialize<ResultadoResponse>(json);
                }
            }
            catch { }
            return null;
        }
        private class LoginResponse
        {
            public bool success { get; set; }
        }

        public class CuentaData
        {
            public string Codigo { get; set; }
            public string Moneda { get; set; }
            public decimal Saldo { get; set; }
            public string Estado { get; set; }
            public string NombreCliente { get; set; }
            public string EmailCliente { get; set; }
            public string TelefonoCliente { get; set; }
        }

        public class MovimientoData
        {
            public string CodigoCuenta { get; set; }
            public int Numero { get; set; }
            public DateTime Fecha { get; set; }
            public string Tipo { get; set; }
            public string Referencia { get; set; }
            public decimal Importe { get; set; }
            public decimal SaldoActual { get; set; }
            public string NombreCliente { get; set; }
        }

        public class ResultadoResponse
        {
            public int codigo { get; set; }
            public string mensaje { get; set; }
        }
    }
}
