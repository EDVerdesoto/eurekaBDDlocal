package ec.edu.monster.views;

import ec.edu.monster.ws.eurekabank.*; 
import ec.edu.monster.servicios.EuBankService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList; 
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class DashboardVista {

    private final EuBankService euBankService;
    private final Scanner scanner;
    private final DecimalFormat decimalFormat;

    public DashboardVista(EuBankService euBankService) {
        this.euBankService = euBankService;
        this.scanner = new Scanner(System.in, "UTF-8");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        this.decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }

    public void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════╗");
            System.out.println("║        Dashboard - EuBank (SOAP)         ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println("\n1. Ver Cuentas de Clientes");
            System.out.println("2. Ver Movimientos (por cuenta o todos)"); 
            System.out.println("3. Registrar Depósito");
            System.out.println("4. Registrar Retiro");
            System.out.println("5. Registrar Transferencia");
            System.out.println("6. Salir");
            System.out.print("\nSeleccione una opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                mostrarError("Opción no válida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    mostrarCuentasClientes();
                    break;
                case 2:
                    mostrarMovimientos(); 
                    break;
                case 3:
                    registrarMovimiento("DEPOSITO");
                    break;
                case 4:
                    registrarMovimiento("RETIRO");
                    break;
                case 5:
                    registrarTransferencia();
                    break;
                case 6:
                    System.out.println("\n¡Gracias por usar EuBank! ¡Adiós!");
                    return;
                default:
                    mostrarError("Opción no válida.");
                    break;
            }
        }
    }

    private void mostrarCuentasClientes() {
        System.out.println("--- Cuentas de Clientes ---");
        try {
            System.out.print("Cargando cuentas...");
            List<DatosCuenta> cuentas = euBankService.traerCuentasConClientes();
            System.out.println(" ✓");
            if (cuentas.isEmpty()) {
                System.out.println("No se encontraron cuentas.");
            } else {
                String header = String.format("%-10s | %-30.30s | %-10s | %-10s | %12s",
                        "CUENTA", "CLIENTE", "MONEDA", "ESTADO", "SALDO");
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                for (DatosCuenta c : cuentas) {
                    System.out.printf("%-10s | %-30.30s | %-10s | %-10s | %12s%n",
                            c.getCodigo(),
                            c.getNombreCliente(),
                            c.getMoneda(),
                            c.getEstado(),
                            decimalFormat.format(c.getSaldo()));
                }
            }
        } catch (Exception e) {
            mostrarError("Error al cargar cuentas: " + e.getMessage());
        }
    }

    private void mostrarMovimientos() {
        System.out.println("--- Movimientos por Cuenta ---");
        System.out.print("Ingrese el número de cuenta (ej. 00100001) o presione Enter para ver todos: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            mostrarTodosLosMovimientos();
        } else {
            mostrarMovimientosDeUnaCuenta(cuenta);
        }
    }

    private void mostrarTodosLosMovimientos() {
        System.out.print("Cargando todas las cuentas...");
        try {
            List<DatosCuenta> cuentas = euBankService.traerCuentasConClientes();
            System.out.println(" ✓");
            List<MovimientoData> todosMovimientos = new ArrayList<>();
            
            System.out.print("Cargando todos los movimientos...");
            for (DatosCuenta cuenta : cuentas) {
                try {
                    todosMovimientos.addAll(euBankService.traerMovimientos(cuenta.getCodigo()));
                } catch (Exception ex) {
                }
            }
            System.out.println(" ✓");

            if (todosMovimientos.isEmpty()) {
                System.out.println("No se encontraron movimientos en ninguna cuenta.");
                return;
            }
            
            todosMovimientos.sort((m1, m2) -> {
                int resFecha = m2.getFecha().toGregorianCalendar().compareTo(m1.getFecha().toGregorianCalendar());
                if (resFecha != 0) {
                    return resFecha;
                }
                return Integer.compare(m2.getNumero(), m1.getNumero());
            });

            String header = String.format("%-10s | %-4s | %-12s | %-25.25s | %12s | %12s",
                    "CUENTA", "NRO", "FECHA", "TIPO", "IMPORTE", "SALDO");
            System.out.println(header);
            System.out.println("-".repeat(header.length()));
            
            for (MovimientoData m : todosMovimientos) {
                String tipo = m.getTipo() != null ? m.getTipo() : "N/A"; 
                System.out.printf("%-10s | %-4d | %-12s | %-25.25s | %12s | %12s%n",
                        m.getCodigoCuenta(), 
                        m.getNumero(),
                        m.getFecha().toString().substring(0, 10),
                        tipo,
                        decimalFormat.format(m.getImporte()),
                        decimalFormat.format(m.getSaldoActual()));
            }

        } catch (Exception e) {
            System.out.println(" ✗");
            mostrarError("Error al cargar todos los movimientos: " + e.getMessage());
        }
    }
    
    private void mostrarMovimientosDeUnaCuenta(String cuenta) {
        try {
            System.out.print("Cargando movimientos de la cuenta " + cuenta + "...");
            List<MovimientoData> movimientos = euBankService.traerMovimientos(cuenta);
            System.out.println(" ✓");
            
            if (movimientos.isEmpty()) {
                System.out.println("No se encontraron movimientos para la cuenta " + cuenta);
            } else {
                
                String header = String.format("%-4s | %-12s | %-25.25s | %12s | %12s",
                        "NRO", "FECHA", "TIPO", "IMPORTE", "SALDO");
                System.out.println(header);
                System.out.println("-".repeat(header.length()));
                
                movimientos.sort((m1, m2) -> Integer.compare(m2.getNumero(), m1.getNumero()));
                
                for (MovimientoData m : movimientos) {
                    String tipo = m.getTipo() != null ? m.getTipo() : "N/A"; 
                    System.out.printf("%-4d | %-12s | %-25.25s | %12s | %12s%n",
                            m.getNumero(),
                            m.getFecha().toString().substring(0, 10),
                            tipo,
                            decimalFormat.format(m.getImporte()),
                            decimalFormat.format(m.getSaldoActual()));
                }
            }
        } catch (Exception e) {
            System.out.println(" ✗");
            mostrarError("Error al cargar movimientos: " + e.getMessage());
        }
    }

    private void registrarMovimiento(String tipo) {
        System.out.println("--- Registrar " + tipo + " ---");
        
        System.out.print("Número de Cuenta: ");
        String cuenta = scanner.nextLine().trim();
        System.out.print("Importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            if (importe <= 0) {
                 mostrarError("El importe debe ser positivo.");
                 return;
            }
        } catch (Exception e) {
            mostrarError("Importe no válido.");
            return;
        }

        try {
            System.out.print("Procesando " + tipo + "...");
            ResultadoOperacion res = euBankService.regMovimiento(tipo, cuenta, "", importe);
            System.out.println(" ✓");
            
            if (res.getCodigo() == 1) { 
                System.out.println("\n¡Operación exitosa!");
            } else {
                mostrarError(res.getMensaje());
            }
        } catch (Exception e) {
            System.out.println(" ✗");
            mostrarError("Error al registrar movimiento: " + e.getMessage());
        }
    }
    
    private void registrarTransferencia() {
        System.out.println("--- Registrar Transferencia ---");

        System.out.print("Número de Cuenta Origen: ");
        String cuentaOrigen = scanner.nextLine().trim();
        System.out.print("Número de Cuenta Destino: ");
        String cuentaDestino = scanner.nextLine().trim();
        System.out.print("Importe: ");
        double importe;
        try {
            importe = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
             if (importe <= 0) {
                 mostrarError("El importe debe ser positivo.");
                 return;
            }
        } catch (Exception e) {
            mostrarError("Importe no válido.");
            return;
        }
        
        if (cuentaOrigen.equals(cuentaDestino)) {
            mostrarError("Las cuentas de origen y destino no pueden ser la misma.");
            return;
        }

        try {
            System.out.print("Procesando Transferencia...");
            ResultadoOperacion res = euBankService.regMovimiento("TRANSFERENCIA", cuentaOrigen, cuentaDestino, importe);
            System.out.println(" ✓");
            
            if (res.getCodigo() == 1) {
                System.out.println("\n¡Operación exitosa!");
            } else {
                mostrarError(res.getMensaje());
            }
        } catch (Exception e) {
            System.out.println(" ✗");
            mostrarError("Error al registrar movimiento: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        System.out.print("\u001B[31m"); 
        System.out.println("\n✗ " + mensaje);
        System.out.print("\u001B[0m"); 
    }


}