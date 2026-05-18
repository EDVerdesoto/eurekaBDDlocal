package ec.edu.monster.vista;

import ec.edu.monster.servicios.EuBankService;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Dome
 */
public class login extends javax.swing.JFrame {

    int xMouse, yMouse;
    private final EuBankService euBankService;
    
    public login(EuBankService euBankService) {
        this.euBankService = euBankService;
        initComponents();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        btnEntrar = new javax.swing.JPanel();
        lblButtonIniciar = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        btnExit = new javax.swing.JPanel();
        lblExit = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSullivan1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        tbx_user = new javax.swing.JTextField();
        separatorUser = new javax.swing.JSeparator();
        separatorPass = new javax.swing.JSeparator();
        jpfpassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        logoeubank = new javax.swing.JLabel();
        lblTarea = new javax.swing.JLabel();
        lblSullivan = new javax.swing.JLabel();
        lblTarea1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(800, 500));

        Background.setBackground(new java.awt.Color(255, 255, 255));
        Background.setToolTipText("");
        Background.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEntrar.setBackground(new java.awt.Color(255, 153, 0));
        btnEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntrar.setName("pnliniciarsesion"); // NOI18N
        btnEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntrarMouseEntered(evt);
            }
        });

        lblButtonIniciar.setFont(new java.awt.Font("Roboto Light", 1, 18)); // NOI18N
        lblButtonIniciar.setForeground(new java.awt.Color(255, 255, 255));
        lblButtonIniciar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblButtonIniciar.setText("Iniciar Sesión");
        lblButtonIniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblButtonIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblButtonIniciarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblButtonIniciarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblButtonIniciarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnEntrarLayout = new javax.swing.GroupLayout(btnEntrar);
        btnEntrar.setLayout(btnEntrarLayout);
        btnEntrarLayout.setHorizontalGroup(
            btnEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblButtonIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        btnEntrarLayout.setVerticalGroup(
            btnEntrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblButtonIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        lblButtonIniciar.getAccessibleContext().setAccessibleName("lblIniciarSesion");

        Background.add(btnEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 360, 190, 50));

        header.setBackground(new java.awt.Color(255, 153, 0));
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

        btnExit.setBackground(new java.awt.Color(255, 153, 0));
        btnExit.setForeground(java.awt.Color.white);

        lblExit.setFont(new java.awt.Font("Roboto Light", 1, 24)); // NOI18N
        lblExit.setForeground(java.awt.Color.white);
        lblExit.setText("  X  ");
        lblExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblExitMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblExitMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnExitLayout = new javax.swing.GroupLayout(btnExit);
        btnExit.setLayout(btnExitLayout);
        btnExitLayout.setHorizontalGroup(
            btnExitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnExitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnExitLayout.setVerticalGroup(
            btnExitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnExitLayout.createSequentialGroup()
                .addComponent(lblExit)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        lblExit.getAccessibleContext().setAccessibleName("btnExit");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/favicon.png"))); // NOI18N

        lblTitle.setFont(new java.awt.Font("Roboto Medium", 0, 16)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("INICIAR SESIÓN");

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 619, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Background.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 40));

        lblSullivan1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSullivan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sulliLogo.png"))); // NOI18N
        lblSullivan1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        Background.add(lblSullivan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 70, 430, 520));

        jPanel1.setBackground(new java.awt.Color(255, 247, 235));

        lblUser.setFont(new java.awt.Font("Roboto", 0, 22)); // NOI18N
        lblUser.setText("Usuario");

        tbx_user.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        tbx_user.setForeground(new java.awt.Color(102, 102, 102));
        tbx_user.setBorder(null);

        separatorUser.setForeground(new java.awt.Color(51, 51, 51));

        separatorPass.setForeground(new java.awt.Color(51, 51, 51));

        jpfpassword.setFont(new java.awt.Font("Roboto Light", 0, 18)); // NOI18N
        jpfpassword.setForeground(new java.awt.Color(102, 102, 102));
        jpfpassword.setToolTipText("");
        jpfpassword.setBorder(null);

        lblPassword.setFont(new java.awt.Font("Roboto", 0, 22)); // NOI18N
        lblPassword.setText("Contraseña");

        logoeubank.setFont(new java.awt.Font("Roboto", 0, 22)); // NOI18N
        logoeubank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EB-SOAP JAVA2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separatorPass, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword)
                    .addComponent(jpfpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUser)
                    .addComponent(separatorUser, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tbx_user, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(logoeubank, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(logoeubank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbx_user, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorUser, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpfpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorPass, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );

        Background.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 450, 380));

        lblTarea.setBackground(new java.awt.Color(255, 255, 255));
        lblTarea.setFont(new java.awt.Font("Britannic Bold", 0, 16)); // NOI18N
        lblTarea.setForeground(new java.awt.Color(255, 153, 0));
        lblTarea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTarea.setText("<html>Edison Verdesoto<br>Joan Cobeña<br>Juan Pasquel</html>");
        lblTarea.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Background.add(lblTarea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 170, 60));

        lblSullivan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/java.png"))); // NOI18N
        Background.add(lblSullivan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 50, 50));

        lblTarea1.setBackground(new java.awt.Color(255, 255, 255));
        lblTarea1.setFont(new java.awt.Font("Britannic Bold", 1, 22)); // NOI18N
        lblTarea1.setForeground(new java.awt.Color(255, 153, 102));
        lblTarea1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTarea1.setText("<html>+ REST</html>");
        lblTarea1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Background.add(lblTarea1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 110, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Background, javax.swing.GroupLayout.PREFERRED_SIZE, 840, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
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

    private void lblExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lblExitMouseClicked

    private void lblExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseEntered
        btnExit.setBackground(Color.red);
    }//GEN-LAST:event_lblExitMouseEntered

    private void lblExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblExitMouseExited
        btnExit.setBackground(new Color(255,153,0));
        
    }//GEN-LAST:event_lblExitMouseExited

    private void btnEntrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEntrarMouseEntered
        
    }//GEN-LAST:event_btnEntrarMouseEntered

    private void lblButtonIniciarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblButtonIniciarMouseEntered
        btnEntrar.setBackground(new Color(178, 107, 0));
    }//GEN-LAST:event_lblButtonIniciarMouseEntered

    private void lblButtonIniciarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblButtonIniciarMouseExited
        btnEntrar.setBackground(new Color(255,153,0));
    }//GEN-LAST:event_lblButtonIniciarMouseExited

    private void lblButtonIniciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblButtonIniciarMouseClicked
        String usuario = tbx_user.getText();
        String clave = new String(jpfpassword.getPassword());

        try {
            if (this.euBankService.iniciarSesion(usuario, clave).getCodigo() == 1) {
                
                // ¡PASA EL SERVICIO A LA SIGUIENTE VENTANA!
                Home home = new Home(euBankService);
                home.setVisible(true);
                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Usuario o clave incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_lblButtonIniciarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JPanel btnEntrar;
    private javax.swing.JPanel btnExit;
    private javax.swing.JPanel header;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jpfpassword;
    private javax.swing.JLabel lblButtonIniciar;
    private javax.swing.JLabel lblExit;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSullivan;
    private javax.swing.JLabel lblSullivan1;
    private javax.swing.JLabel lblTarea;
    private javax.swing.JLabel lblTarea1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel logoeubank;
    private javax.swing.JSeparator separatorPass;
    private javax.swing.JSeparator separatorUser;
    private javax.swing.JTextField tbx_user;
    // End of variables declaration//GEN-END:variables
}
