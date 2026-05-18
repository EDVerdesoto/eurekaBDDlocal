using System.Globalization;
using System.Text;
using System.Xml.Linq;

namespace CLIMOV_EurekaBank_SOAPDOTNET_GR03.Services
{
    public sealed class EurekaBankClient
    {
        private static readonly HttpClient HttpClient = new()
        {
            Timeout = TimeSpan.FromSeconds(60)
        };

        private const string JavaSoapUrl = "https://javasoto.dr00p3r.top/WS_EurekaBank_SOAPJAVA_GR03/WSEurekaBank?wsdl";
        private const string JavaNamespace = "http://ws.monster.edu.ec/";

        private static readonly string[] DotNetBaseUrls =
        [
            "https://dnsoapsoto.dr00p3r.top",
            "http://192.168.100.53:62278",
            "http://10.0.2.2:62278",
            "http://localhost:62278"
        ];

        public async Task<bool> LoginAsync(string usuario, string clave)
        {
            foreach (var baseUrl in DotNetBaseUrls)
            {
                try
                {
                    var dotNetBody = $"""
                        <Login xmlns="http://tempuri.org/">
                          <usuario>{SecurityElement(usuario)}</usuario>
                          <clave>{SecurityElement(clave)}</clave>
                        </Login>
                        """;

                    var document = await PostSoapAsync($"{baseUrl}/Services/LoginService.svc", "http://tempuri.org/ILoginService/Login", dotNetBody);
                    var result = document.Descendants().FirstOrDefault(e => e.Name.LocalName == "LoginResult")?.Value;
                    if (!string.IsNullOrWhiteSpace(result))
                    {
                        return string.Equals(result, "true", StringComparison.OrdinalIgnoreCase);
                    }
                }
                catch
                {
                    // Try the next reachable service endpoint.
                }
            }

            var javaBody = $"""
                <iniciarSesion xmlns="{JavaNamespace}">
                  <usuario>{SecurityElement(usuario)}</usuario>
                  <contrasena>{SecurityElement(clave)}</contrasena>
                </iniciarSesion>
                """;

            var javaDocument = await PostSoapAsync(JavaSoapUrl, $"{JavaNamespace}iniciarSesion", javaBody);
            var javaResult = javaDocument.Descendants().FirstOrDefault(e => e.Name.LocalName == "resultado")?.Value;
            return string.Equals(javaResult, "true", StringComparison.OrdinalIgnoreCase);
        }

        public async Task<List<AccountSummary>> GetAccountsAsync()
        {
            foreach (var baseUrl in DotNetBaseUrls)
            {
                try
                {
                    var dotNetBody = """
                        <GetDatosCuentas xmlns="http://tempuri.org/" />
                        """;

                    var document = await PostSoapAsync($"{baseUrl}/Services/MovimientosService.svc", "http://tempuri.org/IMovimientosService/GetDatosCuentas", dotNetBody);
                    var accounts = ParseAccounts(document, "CuentaData");
                    if (accounts.Count > 0)
                    {
                        return accounts;
                    }
                }
                catch
                {
                    // Try the next reachable service endpoint.
                }
            }

            var javaBody = $"""
                <traerCuentasConClientes xmlns="{JavaNamespace}" />
                """;

            var javaDocument = await PostSoapAsync(JavaSoapUrl, $"{JavaNamespace}traerCuentasConClientes", javaBody);
            return ParseAccounts(javaDocument, "return", "cuentas", "item", "DatosCuenta");
        }

        public async Task<List<MovementSummary>> GetMovementsAsync(string account)
        {
            foreach (var baseUrl in DotNetBaseUrls)
            {
                try
                {
                    var dotNetBody = $"""
                        <GetMovimientos xmlns="http://tempuri.org/">
                          <cuenta>{SecurityElement(account)}</cuenta>
                        </GetMovimientos>
                        """;

                    var document = await PostSoapAsync($"{baseUrl}/Services/MovimientosService.svc", "http://tempuri.org/IMovimientosService/GetMovimientos", dotNetBody);
                    var movements = ParseMovements(document, "MovimientoData");
                    if (movements.Count > 0)
                    {
                        return movements;
                    }
                }
                catch
                {
                    // Try the next reachable service endpoint.
                }
            }

            var javaBody = $"""
                <traerMovimientos xmlns="{JavaNamespace}">
                  <cuenta>{SecurityElement(account)}</cuenta>
                </traerMovimientos>
                """;

            var javaDocument = await PostSoapAsync(JavaSoapUrl, $"{JavaNamespace}traerMovimientos", javaBody);
            return ParseMovements(javaDocument, "return", "movimientos", "item", "MovimientoData");
        }

        public async Task<List<string>> GetMovementTypesAsync()
        {
            foreach (var baseUrl in DotNetBaseUrls)
            {
                try
                {
                    var body = """
                        <GetTiposMovimiento xmlns="http://tempuri.org/" />
                        """;

                    var document = await PostSoapAsync($"{baseUrl}/Services/MovimientosService.svc", "http://tempuri.org/IMovimientosService/GetTiposMovimiento", body);
                    var types = document.Descendants()
                        .Where(e => e.Name.LocalName == "string")
                        .Select(e => e.Value.Trim())
                        .Where(value => !string.IsNullOrWhiteSpace(value))
                        .Distinct(StringComparer.OrdinalIgnoreCase)
                        .ToList();

                    if (types.Count > 0)
                    {
                        return types;
                    }
                }
                catch
                {
                    // Try the next reachable service endpoint.
                }
            }

            return ["DEPOSITO", "RETIRO", "TRANSFERENCIA"];
        }

        public async Task<OperationResult> ProcessMovementAsync(string tipo, string cuentaOrigen, string cuentaDestino, decimal importe)
        {
            Exception? lastError = null;
            foreach (var baseUrl in DotNetBaseUrls)
            {
                try
                {
                    var body = $"""
                        <ProcesarMovimiento xmlns="http://tempuri.org/">
                          <tipo>{SecurityElement(tipo)}</tipo>
                          <cuentaOrigen>{SecurityElement(cuentaOrigen)}</cuentaOrigen>
                          <cuentaDestino>{SecurityElement(cuentaDestino)}</cuentaDestino>
                          <importe>{importe.ToString(CultureInfo.InvariantCulture)}</importe>
                        </ProcesarMovimiento>
                        """;

                    var document = await PostSoapAsync($"{baseUrl}/Services/MovimientosService.svc", "http://tempuri.org/IMovimientosService/ProcesarMovimiento", body);
                    var result = document.Descendants().FirstOrDefault(e => e.Name.LocalName == "ProcesarMovimientoResult");
                    if (result is not null)
                    {
                        return new OperationResult(
                            IntValue(result, "Codigo"),
                            Value(result, "Mensaje"));
                    }
                }
                catch (Exception ex)
                {
                    lastError = ex;
                }
            }

            return new OperationResult(-1, lastError?.Message ?? "No se pudo conectar al servicio.");
        }

        private static async Task<XDocument> PostSoapAsync(string url, string action, string body)
        {
            var envelope = $"""
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
                  <s:Body>{body}</s:Body>
                </s:Envelope>
                """;

            using var request = new HttpRequestMessage(HttpMethod.Post, url);
            request.Headers.TryAddWithoutValidation("SOAPAction", $"\"{action}\"");
            request.Content = new StringContent(envelope, Encoding.UTF8, "text/xml");

            using var timeout = new CancellationTokenSource(TimeSpan.FromSeconds(12));
            using var response = await HttpClient.SendAsync(request, timeout.Token);
            var xml = await response.Content.ReadAsStringAsync();
            response.EnsureSuccessStatusCode();
            return XDocument.Parse(xml);
        }

        private static List<AccountSummary> ParseAccounts(XDocument document, params string[] containerNames)
        {
            var preferredNames = containerNames.Length == 0 ? new[] { "CuentaData" } : containerNames;
            return document.Descendants()
                .Where(e => preferredNames.Contains(e.Name.LocalName) || HasAny(e, "Codigo", "NombreCliente", "Saldo"))
                .Select(e => new AccountSummary(
                    Value(e, "Codigo"),
                    Value(e, "NombreCliente"),
                    Value(e, "Moneda"),
                    DecimalValue(e, "Saldo"),
                    Value(e, "Estado"),
                    FirstValue(e, "EmailCliente", "Email")))
                .Where(a => !string.IsNullOrWhiteSpace(a.Codigo))
                .GroupBy(a => a.Codigo)
                .Select(g => g.First())
                .ToList();
        }

        private static List<MovementSummary> ParseMovements(XDocument document, params string[] containerNames)
        {
            var preferredNames = containerNames.Length == 0 ? new[] { "MovimientoData" } : containerNames;
            return document.Descendants()
                .Where(e => preferredNames.Contains(e.Name.LocalName) || HasAny(e, "Tipo", "Importe", "SaldoActual"))
                .Select(e => new MovementSummary(
                    Value(e, "Tipo"),
                    DecimalValue(e, "Importe"),
                    DateValue(e, "Fecha"),
                    Value(e, "Referencia"),
                    DecimalValue(e, "SaldoActual")))
                .Where(m => !string.IsNullOrWhiteSpace(m.Tipo))
                .ToList();
        }

        private static bool HasAny(XElement parent, params string[] names) => names.Any(name => parent.Elements().Any(e => e.Name.LocalName == name));
        private static string FirstValue(XElement parent, params string[] names) => names.Select(name => Value(parent, name)).FirstOrDefault(value => !string.IsNullOrWhiteSpace(value)) ?? string.Empty;
        private static string SecurityElement(string value) => System.Security.SecurityElement.Escape(value) ?? string.Empty;
        private static string Value(XElement parent, string name) => parent.Elements().FirstOrDefault(e => e.Name.LocalName == name)?.Value ?? string.Empty;
        private static int IntValue(XElement parent, string name) => int.TryParse(Value(parent, name), NumberStyles.Integer, CultureInfo.InvariantCulture, out var value) ? value : -1;
        private static decimal DecimalValue(XElement parent, string name) => decimal.TryParse(Value(parent, name), NumberStyles.Any, CultureInfo.InvariantCulture, out var value) ? value : 0m;
        private static DateTime DateValue(XElement parent, string name) => DateTime.TryParse(Value(parent, name), CultureInfo.InvariantCulture, DateTimeStyles.AssumeLocal, out var value) ? value : DateTime.MinValue;
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
