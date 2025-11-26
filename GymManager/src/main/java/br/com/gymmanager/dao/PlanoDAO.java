/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joaoreis699
 */

package br.com.gymmanager.dao;

import br.com.gymmanager.model.Plano;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PlanoDAO {

    public List<Plano> listarTodos() {
        String sql = "SELECT * FROM tb_plano";
        List<Plano> planos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Plano p = new Plano();
                p.setId(rset.getInt("id_plano"));
                p.setNome(rset.getString("nome"));
                p.setValor(rset.getDouble("valor"));
                
                planos.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar planos: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return planos;
    }    
}
