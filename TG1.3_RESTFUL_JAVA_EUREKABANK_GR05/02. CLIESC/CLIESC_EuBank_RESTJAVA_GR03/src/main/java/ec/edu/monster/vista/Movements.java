package ec.edu.monster.vista;

import ec.edu.monster.model.DatosCuenta;
import ec.edu.monster.model.MovimientoData;
import ec.edu.monster.servicios.EuBankService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dome
 */
public class Movements extends javax.swing.JPanel {

    private EuBankService euBankService;
    private final DecimalFormat decimalFormat;
    
    public Movements(EuBankService euBankService) {
        this.euBankService = euBankService;
        initComponents();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        this.decimalFormat = new DecimalFormat("#,##0.00", symbols);
        cargarDatosMovimientos();
    }
    // </editor-fold>
    
    public void setEuBankService(EuBankService euBankService) {
        this.euBankService = euBankService;
        // Ahora que tenemos el servicio, cargamos los datos
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        modernPanel2 = new ec.edu.monster.vista.ModernPanel();
        lblimagesulli = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblTitle1 = new javax.swing.JLabel();
        btn_crear = new ec.edu.monster.vista.ModernButton();
        tableMovements = new ec.edu.monster.vista.ModernTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(680, 570));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        modernPanel2.setBackground(new java.awt.Color(255, 251, 245));
        modernPanel2.setBorderColor(new java.awt.Color(255, 255, 255));
        modernPanel2.setPanelBackground(new java.awt.Color(255, 251, 245));

        lblimagesulli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/SulliGeneral.png"))); // NOI18N
        lblimagesulli.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblTitle.setFont(new java.awt.Font("Britannic Bold", 0, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 153, 0));
        lblTitle.setText("Movimientos Bancarios");

        lblTitle1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblTitle1.setForeground(new java.awt.Color(102, 102, 102));
        lblTitle1.setText("Revisa los movimientos realizados entre las cuentas bancarias");

        btn_crear.setBackgroundColor(new java.awt.Color(255, 153, 0));
        btn_crear.setBorder(null);
        btn_crear.setBorderColor(new java.awt.Color(255, 251, 245));
        btn_crear.setBorderThickness(0);
        btn_crear.setHoverColor(new java.awt.Color(178, 107, 0));
        btn_crear.setText("+ Crear");
        btn_crear.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btn_crear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_crearMouseClicked(evt);
            }
        });
        btn_crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_crearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout modernPanel2Layout = new javax.swing.GroupLayout(modernPanel2);
        modernPanel2.setLayout(modernPanel2Layout);
        modernPanel2Layout.setHorizontalGroup(
            modernPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modernPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblimagesulli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modernPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle1)
                    .addComponent(lblTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btn_crear, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        modernPanel2Layout.setVerticalGroup(
            modernPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblimagesulli, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(modernPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(modernPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_crear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(modernPanel2Layout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTitle1)))
                .addGap(16, 16, 16))
        );

        tableMovements.setBackground(new java.awt.Color(255, 255, 255));
        tableMovements.setBorder(null);
        tableMovements.setBorderRadius(0);
        tableMovements.setContainerBackground(new java.awt.Color(255, 255, 255));
        tableMovements.setEvenRowColor(new java.awt.Color(250, 244, 235));
        tableMovements.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        tableMovements.setHeaderBackground(new java.awt.Color(255, 153, 0));
        tableMovements.setHoverRowColor(new java.awt.Color(255, 204, 153));
        tableMovements.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tableMovements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modernPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(modernPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tableMovements, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 570));
    }// </editor-fold>//GEN-END:initComponents
    
    private void cargarDatosMovimientos() {
        if (this.euBankService == null) {
            System.err.println("Error: EuBankService no fue inyectado en el panel Movements.");
            return;
        }

        String[] columnNames = {"Cuenta", "Nro", "Fecha", "Tipo", "Importe", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try {
            // Lógica para traer TODOS los movimientos (igual que en consola)
            List<DatosCuenta> cuentas = this.euBankService.traerCuentasConClientes();
            List<MovimientoData> todosMovimientos = new ArrayList<>();
            for (DatosCuenta cuenta : cuentas) {
                todosMovimientos.addAll(this.euBankService.traerMovimientos(cuenta.getCodigo()));
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['Z']");
            todosMovimientos.sort((m1, m2) -> {
                java.time.LocalDate fecha1 = null, fecha2 = null;
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
            // Llena la tabla
            for (MovimientoData mov : todosMovimientos) {
                Object[] row = new Object[] {
                    mov.getCodigoCuenta(),
                    mov.getNumero(),
                    mov.getFecha().toString().substring(0, 10), // Acorta la fecha
                    mov.getTipo(),
                    decimalFormat.format(mov.getImporte()),
                    decimalFormat.format(mov.getSaldoActual())
                };
                model.addRow(row);
            }
            
            tableMovements.getTable().setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar movimientos: " + ex.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btn_crearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_crearMouseClicked

    }//GEN-LAST:event_btn_crearMouseClicked

    private void btn_crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_crearActionPerformed
        // 1. Obtener la ventana padre (el JFrame que contiene este JPanel)
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);

        // 2. Crear el JDialog modal, pasándole el padre y 'true'
        //    ¡Aquí usamos el nuevo constructor!
        Modal modal = new Modal((java.awt.Frame) parentWindow, true, this.euBankService);

        // 3. Mostrar el modal. El código se PAUSARÁ aquí
        //    hasta que el usuario cierre el diálogo.
        modal.setVisible(true);

        // 4. El código se reanuda AQUÍ cuando el modal se cierra.
        String tipoMovimiento = modal.getSeleccion();

        // 5. Verificar si el usuario presionó "Crear" (tipoMovimiento no será null)
        if (tipoMovimiento != null) {
            // El usuario NO canceló
            String origen = modal.getCuentaOrigen();
            String destino = modal.getCuentaDestino();

            // Imprimir en consola (para probar)
            System.out.println("--- Nuevo Movimiento ---");
            System.out.println("Tipo: " + tipoMovimiento);
            System.out.println("Origen: " + origen);
            System.out.println("Destino: " + destino);

            // TODO: Aquí es donde agregas los datos a tu 'tableMovements'
            // Por ejemplo:
            // javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tableMovements.getModel();
            // model.addRow(new Object[]{"ID_NUEVO", tipoMovimiento, origen, destino});

        } else {
            // El usuario presionó "Cancelar"
            System.out.println("Operación cancelada.");
        }
    }//GEN-LAST:event_btn_crearActionPerformed
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ec.edu.monster.vista.ModernButton btn_crear;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTitle1;
    private javax.swing.JLabel lblimagesulli;
    private ec.edu.monster.vista.ModernPanel modernPanel2;
    private ec.edu.monster.vista.ModernTable tableMovements;
    // End of variables declaration//GEN-END:variables
}
