/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.dao;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.model.FichaTreino;
import br.com.gymmanager.model.ItemFicha;
import br.com.gymmanager.util.Sessao;
import java.sql.*;

public class TreinoDAO {

    public boolean salvarFichaCompleta(FichaTreino ficha) {
        String sqlFicha = "INSERT INTO tb_ficha (id_aluno, id_instrutor, objetivo, data_criacao, status) VALUES (?, ?, ?, CURDATE(), 'Ativa')";
        
        String sqlItem = "INSERT INTO tb_item_ficha (id_ficha, id_exercicio, series, repeticoes, tempo, divisao) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmFicha = null;
        PreparedStatement pstmItem = null;

        try {
            conn = ConexaoDAO.getConexao();
            
            conn.setAutoCommit(false);

            pstmFicha = conn.prepareStatement(sqlFicha, Statement.RETURN_GENERATED_KEYS);
            pstmFicha.setInt(1, ficha.getAluno().getId());
            
            if (Sessao.getInstance().getFuncionarioLogado() != null) {
                pstmFicha.setInt(2, Sessao.getInstance().getFuncionarioLogado().getId());
            } else {
                pstmFicha.setNull(2, java.sql.Types.INTEGER);
            }
            
            pstmFicha.setString(3, ficha.getObjetivo());
            pstmFicha.executeUpdate();

            ResultSet rsKeys = pstmFicha.getGeneratedKeys();
            int idFichaGerada = 0;
            if (rsKeys.next()) {
                idFichaGerada = rsKeys.getInt(1);
            }

            pstmItem = conn.prepareStatement(sqlItem);
            
            for (ItemFicha item : ficha.getItens()) {
                pstmItem.setInt(1, idFichaGerada);
                pstmItem.setInt(2, item.getExercicio().getId());
                pstmItem.setInt(3, item.getSeries());
                pstmItem.setString(4, item.getRepeticoes());
                
                pstmItem.setString(5, item.getTempo());
                
                String div = (item.getDivisao() != null) ? item.getDivisao() : "A";
                pstmItem.setString(6, div);
                
                pstmItem.addBatch();
            }
            
            pstmItem.executeBatch(); 

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmFicha != null) pstmFicha.close();
                if (pstmItem != null) pstmItem.close();
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public FichaTreino buscarFichaAtivaPorAluno(int idAluno) {
        String sqlFicha = "SELECT * FROM tb_ficha WHERE id_aluno = ? AND status = 'Ativa'";
        String sqlItens = "SELECT i.*, e.nome as nome_exercicio, e.grupo_muscular " +
                          "FROM tb_item_ficha i " +
                          "INNER JOIN tb_exercicio e ON i.id_exercicio = e.id_exercicio " +
                          "WHERE i.id_ficha = ?";
        
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        FichaTreino ficha = null;

        try {
            conn = ConexaoDAO.getConexao();
            
            pstm = conn.prepareStatement(sqlFicha);
            pstm.setInt(1, idAluno);
            rset = pstm.executeQuery();
            
            if (rset.next()) {
                ficha = new FichaTreino();
                ficha.setId(rset.getInt("id_ficha"));
                ficha.setObjetivo(rset.getString("objetivo"));
                
                try (PreparedStatement pstmItem = conn.prepareStatement(sqlItens)) {
                    pstmItem.setInt(1, ficha.getId());
                    ResultSet rsetItem = pstmItem.executeQuery();
                    
                    while (rsetItem.next()) {
                        ItemFicha item = new ItemFicha();
                        item.setId(rsetItem.getInt("id_item"));
                        item.setSeries(rsetItem.getInt("series"));
                        item.setRepeticoes(rsetItem.getString("repeticoes"));
                        item.setTempo(rsetItem.getString("tempo"));
                        item.setDivisao(rsetItem.getString("divisao"));
                        
                        br.com.gymmanager.model.Exercicio ex = new br.com.gymmanager.model.Exercicio();
                        ex.setId(rsetItem.getInt("id_exercicio"));
                        ex.setNome(rsetItem.getString("nome_exercicio"));
                        ex.setGrupoMuscular(rsetItem.getString("grupo_muscular"));
                        
                        item.setExercicio(ex);
                        ficha.adicionarItem(item);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return ficha;
    }

    public boolean excluirFichaDoAluno(int idAluno) {
        String sqlBusca = "SELECT id_ficha FROM tb_ficha WHERE id_aluno = ?";
        String sqlDelItens = "DELETE FROM tb_item_ficha WHERE id_ficha = ?";
        String sqlDelFicha = "DELETE FROM tb_ficha WHERE id_ficha = ?";
        
        Connection conn = null;
        try {
            conn = ConexaoDAO.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sqlBusca);
            pstm.setInt(1, idAluno);
            ResultSet rs = pstm.executeQuery();
            
            int idFicha = -1;
            if (rs.next()) {
                idFicha = rs.getInt("id_ficha");
            }
            rs.close();
            pstm.close();
            
            if (idFicha != -1) {
                pstm = conn.prepareStatement(sqlDelItens);
                pstm.setInt(1, idFicha);
                pstm.executeUpdate();
                pstm.close();
                
                pstm = conn.prepareStatement(sqlDelFicha);
                pstm.setInt(1, idFicha);
                pstm.executeUpdate();
                pstm.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if(conn!=null) conn.close(); } catch(Exception e){}
        }
    }
}