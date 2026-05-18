using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using CLICON_EuBank_SOAPDOTNET_GR03.Models;

namespace CLICON_EuBank_SOAPDOTNET_GR03.Views
{
    public class ConsoleView
    {
        public void ShowWelcome()
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("╔═══════════════════════════════════════════╗");
            Console.WriteLine("║  Cliente SOAP - EuBank (Consola)          ║");
            Console.WriteLine("╚═══════════════════════════════════════════╝");
            Console.ResetColor();
        }

        public Tuple<string, string> GetLoginCredentials()
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("╔══════════════════════════════════════════╗");
            Console.WriteLine("║         Sistema de Login - EuBank        ║");
            Console.WriteLine("╚══════════════════════════════════════════╝");
            Console.ResetColor();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("\nUsuario: ");
            Console.ResetColor();
            string usuario = Console.ReadLine();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Contraseña: ");
            Console.ResetColor();
            string clave = Console.ReadLine();

            return Tuple.Create(usuario, clave);
        }

        public void ShowLoginSuccess(string user)
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("\n✓ Autenticación exitosa. Bienvenido, " + user + "!");
            Console.ResetColor();
        }

        public void ShowLoginError(string message)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("\n✗ " + message);
            Console.ResetColor();
        }

        public void ShowError(string message)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine("\n✗ " + message);
            Console.ResetColor();
        }

        public void ShowMessage(string message)
        {
            Console.WriteLine(message);
        }

        public bool AskRetry()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.Write("¿Desea intentar de nuevo? (s/n): ");
            Console.ResetColor();
            string opcion = Console.ReadLine();
            if (opcion != null)
                opcion = opcion.Trim().ToLower();
            return opcion == "s";
        }

        public void ShowAuthenticating()
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.Write("\nAutenticando (SOAP)...");
            Console.ResetColor();
        }

        public void ShowAuthOk()
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine(" ✓");
            Console.ResetColor();
        }

        public void ShowAuthFail()
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine(" ✗");
            Console.ResetColor();
        }

        public int ShowDashboardMenu()
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("╔══════════════════════════════════════════╗");
            Console.WriteLine("║        Dashboard - EuBank (SOAP)         ║");
            Console.WriteLine("╚══════════════════════════════════════════╝");
            Console.ResetColor();
            Console.WriteLine("\n1. Ver Cuentas de Clientes");
            Console.WriteLine("2. Ver Movimientos (por cuenta o todos)");
            Console.WriteLine("3. Registrar Depósito");
            Console.WriteLine("4. Registrar Retiro");
            Console.WriteLine("5. Registrar Transferencia");
            Console.WriteLine("6. Salir");
            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("\nSeleccione una opción: ");
            Console.ResetColor();
            string input = Console.ReadLine();
            return int.TryParse(input, out int opcion) ? opcion : 0;
        }

        public void DisplayCuentas(List<Account> cuentas)
        {
            Console.WriteLine();
            if (cuentas.Any())
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                string header = string.Format("{0,-10} | {1,-30} | {2,-10} | {3,-10} | {4,12}",
                        "CUENTA", "CLIENTE", "MONEDA", "ESTADO", "SALDO");
                Console.WriteLine(header);
                Console.WriteLine(new string('-', header.Length));
                Console.ResetColor();

                foreach (var c in cuentas)
                {
                    Console.WriteLine("{0,-10} | {1,-30} | {2,-10} | {3,-10} | {4,12:N2}",
                            c.Codigo,
                            c.NombreCliente,
                            c.Moneda,
                            c.Estado,
                            c.Saldo);
                }

                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine(new string('-', header.Length));
                Console.ResetColor();
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("No se encontraron cuentas.");
                Console.ResetColor();
            }
            Console.WriteLine();
        }

        public string GetMovimientoFilter()
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Ingrese el número de cuenta (ej. 00100001) o presione Enter para ver todos: ");
            Console.ResetColor();
            string input = Console.ReadLine();
            return input != null ? input.Trim() : "";
        }

        public void DisplayMovimientos(List<Movement> movimientos)
        {
            Console.WriteLine();
            if (movimientos.Any())
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                string header = string.Format("{0,-10} | {1,-4} | {2,-12} | {3,-25} | {4,12} | {5,12}",
                        "CUENTA", "NRO", "FECHA", "TIPO", "IMPORTE", "SALDO");
                Console.WriteLine(header);
                Console.WriteLine(new string('-', header.Length));
                Console.ResetColor();

                foreach (var m in movimientos)
                {
                    string tipo = m.Tipo != null ? m.Tipo : "N/A";
                    Console.WriteLine("{0,-10} | {1,-4} | {2,-12} | {3,-25} | {4,12:N2} | {5,12:N2}",
                            m.CodigoCuenta,
                            m.Numero,
                            m.Fecha.ToString("dd/MM/yyyy"),
                            tipo,
                            m.Importe,
                            m.SaldoActual);
                }

                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine(new string('-', header.Length));
                Console.ResetColor();
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("No se encontraron movimientos.");
                Console.ResetColor();
            }
            Console.WriteLine();
        }

        public MovementRequest GetDepositoData()
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("--- Registrar Depósito ---");
            Console.ResetColor();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Número de Cuenta: ");
            Console.ResetColor();
            string cuenta = Console.ReadLine();
            if (string.IsNullOrEmpty(cuenta))
            {
                ShowError("La cuenta no puede estar vacía.");
                return null;
            }

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Importe: ");
            Console.ResetColor();
            string importeStr = Console.ReadLine();
            if (!decimal.TryParse(importeStr != null ? importeStr.Trim().Replace(',', '.') : "", NumberStyles.Any, CultureInfo.InvariantCulture, out decimal importe) || importe <= 0)
            {
                ShowError("Importe no válido.");
                return null;
            }

            return new MovementRequest { Tipo = "DEPOSITO", CuentaOrigen = cuenta, CuentaDestino = null, Importe = importe };
        }

        public MovementRequest GetRetiroData()
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("--- Registrar Retiro ---");
            Console.ResetColor();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Número de Cuenta: ");
            Console.ResetColor();
            string cuenta = Console.ReadLine();
            if (string.IsNullOrEmpty(cuenta))
            {
                ShowError("La cuenta no puede estar vacía.");
                return null;
            }

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Importe: ");
            Console.ResetColor();
            string importeStr = Console.ReadLine();
            if (!decimal.TryParse(importeStr != null ? importeStr.Trim().Replace(',', '.') : "", NumberStyles.Any, CultureInfo.InvariantCulture, out decimal importe) || importe <= 0)
            {
                ShowError("Importe no válido.");
                return null;
            }

            return new MovementRequest { Tipo = "RETIRO", CuentaOrigen = cuenta, CuentaDestino = null, Importe = importe };
        }

        public MovementRequest GetTransferenciaData()
        {
            Console.WriteLine();
            Console.ForegroundColor = ConsoleColor.Cyan;
            Console.WriteLine("--- Registrar Transferencia ---");
            Console.ResetColor();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Número de Cuenta Origen: ");
            Console.ResetColor();
            string origen = Console.ReadLine();
            if (string.IsNullOrEmpty(origen))
            {
                ShowError("La cuenta origen no puede estar vacía.");
                return null;
            }

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Número de Cuenta Destino: ");
            Console.ResetColor();
            string destino = Console.ReadLine();
            if (string.IsNullOrEmpty(destino))
            {
                ShowError("La cuenta destino no puede estar vacía.");
                return null;
            }

            if (origen.Equals(destino, StringComparison.OrdinalIgnoreCase))
            {
                ShowError("Las cuentas de origen y destino no pueden ser la misma.");
                return null;
            }

            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("Importe: ");
            Console.ResetColor();
            string importeStr = Console.ReadLine();
            if (!decimal.TryParse(importeStr != null ? importeStr.Trim().Replace(',', '.') : "", NumberStyles.Any, CultureInfo.InvariantCulture, out decimal importe) || importe <= 0)
            {
                ShowError("Importe no válido.");
                return null;
            }

            return new MovementRequest { Tipo = "TRANSFERENCIA", CuentaOrigen = origen, CuentaDestino = destino, Importe = importe };
        }

        public void ShowOperationResult(CrearMovimientoResponse response)
        {
            if (response.Success)
            {
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine("\n¡Operación exitosa!");
                Console.ResetColor();
            }
            else
            {
                ShowError(response.Message != null ? response.Message : "Error desconocido al procesar el movimiento.");
            }
        }
    }
}
