using CLICON_EuBank_RESTDOTNET_GR03.Services;
using CLICON_EuBank_RESTDOTNET_GR03.Views;

Console.OutputEncoding = System.Text.Encoding.UTF8;
Console.InputEncoding = System.Text.Encoding.UTF8;

var service = new EuBankApiService();
var view = new ConsoleView();

view.ShowWelcome();

int intentos = 0;
const int MAX_INTENTOS = 3;
string? currentUser = null;

while (intentos < MAX_INTENTOS)
{
    var (usuario, clave) = view.GetLoginCredentials();
    if (string.IsNullOrEmpty(usuario)) { view.ShowError("El usuario no puede estar vacio."); continue; }
    if (string.IsNullOrEmpty(clave)) { view.ShowError("La contrasena no puede estar vacia."); continue; }

    view.ShowAuthenticating();
    try
    {
        if (await service.LoginAsync(usuario, clave))
        {
            view.ShowAuthOk();
            view.ShowLoginSuccess(usuario);
            currentUser = usuario;
            break;
        }
        else
        {
            view.ShowAuthFail();
            intentos++;
            view.ShowLoginError($"Autenticacion fallida. (Intento {intentos}/{MAX_INTENTOS})\n");
            if (intentos >= MAX_INTENTOS) { view.ShowError("Maximo de intentos alcanzado."); break; }
            if (!view.AskRetry()) break;
        }
    }
    catch (Exception ex)
    {
        view.ShowAuthFail();
        intentos++;
        view.ShowLoginError($"Error: {ex.Message} (Intento {intentos}/{MAX_INTENTOS})");
        if (intentos >= MAX_INTENTOS) { view.ShowError("Maximo de intentos alcanzado."); break; }
        if (!view.AskRetry()) break;
    }
}

if (currentUser == null)
{
    view.ShowMessage("\nNo se pudo autenticar. Saliendo...");
    return;
}

while (true)
{
    int opcion = view.ShowDashboardMenu();
    switch (opcion)
    {
        case 1:
            try { var cuentas = await service.GetCuentasAsync(); view.DisplayCuentas(cuentas); }
            catch (Exception ex) { view.ShowError("Error al cargar cuentas: " + ex.Message); }
            break;
        case 2:
            try { string? filtro = view.GetMovimientoFilter(); var movs = await service.GetMovimientosAsync(filtro); view.DisplayMovimientos(movs); }
            catch (Exception ex) { view.ShowError("Error al cargar movimientos: " + ex.Message); }
            break;
        case 3:
            try
            {
                var tipos = await service.GetTiposMovimientoAsync();
                var (tipo, cuentaOrigen, cuentaDestino, importe) = view.GetCrearMovimientoData(tipos);
                if (cuentaOrigen != null)
                {
                    var result = await service.CrearMovimientoAsync(tipo, cuentaOrigen, cuentaDestino ?? "", importe);
                    view.ShowOperationResult(result);
                }
            }
            catch (Exception ex) { view.ShowError("Error al crear movimiento: " + ex.Message); }
            break;
        case 4:
            view.ShowMessage("\nGracias por usar EuBank!");
            return;
        default:
            view.ShowError("Opcion no valida.");
            break;
    }
}
