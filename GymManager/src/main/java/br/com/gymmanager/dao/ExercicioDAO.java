/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.dao;

/**
 *
 * @author joaoreis699
 * 
*/

import br.com.gymmanager.model.Exercicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExercicioDAO {

    public List<Exercicio> listarTodos() {
        String sql = "SELECT * FROM tb_exercicio ORDER BY grupo_muscular, nome";
        List<Exercicio> lista = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Exercicio e = new Exercicio();
                e.setId(rset.getInt("id_exercicio"));
                e.setNome(rset.getString("nome"));
                e.setGrupoMuscular(rset.getString("grupo_muscular"));
               
                
                lista.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
        return lista;
    }
    
    public boolean cadastrar(Exercicio ex) {
        String sql = "INSERT INTO tb_exercicio (nome, grupo_muscular) VALUES (?, ?)";
        try (Connection conn = ConexaoDAO.getConexao();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            
            pstm.setString(1, ex.getNome());
            pstm.setString(2, ex.getGrupoMuscular());
            
            pstm.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}