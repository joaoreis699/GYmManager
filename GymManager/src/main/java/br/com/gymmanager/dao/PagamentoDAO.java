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

    /**
     * GERA UMA NOVA COBRANÇA (MENSALIDADE).
     * Usado automaticamente quando um aluno é cadastrado.
     */
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
            pstm.setString(4, "Pendente"); // Sempre nasce pendente
            pstm.setString(5, null); // Forma de pagamento ainda não existe

            pstm.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao gerar mensalidade: " + e.getMessage());
            return false;
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    /**
     * LISTA PAGAMENTOS PENDENTES (Para a tela de Caixa).
     * Faz JOIN com Aluno para mostrar o nome de quem deve.
     */
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
                
                // Reconstrói o Aluno (básico) para saber quem é
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

    /**
     * BAIXA O PAGAMENTO (Receber dinheiro).
     * Atualiza o status para 'Pago' e define a data de hoje.
     */
    public boolean baixarPagamento(int idPagamento, String formaPagamento) {
        // CURDATE() é uma função do MySQL que pega a data atual
        String sql = "UPDATE tb_pagamento SET status = 'Pago', data_pagamento = CURDATE(), forma_pagamento = ? " +
                     "WHERE id_pagamento = ?";
        
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, formaPagamento); // Dinheiro, Pix, etc.
            pstm.setInt(2, idPagamento);

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao baixar pagamento: " + e.getMessage());
            return false;
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    /**
     * CONTA QUANTAS PENDÊNCIAS EXISTEM (Para o Dashboard).
     */
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
    
    // Helper
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
