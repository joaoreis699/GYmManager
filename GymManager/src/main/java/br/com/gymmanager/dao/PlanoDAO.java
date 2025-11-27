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

    public boolean cadastrar(Plano plano) {
        String sql = "INSERT INTO tb_plano (nome, valor, duracao_meses) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, plano.getNome());
            pstm.setDouble(2, plano.getValor());
            pstm.setInt(3, plano.getDuracaoMeses()); // Novo
            pstm.execute();
            return true;
        } catch (SQLException e) { /* ... */ return false; } finally { fecharConexao(conn, pstm, null); }
    }

    public boolean atualizar(Plano plano) {
        String sql = "UPDATE tb_plano SET nome = ?, valor = ?, duracao_meses = ? WHERE id_plano = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, plano.getNome());
            pstm.setDouble(2, plano.getValor());
            pstm.setInt(3, plano.getDuracaoMeses()); // Novo
            pstm.setInt(4, plano.getId());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) { /* ... */ return false; } finally { fecharConexao(conn, pstm, null); }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM tb_plano WHERE id_plano = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            pstm.execute();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                JOptionPane.showMessageDialog(null, "Não é possível remover este plano pois existem alunos matriculados nele.\nAltere os alunos para outro plano antes de remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao remover plano: " + e.getMessage());
            }
            return false;
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    public List<Plano> listarTodos() {
        String sql = "SELECT id_plano, nome, valor, duracao_meses FROM tb_plano";
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
                p.setDuracaoMeses(rset.getInt("duracao_meses")); // Novo setter
                planos.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); } finally { fecharConexao(conn, pstm, rset); }
        return planos;
    }
    
    private void fecharConexao(Connection conn, PreparedStatement pstm, ResultSet rset) {
        try {
            if (rset != null) rset.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}