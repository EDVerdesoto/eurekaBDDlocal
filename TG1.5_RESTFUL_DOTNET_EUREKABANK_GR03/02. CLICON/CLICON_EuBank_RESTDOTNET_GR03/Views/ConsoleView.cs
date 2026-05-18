using CLICON_EuBank_RESTDOTNET_GR03.Models;

namespace CLICON_EuBank_RESTDOTNET_GR03.Views
{
    public class ConsoleView
    {
        private static string NombreMoneda(string moneda)
        {
            switch (moneda)
            {
                case "01": return "Soles";
                case "02": return "Dolares";
                default: return moneda;
            }
        }

        public void ShowWelcome()
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("================================================");
            Console.WriteLine("       Cliente REST - EuBank (Consola)          ");
            Console.WriteLine("================================================");
            Console.ResetColor();
            Console.WriteLine();
        }

        public (string usuario, string clave) GetLoginCredentials()
        {
            Console.Write("\nUsuario: ");
            string? usuario = Console.ReadLine();
            Console.Write("Contrasena: ");
            string clave = ReadPassword();
            return (usuario ?? "", clave);
        }

        private static string ReadPassword()
        {
            var pass = string.Empty;
            while (true)
            {
                var key = Console.ReadKey(true);
                if (key.Key == ConsoleKey.Enter) break;
                if (key.Key == ConsoleKey.Backspace && pass.Length > 0)
                {
                    pass = pass[..^1];
                    Console.Write("\b \b");
                }
                else if (!char.IsControl(key.KeyChar))
                {
                    pass += key.KeyChar;
                    Console.Write("*");
                }
            }
            Console.WriteLine();
            return pass;
        }

        public void ShowAuthenticating()
        {
            Console.WriteLine();
            Console.WriteLine("Autenticando (REST)...");
        }

        public void ShowAuthOk()
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("[OK] Autenticacion exitosa.");
            Console.ResetColor();
        }

        public void ShowLoginSuccess(string usuario)
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine($"Bienvenido, {usuario}!");
            Console.ResetColor();
            Thread.Sleep(1500);
        }

        public void ShowAuthFail()
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("[FAIL] Autenticacion fallida.");
            Console.ResetColor();
        }

        public void ShowLoginError(string mensaje)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(mensaje);
            Console.ResetColor();
        }

        public void ShowError(string mensaje)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine($"*** ERROR: {mensaje}");
            Console.ResetColor();
            Thread.Sleep(1200);
        }

        public void ShowMessage(string mensaje)
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine(mensaje);
            Console.ResetColor();
            Thread.Sleep(1500);
        }

        public bool AskRetry()
        {
            Console.WriteLine();
            Console.Write("Desea intentar nuevamente? (S/N): ");
            var key = Console.ReadKey(true);
            return key.Key == ConsoleKey.S;
        }

        public int ShowDashboardMenu()
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("================================================");
            Console.WriteLine("         Dashboard - EuBank (REST)              ");
            Console.WriteLine("================================================");
            Console.ResetColor();
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.White;
            Console.WriteLine("  1. Consultar cuentas");
            Console.WriteLine("  2. Consultar movimientos");
            Console.WriteLine("  3. Crear movimiento");
            Console.WriteLine("  4. Salir");
            Console.ResetColor();
            Console.WriteLine();
            Console.Write("Seleccione una opcion: ");

            while (true)
            {
                string? input = Console.ReadLine();
                if (int.TryParse(input, out int opcion) && opcion >= 1 && opcion <= 4)
                    return opcion;
                Console.Write("Opcion no valida. Seleccione 1-4: ");
            }
        }

        public void DisplayCuentas(List<CuentaData> cuentas)
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("=== CUENTAS BANCARIAS ===");
            Console.ResetColor();
            Console.WriteLine();

            if (cuentas.Count == 0)
            {
                Console.WriteLine("No se encontraron cuentas.");
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine($"{"Codigo",-12} {"Moneda",-12} {"Saldo",14} {"Estado",-12} {"Cliente",-25} {"Email",-30} {"Telefono",-15}");
                Console.ResetColor();
                Console.WriteLine(new string('-', 120));

                foreach (var c in cuentas)
                {
                    Console.WriteLine($"{c.codigo,-12} {NombreMoneda(c.moneda),-12} {c.saldo,14:N2} {c.estado,-12} {c.nombreCliente,-25} {c.emailCliente,-30} {c.telefonoCliente,-15}");
                }
            }

            Console.WriteLine();
            Console.Write("Presione ENTER para continuar...");
            Console.ReadLine();
        }

        public string? GetMovimientoFilter()
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("=== CONSULTA DE MOVIMIENTOS ===");
            Console.ResetColor();
            Console.WriteLine();
            Console.WriteLine("Deje vacio para consultar todos los movimientos.");
            Console.Write("Codigo de cuenta (opcional): ");
            string? cuenta = Console.ReadLine();
            return string.IsNullOrWhiteSpace(cuenta) ? null : cuenta.Trim();
        }

        public void DisplayMovimientos(List<MovimientoData> movimientos)
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("=== MOVIMIENTOS BANCARIOS ===");
            Console.ResetColor();
            Console.WriteLine();

            if (movimientos.Count == 0)
            {
                Console.WriteLine("No se encontraron movimientos.");
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine($"{"Cuenta",-12} {"#",5} {"Fecha",-12} {"Tipo",-18} {"Referencia",-20} {"Importe",12} {"Saldo Actual",14} {"Cliente",-25}");
                Console.ResetColor();
                Console.WriteLine(new string('-', 125));

                foreach (var m in movimientos)
                {
                    decimal importe = m.importe;
                    if (m.tipo == "RETIRO" || m.tipo == "TRANSFERENCIA")
                        importe = -Math.Abs(importe);

                    Console.ForegroundColor = importe >= 0 ? ConsoleColor.Green : ConsoleColor.Red;
                    Console.WriteLine($"{m.codigoCuenta,-12} {m.numero,5} {m.fecha,-12:dd/MM/yyyy} {m.tipo,-18} {m.referencia,-20} {importe,12:N2} {m.saldoActual,14:N2} {m.nombreCliente,-25}");
                    Console.ResetColor();
                }
            }

            Console.WriteLine();
            Console.Write("Presione ENTER para continuar...");
            Console.ReadLine();
        }

        public (string tipo, string? cuentaOrigen, string? cuentaDestino, decimal importe) GetCrearMovimientoData(List<string> tipos)
        {
            Console.Clear();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("=== CREAR MOVIMIENTO ===");
            Console.ResetColor();
            Console.WriteLine();

            Console.WriteLine("Tipos de movimiento disponibles:");
            for (int i = 0; i < tipos.Count; i++)
                Console.WriteLine($"  {i + 1}. {tipos[i]}");

            Console.WriteLine();
            int tipoIndex = 0;
            while (true)
            {
                Console.Write("Seleccione el tipo (numero): ");
                string? input = Console.ReadLine();
                if (int.TryParse(input, out tipoIndex) && tipoIndex >= 1 && tipoIndex <= tipos.Count)
                    break;
                Console.WriteLine($"Seleccione un numero entre 1 y {tipos.Count}.");
            }

            string tipo = tipos[tipoIndex - 1];

            Console.Write("Cuenta origen: ");
            string? cuentaOrigen = Console.ReadLine();
            if (string.IsNullOrWhiteSpace(cuentaOrigen)) cuentaOrigen = null;

            string? cuentaDestino = null;
            if (tipo == "TRANSFERENCIA")
            {
                Console.Write("Cuenta destino: ");
                cuentaDestino = Console.ReadLine();
                if (string.IsNullOrWhiteSpace(cuentaDestino)) cuentaDestino = null;
            }

            decimal importe = 0;
            while (true)
            {
                Console.Write("Importe: ");
                string? input = Console.ReadLine();
                if (decimal.TryParse(input, out importe) && importe > 0)
                    break;
                Console.WriteLine("Ingrese un importe valido mayor a cero.");
            }

            return (tipo, cuentaOrigen, cuentaDestino, importe);
        }

        public void ShowOperationResult(ResultadoResponse? result)
        {
            Console.WriteLine();
            if (result == null)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("*** ERROR: No se pudo completar la operacion.");
                Console.ResetColor();
            }
            else if (result.codigo == 1)
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine($"[OK] Operacion exitosa: {result.mensaje}");
                Console.ResetColor();
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"[FAIL] Codigo {result.codigo}: {result.mensaje}");
                Console.ResetColor();
            }

            Console.WriteLine();
            Console.Write("Presione ENTER para continuar...");
            Console.ReadLine();
        }
    }
}
