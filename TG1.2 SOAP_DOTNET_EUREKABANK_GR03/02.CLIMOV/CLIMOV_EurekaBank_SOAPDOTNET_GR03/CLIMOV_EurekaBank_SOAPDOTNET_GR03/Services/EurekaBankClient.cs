using System.Globalization;
using System.ServiceModel;
using LoginServiceRef;
using MovimientosServiceRef;

namespace CLIMOV_EurekaBank_SOAPDOTNET_GR03.Services
{
    public sealed class EurekaBankClient
    {
        private const string BaseUrl = "https://dnsoapsoto.dr00p3r.top";

        public async Task<bool> LoginAsync(string usuario, string clave)
        {
            try
            {
                var binding = new BasicHttpsBinding
                {
                    MaxBufferSize = int.MaxValue,
                    MaxReceivedMessageSize = int.MaxValue,
                    Security = new BasicHttpsSecurity { Mode = BasicHttpsSecurityMode.Transport }
                };

                var client = new LoginServiceClient(binding, new EndpointAddress($"{BaseUrl}/Services/LoginService.svc"));
                return await client.LoginAsync(usuario, clave);
            }
            catch (TimeoutException ex)
            {
                throw new HttpRequestException("El servidor tardó demasiado en responder. Verifica tu conexión a Internet.", ex);
            }
            catch (HttpRequestException ex)
            {
                throw new HttpRequestException($"No se pudo conectar al servidor. Verifica que {BaseUrl} sea accesible.", ex);
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al iniciar sesión: {ex.Message}", ex);
            }
        }

        public async Task<List<AccountSummary>> GetAccountsAsync()
        {
            try
            {
                var binding = new BasicHttpsBinding
                {
                    MaxBufferSize = int.MaxValue,
                    MaxReceivedMessageSize = int.MaxValue,
                    Security = new BasicHttpsSecurity { Mode = BasicHttpsSecurityMode.Transport }
                };

                var client = new MovimientosServiceClient(binding, new EndpointAddress($"{BaseUrl}/Services/MovimientosService.svc"));
                var cuentas = await client.GetDatosCuentasAsync();
                return cuentas
                    .Where(c => !string.IsNullOrWhiteSpace(c.Codigo))
                    .Select(c => new AccountSummary(
                        c.Codigo,
                        c.NombreCliente,
                        c.Moneda,
                        c.Saldo,
                        c.Estado,
                        c.EmailCliente))
                    .ToList();
            }
            catch
            {
                return [];
            }
        }

        public async Task<List<MovementSummary>> GetMovementsAsync(string account)
        {
            try
            {
                var binding = new BasicHttpsBinding
                {
                    MaxBufferSize = int.MaxValue,
                    MaxReceivedMessageSize = int.MaxValue,
                    Security = new BasicHttpsSecurity { Mode = BasicHttpsSecurityMode.Transport }
                };

                var client = new MovimientosServiceClient(binding, new EndpointAddress($"{BaseUrl}/Services/MovimientosService.svc"));
                var movimientos = await client.GetMovimientosAsync(account);
                return movimientos
                    .Where(m => !string.IsNullOrWhiteSpace(m.Tipo))
                    .Select(m => new MovementSummary(
                        m.Tipo,
                        m.Importe,
                        m.Fecha,
                        m.Referencia,
                        m.SaldoActual))
                    .ToList();
            }
            catch
            {
                return [];
            }
        }

        public async Task<List<string>> GetMovementTypesAsync()
        {
            try
            {
                var binding = new BasicHttpsBinding
                {
                    MaxBufferSize = int.MaxValue,
                    MaxReceivedMessageSize = int.MaxValue,
                    Security = new BasicHttpsSecurity { Mode = BasicHttpsSecurityMode.Transport }
                };

                var client = new MovimientosServiceClient(binding, new EndpointAddress($"{BaseUrl}/Services/MovimientosService.svc"));
                var tipos = await client.GetTiposMovimientoAsync();
                return tipos
                    .Where(t => !string.IsNullOrWhiteSpace(t))
                    .Distinct(StringComparer.OrdinalIgnoreCase)
                    .ToList();
            }
            catch
            {
                return ["DEPOSITO", "RETIRO", "TRANSFERENCIA"];
            }
        }

        public async Task<OperationResult> ProcessMovementAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            try
            {
                var binding = new BasicHttpsBinding
                {
                    MaxBufferSize = int.MaxValue,
                    MaxReceivedMessageSize = int.MaxValue,
                    Security = new BasicHttpsSecurity { Mode = BasicHttpsSecurityMode.Transport }
                };

                var client = new MovimientosServiceClient(binding, new EndpointAddress($"{BaseUrl}/Services/MovimientosService.svc"));
                var resultado = await client.ProcesarMovimientoAsync(tipo, cuentaOrigen, cuentaDestino, importe);
                return new OperationResult(resultado.Codigo, resultado.Mensaje);
            }
            catch (Exception ex)
            {
                return new OperationResult(-1, ex.Message ?? "No se pudo conectar al servicio.");
            }
        }
    }

    public sealed record AccountSummary(string Codigo, string NombreCliente, string Moneda, decimal Saldo, string Estado, string Email)
    {
        public string ClienteMoneda => $"{NombreCliente} • {Moneda}";
        public string SaldoTexto => $"Saldo: S/ {Saldo:N2}";
        public string EstadoEmail => $"{Estado} • {Email}";
    }

    public sealed record MovementSummary(string Tipo, decimal Importe, DateTime FechaReal, string ReferenciaBase, decimal Saldo)
    {
        public string TipoImporte => $"{Tipo} {(Importe >= 0 ? "+" : string.Empty)}{Importe:N2}";
        public string Fecha => FechaReal == DateTime.MinValue ? string.Empty : FechaReal.ToString("dd/MM/yyyy HH:mm", CultureInfo.CurrentCulture);
        public string Referencia => string.IsNullOrWhiteSpace(ReferenciaBase) ? string.Empty : $"Ref: {ReferenciaBase}";
        public string SaldoTexto => $"Saldo: S/ {Saldo:N2}";
        public Color Color => Importe >= 0 ? Color.FromArgb("#388E3C") : Color.FromArgb("#D32F2F");
    }

    public sealed record OperationResult(int Codigo, string Mensaje)
    {
        public bool Success => Codigo == 1;
    }
}
