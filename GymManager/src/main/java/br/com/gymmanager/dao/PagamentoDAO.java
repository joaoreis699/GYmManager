/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.dao;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Pagamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PagamentoDAO {

    public boolean gerarMensalidade(Pagamento pag) {
        String sql = "INSERT INTO tb_pagamento (id_aluno, valor, data_vencimento, status, forma_pagamento) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, pag.getAluno().getId());
            pstm.setDouble(2, pag.getValor());
            pstm.setString(3, pag.getDataVencimento());
            pstm.setString(4, "Pendente");
            pstm.setString(5, null);

            pstm.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao gerar mensalidade: " + e.getMessage());
            return false;
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    public List<Pagamento> listarPendentes() {
        String sql = "SELECT p.*, a.nome AS nome_aluno, a.cpf AS cpf_aluno " +
                     "FROM tb_pagamento p " +
                     "INNER JOIN tb_aluno a ON p.id_aluno = a.id_aluno " +
                     "WHERE p.status = 'Pendente'";

        List<Pagamento> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Pagamento p = new Pagamento();
                p.setId(rset.getInt("id_pagamento"));
                p.setValor(rset.getDouble("valor"));
                p.setDataVencimento(rset.getString("data_vencimento"));
                p.setStatus(rset.getString("status"));
                
                Aluno a = new Aluno();
                a.setId(rset.getInt("id_aluno"));
                a.setNome(rset.getString("nome_aluno"));
                a.setCpf(rset.getString("cpf_aluno"));
                
                p.setAluno(a);
                lista.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar pendências: " + e.getMessage());
        } finally {
            fecharConexao(conn, pstm, rset);
        }
        return lista;
    }

    public boolean baixarPagamento(int idPagamento, String formaPagamento) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            conn = ConexaoDAO.getConexao();
            conn.setAutoCommit(false); 

            String sqlBaixa = "UPDATE tb_pagamento SET status = 'Pago', data_pagamento = CURDATE(), forma_pagamento = ? WHERE id_pagamento = ?";
            pstm = conn.prepareStatement(sqlBaixa);
            pstm.setString(1, formaPagamento);
            pstm.setInt(2, idPagamento);
            pstm.executeUpdate();

            String sqlBusca = "SELECT pmt.id_aluno, pmt.valor, pmt.data_vencimento, pln.duracao_meses " +
                              "FROM tb_pagamento pmt " +
                              "JOIN tb_aluno a ON pmt.id_aluno = a.id_aluno " +
                              "JOIN tb_plano pln ON a.id_plano = pln.id_plano " +
                              "WHERE pmt.id_pagamento = ?";
            
            pstm = conn.prepareStatement(sqlBusca);
            pstm.setInt(1, idPagamento);
            rs = pstm.executeQuery();

            if (rs.next()) {
                int idAluno = rs.getInt("id_aluno");
                double valor = rs.getDouble("valor");
                String vencimentoAtualStr = rs.getString("data_vencimento"); 
                int duracaoMeses = rs.getInt("duracao_meses"); // <-- NOVO: Duração do Plano

                java.time.LocalDate dataAtual = java.time.LocalDate.parse(vencimentoAtualStr);
                java.time.LocalDate proximoVencimento = dataAtual.plusMonths(duracaoMeses);
                String novoVencimento = proximoVencimento.toString(); 

                String sqlNovo = "INSERT INTO tb_pagamento (id_aluno, valor, data_vencimento, status) VALUES (?, ?, ?, 'Pendente')";
                pstm = conn.prepareStatement(sqlNovo);
                pstm.setInt(1, idAluno);
                pstm.setDouble(2, valor);
                pstm.setString(3, novoVencimento);
                pstm.executeUpdate();
            }

            conn.commit(); 
            return true;

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            fecharConexao(conn, pstm, rs);
        }
    }

    public int contarPendencias() {
        String sql = "SELECT COUNT(*) AS total FROM tb_pagamento WHERE status = 'Pendente'";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        int total = 0;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            if (rset.next()) {
                total = rset.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            fecharConexao(conn, pstm, rset);
        }
        return total;
    }
    
    private void fecharConexao(Connection conn, PreparedStatement pstm, ResultSet rset) {
        try {
            if (rset != null) rset.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}