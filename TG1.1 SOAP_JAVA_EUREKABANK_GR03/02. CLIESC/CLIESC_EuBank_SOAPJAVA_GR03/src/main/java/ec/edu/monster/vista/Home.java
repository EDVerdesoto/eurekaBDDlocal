package ec.edu.monster.vista;

import ec.edu.monster.servicios.EuBankService;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 *
 * @author Dome
 */
public class Home extends javax.swing.JFrame {
    private final EuBankService euBankService = new EuBankService();
    int xMouse, yMouse;
    public Home() {
        initComponents();
        
        Clients c = new Clients();
        c.setSize(680, 570);
        c.setLocation(0, 0);
        this.setLocationRelativeTo(null);
        
        Contenedor.removeAll();
        Contenedor.add(c, BorderLayout.CENTER);
        Contenedor.revalidate();
        Contenedor.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        btn_exit = new ec.edu.monster.vista.ModernButton();
        Menu = new javax.swing.JPanel();
        lblLogoEu = new javax.swing.JLabel();
        btn_clients = new ec.edu.monster.vista.ModernButton();
        btn_movements = new ec.edu.monster.vista.ModernButton();
        btn_exitsession = new ec.edu.monster.vista.ModernButton();
        Contenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(850, 640));

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setToolTipText("");
        Background.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(153, 214, 234));
        header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                headerMouseDragged(evt);
            }
        });
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerMousePressed(evt);
            }
        });

        btn_exit.setBackgroundColor(new java.awt.Color(153, 214, 234));
        btn_exit.setBorder(null);
        btn_exit.setBorderColor(new java.awt.Color(153, 214, 234));
        btn_exit.setBorderRadius(0);
        btn_exit.setBorderThickness(0);
        btn_exit.setHoverColor(new java.awt.Color(255, 51, 51));
        btn_exit.setPressedColor(new java.awt.Color(153, 0, 0));
        btn_exit.setText("X");
        btn_exit.setFont(new java.awt.Font("Roboto", 1, 20)); // NOI18N
        btn_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_exitMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(0, 825, Short.MAX_VALUE)
                .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_exit, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        Background.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 30));

        Menu.setBackground(new java.awt.Color(242, 252, 255));

        lblLogoEu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EuBank2.png"))); // NOI18N

        btn_clients.setBackgroundColor(new java.awt.Color(153, 214, 234));
        btn_clients.setBorder(null);
        btn_clients.setBorderColor(new java.awt.Color(153, 214, 234));
        btn_clients.setBorderRadius(0);
        btn_clients.setBorderThickness(0);
        btn_clients.setHoverColor(new java.awt.Color(87, 175, 204));
        btn_clients.setText("Cuentas de Clientes");
        btn_clients.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        btn_clients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clientsActionPerformed(evt);
            }
        });

        btn_movements.setBackgroundColor(new java.awt.Color(153, 214, 234));
        btn_movements.setBorder(null);
        btn_movements.setBorderColor(new java.awt.Color(153, 214, 234));
        btn_movements.setBorderRadius(0);
        btn_movements.setBorderThickness(0);
        btn_movements.setHoverColor(new java.awt.Color(87, 175, 204));
        btn_movements.setText("Movimientos");
        btn_movements.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        btn_movements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_movementsActionPerformed(evt);
            }
        });

        btn_exitsession.setBackgroundColor(new java.awt.Color(153, 214, 234));
        btn_exitsession.setBorder(null);
        btn_exitsession.setBorderColor(new java.awt.Color(153, 214, 234));
        btn_exitsession.setBorderRadius(0);
        btn_exitsession.setBorderThickness(0);
        btn_exitsession.setHoverColor(new java.awt.Color(87, 175, 204));
        btn_exitsession.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ingresar.png"))); // NOI18N
        btn_exitsession.setText("Cerrar Sesión");
        btn_exitsession.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        btn_exitsession.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_exitsession.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_exitsessionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_clients, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_movements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_exitsession, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogoEu)
                .addContainerGap())
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblLogoEu, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btn_clients, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_movements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                .addComponent(btn_exitsession, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        Background.add(Menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 190, 570));

        Contenedor.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout ContenedorLayout = new javax.swing.GroupLayout(Contenedor);
        Contenedor.setLayout(ContenedorLayout);
        ContenedorLayout.setHorizontalGroup(
            ContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );
        ContenedorLayout.setVerticalGroup(
            ContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        Background.add(Contenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 680, 570));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void headerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
        
    }//GEN-LAST:event_headerMousePressed

    private void headerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_headerMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_headerMouseDragged

    private void btn_exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_exitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btn_exitMouseClicked

    private void btn_exitsessionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_exitsessionMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btn_exitsessionMouseClicked

    private void btn_clientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clientsActionPerformed
        Clients c = new Clients();
        c.setSize(680, 570);
        c.setLocation(0, 0);
        
        Contenedor.removeAll();
        Contenedor.add(c, BorderLayout.CENTER);
        Contenedor.revalidate();
        Contenedor.repaint();
    }//GEN-LAST:event_btn_clientsActionPerformed

    private void btn_movementsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_movementsActionPerformed
        Movements c = new Movements();
        c.setSize(680, 570);
        c.setLocation(0, 0);
        
        Contenedor.removeAll();
        Contenedor.add(c, BorderLayout.CENTER);
        Contenedor.revalidate();
        Contenedor.repaint();
    }//GEN-LAST:event_btn_movementsActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JPanel Contenedor;
    private javax.swing.JPanel Menu;
    private ec.edu.monster.vista.ModernButton btn_clients;
    private ec.edu.monster.vista.ModernButton btn_exit;
    private ec.edu.monster.vista.ModernButton btn_exitsession;
    private ec.edu.monster.vista.ModernButton btn_movements;
    private javax.swing.JPanel header;
    private javax.swing.JLabel lblLogoEu;
    // End of variables declaration//GEN-END:variables
}
