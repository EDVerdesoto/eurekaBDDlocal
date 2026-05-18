using System.Net.Http.Json;
using System.Text.Json.Serialization;

namespace CLIMOV_EurekaBank_RESTDOTNET_GR03.Services
{
    public sealed class EurekaBankClient
    {
        private static readonly HttpClient HttpClient = CreateHttpClient();
        private static readonly string[] BaseUrls = ["https://dnrestsoto.dr00p3r.top/api"];

        public static string? LastConnectionError { get; private set; }

        private static HttpClient CreateHttpClient()
        {
            var handler = new HttpClientHandler
            {
                ServerCertificateCustomValidationCallback = (_, _, _, _) => true
            };

            return new HttpClient(handler) { Timeout = TimeSpan.FromSeconds(10) };
        }

        public async Task<bool> LoginAsync(string usuario, string clave)
        {
            LastConnectionError = null;

            foreach (var baseUrl in BaseUrls)
            {
                try
                {
                    var response = await HttpClient.PostAsJsonAsync($"{baseUrl}/login", new { usuario, clave });
                    if (response.IsSuccessStatusCode)
                    {
                        var result = await response.Content.ReadFromJsonAsync<LoginResponse>();
                        if (result?.success == true)
                        {
                            LastConnectionError = null;
                            return true;
                        }

                        LastConnectionError = "Credenciales inválidas.";
                    }
                    else
                    {
                        LastConnectionError = $"El servidor respondió {(int)response.StatusCode} ({response.ReasonPhrase}).";
                    }
                }
                catch (Exception ex)
                {
                    LastConnectionError = ex.Message;
                }
            }

            return false;
        }

        public async Task<List<AccountSummary>> GetAccountsAsync()
        {
            foreach (var baseUrl in BaseUrls)
            {
                try
                {
                    var accounts = await HttpClient.GetFromJsonAsync<List<CuentaDTO>>($"{baseUrl}/cuentas");
                    if (accounts != null && accounts.Count > 0)
                        return accounts.Select(a => new AccountSummary(a.codigo, a.nombreCliente, a.moneda, a.saldo, a.estado, a.emailCliente)).ToList();
                }
                catch { }
            }
            return [];
        }

        public async Task<List<MovementSummary>> GetMovementsAsync(string account)
        {
            foreach (var baseUrl in BaseUrls)
            {
                try
                {
                    var url = $"{baseUrl}/movimientos?cuenta={account}";
                    var movements = await HttpClient.GetFromJsonAsync<List<MovimientoDTO>>(url);
                    if (movements != null && movements.Count > 0)
                        return movements.Select(m => new MovementSummary(m.tipo, m.importe, m.fecha, m.referencia ?? "", m.saldoActual)).ToList();
                }
                catch { }
            }
            return [];
        }

        public async Task<List<string>> GetMovementTypesAsync()
        {
            foreach (var baseUrl in BaseUrls)
            {
                try
                {
                    var types = await HttpClient.GetFromJsonAsync<List<string>>($"{baseUrl}/tiposmovimiento");
                    if (types != null && types.Count > 0) return types;
                }
                catch { }
            }
            return ["DEPOSITO", "RETIRO", "TRANSFERENCIA"];
        }

        public async Task<OperationResult> ProcessMovementAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            foreach (var baseUrl in BaseUrls)
            {
                try
                {
                    var response = await HttpClient.PostAsJsonAsync($"{baseUrl}/movimientos/crear", new { tipo, cuentaOrigen, cuentaDestino, importe });
                    if (response.IsSuccessStatusCode)
                    {
                        var result = await response.Content.ReadFromJsonAsync<ResultadoDTO>();
                        if (result != null) return new OperationResult(result.codigo, result.mensaje);
                    }
                }
                catch { }
            }
            return new OperationResult(-1, "No se pudo conectar al servicio.");
        }

        private class LoginResponse { public bool success { get; set; } }
        private class CuentaDTO { public string codigo { get; set; } = ""; public string moneda { get; set; } = ""; public decimal saldo { get; set; } public string estado { get; set; } = ""; public string nombreCliente { get; set; } = ""; public string emailCliente { get; set; } = ""; public string telefonoCliente { get; set; } = ""; }
        private class MovimientoDTO { public string tipo { get; set; } = ""; public decimal importe { get; set; } public DateTime fecha { get; set; } public string referencia { get; set; } = ""; public decimal saldoActual { get; set; } public string codigoCuenta { get; set; } = ""; }
        private class ResultadoDTO { public int codigo { get; set; } public string mensaje { get; set; } = ""; }
    }

    public sealed record AccountSummary(string Codigo, string NombreCliente, string Moneda, decimal Saldo, string Estado, string Email)
    {
        public string NombreMoneda => Moneda == "01" ? "Soles" : Moneda == "02" ? "Dolares" : Moneda;
        public string ClienteMoneda => $"{NombreCliente} • {NombreMoneda}";
        public string SaldoTexto => $"Saldo: S/ {Saldo:N2}";
        public string EstadoEmail => $"{Estado} • {Email}";
    }

    public sealed record MovementSummary(string Tipo, decimal Importe, DateTime FechaReal, string ReferenciaBase, decimal Saldo)
    {
        public string TipoImporte => $"{Tipo} {(Importe >= 0 ? "+" : "")}{Importe:N2}";
        public string Fecha => FechaReal == DateTime.MinValue ? "" : FechaReal.ToString("dd/MM/yyyy HH:mm");
        public string Referencia => string.IsNullOrWhiteSpace(ReferenciaBase) ? "" : $"Ref: {ReferenciaBase}";
        public string SaldoTexto => $"Saldo: S/ {Saldo:N2}";
        public Color Color => Importe >= 0 ? Color.FromArgb("#388E3C") : Color.FromArgb("#D32F2F");
    }

    public sealed record OperationResult(int Codigo, string Mensaje)
    {
        public bool Success => Codigo == 1;
    }
}
