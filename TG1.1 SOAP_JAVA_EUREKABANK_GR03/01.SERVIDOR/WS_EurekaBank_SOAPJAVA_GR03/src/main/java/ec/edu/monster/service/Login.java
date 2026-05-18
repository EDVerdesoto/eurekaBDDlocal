/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.service;

import ec.edu.monster.db.AccesoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author leona
 */
public class Login {

    public boolean IniciarSesion(String nombreUsuario, String contrasena) {
        Connection cn = null;
        boolean isValid = false;
        try {
            // Obtener la conexión
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // Consulta para validar nombreUsuario y contraseña
            String sql = "SELECT COUNT(*) AS conteo "
                    + "FROM Usuario "
                    + "WHERE vch_emplusuario = ? AND vch_emplclave = ?";
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, nombreUsuario);
            pstm.setString(2, contrasena);

            // Ejecutar la consulta
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                int conteo = rs.getInt("conteo");
                isValid = (conteo > 0); // Si hay registros, el nombreUsuario es válido
            }

            // Cerrar recursos
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar nombreUsuario: " + e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return isValid;
    }
}

