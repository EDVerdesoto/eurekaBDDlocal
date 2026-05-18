package ec.edu.monster.views;

import ec.edu.monster.model.DatosCuenta;
import ec.edu.monster.model.MovimientoData;
import ec.edu.monster.model.ResultadoOperacion;
import ec.edu.monster.servicios.EuBankService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DashboardVista {

    private final EuBankService euBankService;
    private final Scanner scanner;
    private final DecimalFormat decimalFormat;

    public static class MovimientoRequest {
        public String tipo;
        public String cuentaOrigen;
        public String cuentaDestino;
        public double importe;
    }
    
    public DashboardVista(EuBankService euBankService) {
        this.euBankService = euBankService;
        this.scanner = new Scanner(System.in, "UTF-8");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        this.decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }

    public void mostrarMenuPrincipal() {
        while (true) {
    
            dibujarMarco("  Menu Principal - EurekaBank  ");
            System.out.println();
            System.out.println("  [1] Ver Cuentas de Clientes");
            System.out.println("  [2] Ver Movimientos");
            System.out.println("  [3] Registrar Deposito");
            System.out.println("  [4] Registrar Retiro");
            System.out.println("  [5] Registrar Transferencia");
            System.out.println("  [6] Salir");
            System.out.println();
            System.out.print("  Seleccione una opcion: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim()); 
            } catch (Exception e) {
                mostrarError("Opcion no valida. Debe ser un numero.");
        
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
                    System.out.println("\n  Gracias por usar EurekaBank. Hasta pronto!");
                    return;
                default:
                    mostrarError("Opcion no valida. Seleccione de 1 a 6.");
            
                    break;
            }
        }
    }

    private void mostrarCuentasClientes() {

        dibujarMarco("  Cuentas de Clientes  ");
        try {
            System.out.print("  Cargando cuentas...");
            List<DatosCuenta> cuentas = euBankService.traerCuentasConClientes();
            System.out.println(" [OK]");
            System.out.println();
            
            if (cuentas.isEmpty()) {
                System.out.println("  No se encontraron cuentas.");
            } else {
                System.out.println(repiteChar('-', 78));
                System.out.printf("  %-10s | %-28s | %-8s | %-8s | %12s%n",
                        "CUENTA", "CLIENTE", "MONEDA", "ESTADO", "SALDO");
                System.out.println(repiteChar('-', 78));
                
                for (DatosCuenta c : cuentas) {
                    System.out.printf("  %-10s | %-28s | %-8s | %-8s | %12s%n",
                            c.getCodigo(),
                            truncarTexto(c.getNombreCliente(), 28),
                            c.getMoneda(),
                            c.getEstado(),
                            decimalFormat.format(c.getSaldo()));
                }
                System.out.println(repiteChar('-', 78));
            }
        } catch (Exception e) {
            mostrarError("Error al cargar cuentas: " + e.getMessage());
        }

    }

    private void mostrarMovimientos() {

        dibujarMarco("  Movimientos  ");
        System.out.print("  Ingrese numero de cuenta o Enter para ver todos: ");
        String cuenta = scanner.nextLine().trim();

        if (cuenta.isEmpty()) {
            mostrarTodosLosMovimientos();
        } else {
            mostrarMovimientosDeUnaCuenta(cuenta);
        }

    }

    private void mostrarTodosLosMovimientos() {
        try {
            System.out.print("  Cargando cuentas...");
            List<DatosCuenta> cuentas = euBankService.traerCuentasConClientes();
            System.out.println(" [OK]");
            List<MovimientoData> todosMovimientos = new ArrayList<>();
            
            System.out.print("  Cargando movimientos...");
            for (DatosCuenta cuenta : cuentas) {
                try {
                    todosMovimientos.addAll(euBankService.traerMovimientos(cuenta.getCodigo()));
                } catch (Exception ex) {
                }
            }
            System.out.println(" [OK]");
            System.out.println();

            if (todosMovimientos.isEmpty()) {
                System.out.println("  No se encontraron movimientos.");
                return;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['Z']");
            todosMovimientos.sort((m1, m2) -> {
                java.time.LocalDate fecha1 = null;
                java.time.LocalDate fecha2 = null;
                try {
                    if (m1.getFecha() != null && !m1.getFecha().isEmpty()) 
                        fecha1 = java.time.LocalDate.parse(m1.getFecha(), formatter);
                } catch (DateTimeParseException e) {}
                try {
                    if (m2.getFecha() != null && !m2.getFecha().isEmpty()) 
                        fecha2 = java.time.LocalDate.parse(m2.getFecha(), formatter);
                } catch (DateTimeParseException e) {}

                if (fecha1 != null && fecha2 != null) {
                    int resFecha = fecha2.compareTo(fecha1);
                    if (resFecha != 0) return resFecha;
                    return Integer.compare(m2.getNumero(), m1.getNumero());
                }
                else if (fecha1 == null && fecha2 != null) return 1;
                else if (fecha1 != null && fecha2 == null) return -1;
                else return Integer.compare(m2.getNumero(), m1.getNumero());
            });

            System.out.println(repiteChar('-', 95));
            System.out.printf("  %-10s | %-4s | %-17s | %-25s | %12s | %12s%n",
                    "CUENTA", "NRO", "FECHA", "TIPO", "IMPORTE", "SALDO");
            System.out.println(repiteChar('-', 95));
            
            for (MovimientoData m : todosMovimientos) {
                String tipo = m.getTipo() != null ? m.getTipo() : "N/A"; 
                System.out.printf("  %-10s | %-4d | %-17s | %-25s | %12s | %12s%n",
                        m.getCodigoCuenta(), 
                        m.getNumero(),
                        m.getFecha(), 
                        truncarTexto(tipo, 25),
                        decimalFormat.format(m.getImporte()),
                        decimalFormat.format(m.getSaldoActual()));
            }
            System.out.println(repiteChar('-', 95));

        } catch (Exception e) {
            System.out.println(" [ERROR]");
            mostrarError("Error al cargar movimientos: " + e.getMessage());
        }
    }
    
    private void mostrarMovimientosDeUnaCuenta(String cuenta) {
        try {
            System.out.print("  Cargando movimientos de cuenta " + cuenta + "...");
            List<MovimientoData> movimientos = euBankService.traerMovimientos(cuenta);
            System.out.println(" [OK]");
            System.out.println();
            
            if (movimientos.isEmpty()) {
                System.out.println("  No se encontraron movimientos para la cuenta " + cuenta);
            } else {
                System.out.println(repiteChar('-', 82));
                System.out.printf("  %-4s | %-17s | %-25s | %12s | %12s%n",
                        "NRO", "FECHA", "TIPO", "IMPORTE", "SALDO");
                System.out.println(repiteChar('-', 82));
                
                for (MovimientoData m : movimientos) {
                    String tipo = m.getTipo() != null ? m.getTipo() : "N/A"; 
                    System.out.printf("  %-4d | %-17s | %-25s | %12s | %12s%n",
                            m.getNumero(),
                            m.getFecha(), 
                            truncarTexto(tipo, 25),
                            decimalFormat.format(m.getImporte()),
                            decimalFormat.format(m.getSaldoActual()));
                }
                System.out.println(repiteChar('-', 82));
            }
        } catch (Exception e) {
            System.out.println(" [ERROR]");
            mostrarError("Error al cargar movimientos: " + e.getMessage());
        }
    }

    private void registrarMovimiento(String tipo) {

        dibujarMarco("  Registrar " + tipo + "  ");
        
        System.out.print("  Numero de Cuenta: ");
        String cuenta = scanner.nextLine().trim();
        System.out.print("  Importe: ");
        double importe = 0.0;
        try {
            importe = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            if (importe <= 0) {
                 mostrarError("El importe debe ser positivo.");
         
                 return;
            }
        } catch (Exception e) {
            mostrarError("Importe no valido.");
    
            return;
        }

        try {
            System.out.print("\n  Procesando " + tipo.toLowerCase() + "...");
            
            MovimientoRequest request = new MovimientoRequest();
            request.tipo = tipo;
            request.cuentaOrigen = cuenta;
            request.cuentaDestino = "";
            request.importe = importe;
            
            ResultadoOperacion res = euBankService.regMovimiento(request);
            System.out.println(" [OK]");
            
            if (res.getCodigo() == 1) { 
                System.out.println("\n  Operacion exitosa!");
                if(res.getMensaje() != null) System.out.println("  " + res.getMensaje());
            } else {
                mostrarError(res.getMensaje());
            }
        } catch (HttpClientErrorException e) { 
            System.out.println(" [ERROR]");
            mostrarError("Error del servicio: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println(" [ERROR]");
            mostrarError("Error al registrar movimiento: " + e.getMessage());
        }

    }
    
    private void registrarTransferencia() {

        dibujarMarco("  Registrar Transferencia  ");

        System.out.print("  Cuenta Origen: ");
        String cuentaOrigen = scanner.nextLine().trim();
        System.out.print("  Cuenta Destino: ");
        String cuentaDestino = scanner.nextLine().trim();
        System.out.print("  Importe: ");
        double importe = 0.0;
        try {
            importe = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
             if (importe <= 0) {
                 mostrarError("El importe debe ser positivo.");
         
                 return;
            }
        } catch (Exception e) {
            mostrarError("Importe no valido.");
    
            return;
        }
        
        if (cuentaOrigen.equals(cuentaDestino)) {
            mostrarError("Las cuentas origen y destino no pueden ser la misma.");
    
            return;
        }

        try {
            System.out.print("\n  Procesando transferencia...");
            
            MovimientoRequest request = new MovimientoRequest();
            request.tipo = "TRANSFERENCIA";
            request.cuentaOrigen = cuentaOrigen;
            request.cuentaDestino = cuentaDestino;
            request.importe = importe;
            
            ResultadoOperacion res = euBankService.regMovimiento(request);
            System.out.println(" [OK]");
            
            if (res.getCodigo() == 1) {
                System.out.println("\n  Transferencia exitosa!");
            } else {
                mostrarError(res.getMensaje());
            }
        } catch (HttpClientErrorException e) { 
            System.out.println(" [ERROR]");
            mostrarError("Error del servicio: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println(" [ERROR]");
            mostrarError("Error al registrar transferencia: " + e.getMessage());
        }

    }

    private void mostrarError(String mensaje) {
        System.out.println("\n  [ERROR] " + mensaje);
    }

    private void esperarTecla() {
        System.out.println("\n  Presione Enter para continuar...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
        }
    }

    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void dibujarMarco(String titulo) {
        int ancho = 50;
        System.out.println();
        System.out.println("  " + repiteChar('=', ancho));
        System.out.println("  " + centrarTexto(titulo, ancho));
        System.out.println("  " + repiteChar('=', ancho));
        System.out.println();
    }

    private String repiteChar(char c, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int espacios = ancho - texto.length();
        int izq = espacios / 2;
        int der = espacios - izq;
        return repiteChar(' ', izq) + texto + repiteChar(' ', der);
    }

    private String truncarTexto(String texto, int maximo) {
        if (texto == null) return "";
        if (texto.length() <= maximo) return texto;
        return texto.substring(0, maximo - 3) + "...";
    }
}
