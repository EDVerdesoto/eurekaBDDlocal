using System.ServiceModel;
using LoginServiceRef;
using MovimientoServiceRef;

namespace CLIWEB_EuBank_SOAPDOTNET_GR03.Services;

public interface IEurekaBankSoapService
{
    Task<bool> LoginAsync(string usuario, string clave);
    Task<IReadOnlyList<CuentaData>> GetCuentasAsync();
    Task<IReadOnlyList<string>> GetTiposMovimientoAsync();
    Task<IReadOnlyList<MovimientoData>> GetMovimientosAsync(string cuenta);
    Task<IReadOnlyList<MovimientoData>> GetTodosMovimientosAsync();
    Task<ResultadoOperacion> ProcesarMovimientoAsync(string tipo, string cuentaOrigen, string? cuentaDestino, decimal importe);
}

public sealed class EurekaBankSoapService : IEurekaBankSoapService
{
    private readonly string _baseUrl;

    public EurekaBankSoapService(IConfiguration configuration)
    {
        _baseUrl = (configuration["SoapServices:BaseUrl"] ?? "https://dnsoapsoto.dr00p3r.top/api/").TrimEnd('/');
    }

    public async Task<bool> LoginAsync(string usuario, string clave)
    {
        var client = CreateLoginClient();
        try
        {
            return await client.LoginAsync(usuario, clave);
        }
        finally
        {
            await CloseOrAbortAsync(client);
        }
    }

    public async Task<IReadOnlyList<CuentaData>> GetCuentasAsync()
    {
        var client = CreateMovimientosClient();
        try
        {
            return await client.GetDatosCuentasAsync();
        }
        finally
        {
            await CloseOrAbortAsync(client);
        }
    }

    public async Task<IReadOnlyList<string>> GetTiposMovimientoAsync()
    {
        var client = CreateMovimientosClient();
        try
        {
            return await client.GetTiposMovimientoAsync();
        }
        finally
        {
            await CloseOrAbortAsync(client);
        }
    }

    public async Task<IReadOnlyList<MovimientoData>> GetMovimientosAsync(string cuenta)
    {
        var client = CreateMovimientosClient();
        try
        {
            return await client.GetMovimientosAsync(cuenta);
        }
        finally
        {
            await CloseOrAbortAsync(client);
        }
    }

    public async Task<IReadOnlyList<MovimientoData>> GetTodosMovimientosAsync()
    {
        var cuentas = await GetCuentasAsync();
        var movimientos = new List<MovimientoData>();

        foreach (var cuenta in cuentas.Where(c => !string.IsNullOrWhiteSpace(c.Codigo)))
        {
            var movimientosCuenta = await GetMovimientosAsync(cuenta.Codigo);
            movimientos.AddRange(movimientosCuenta);
        }

        return movimientos;
    }

    public async Task<ResultadoOperacion> ProcesarMovimientoAsync(string tipo, string cuentaOrigen, string? cuentaDestino, decimal importe)
    {
        var client = CreateMovimientosClient();
        try
        {
            return await client.ProcesarMovimientoAsync(tipo, cuentaOrigen, cuentaDestino ?? string.Empty, importe);
        }
        finally
        {
            await CloseOrAbortAsync(client);
        }
    }

    private LoginServiceClient CreateLoginClient()
    {
        return new LoginServiceClient(CreateBinding(), new EndpointAddress(BuildServiceUrl("Services/LoginService.svc")));
    }

    private MovimientosServiceClient CreateMovimientosClient()
    {
        return new MovimientosServiceClient(CreateBinding(), new EndpointAddress(BuildServiceUrl("Services/MovimientosService.svc")));
    }

    private string BuildServiceUrl(string relativePath)
    {
        return $"{_baseUrl}/{relativePath.TrimStart('/')}";
    }

    private static BasicHttpsBinding CreateBinding()
    {
        return new BasicHttpsBinding(BasicHttpsSecurityMode.Transport)
        {
            MaxBufferSize = int.MaxValue,
            MaxReceivedMessageSize = int.MaxValue,
            ReaderQuotas = System.Xml.XmlDictionaryReaderQuotas.Max
        };
    }

    private static async Task CloseOrAbortAsync(ICommunicationObject client)
    {
        try
        {
            if (client.State == CommunicationState.Faulted)
            {
                client.Abort();
                return;
            }

            await Task.Factory.FromAsync(client.BeginClose(null, null), client.EndClose);
        }
        catch
        {
            client.Abort();
        }
    }
}
