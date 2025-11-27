/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joaoreis699
 */
package br.com.gymmanager.dao;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Plano;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AlunoDAO {

    public boolean cadastrar(Aluno aluno) {
        String sql = "INSERT INTO tb_aluno (nome, cpf, data_nascimento, telefone, email, caminho_foto, status, id_plano, data_matricula) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getCpf());
            pstm.setString(3, aluno.getDataNascimento());
            pstm.setString(4, aluno.getTelefone());
            pstm.setString(5, aluno.getEmail());
            pstm.setString(6, aluno.getCaminhoFoto());
            pstm.setString(7, aluno.getStatus());
            pstm.setInt(8, aluno.getPlano().getId()); 
            pstm.setString(9, aluno.getDataMatricula());

            pstm.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar aluno: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean atualizar(Aluno aluno) {
        String sql = "UPDATE tb_aluno SET nome=?, cpf=?, data_nascimento=?, telefone=?, email=?, caminho_foto=?, status=?, id_plano=?, data_matricula=? WHERE id_aluno=?";
        
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getCpf());
            pstm.setString(3, aluno.getDataNascimento());
            pstm.setString(4, aluno.getTelefone());
            pstm.setString(5, aluno.getEmail());
            pstm.setString(6, aluno.getCaminhoFoto());
            pstm.setString(7, aluno.getStatus());
            pstm.setInt(8, aluno.getPlano().getId());
            pstm.setString(9, aluno.getDataMatricula());
            pstm.setInt(10, aluno.getId());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar aluno: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean remover(int id) {
        // Precisamos apagar tudo que está ligado ao aluno antes de apagar o aluno
        String sqlDelItens = "DELETE FROM tb_item_ficha WHERE id_ficha IN (SELECT id_ficha FROM tb_ficha WHERE id_aluno = ?)";
        String sqlDelFicha = "DELETE FROM tb_ficha WHERE id_aluno = ?";
        String sqlDelPag   = "DELETE FROM tb_pagamento WHERE id_aluno = ?";
        String sqlDelAluno = "DELETE FROM tb_aluno WHERE id_aluno = ?";
        
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            conn.setAutoCommit(false); 

            pstm = conn.prepareStatement(sqlDelItens);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            pstm.close();

            pstm = conn.prepareStatement(sqlDelFicha);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            pstm.close();

            pstm = conn.prepareStatement(sqlDelPag);
            pstm.setInt(1, id);
            pstm.executeUpdate();
            pstm.close();

            pstm = conn.prepareStatement(sqlDelAluno);
            pstm.setInt(1, id);
            pstm.execute();

            conn.commit(); 
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException ex) { ex.printStackTrace(); }
            
            JOptionPane.showMessageDialog(null, "Erro ao remover aluno e seus dados vinculados: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }   

    public List<Aluno> listarTodos() {
        String sql = "SELECT a.*, p.nome AS nome_plano, p.valor AS valor_plano " +
                     "FROM tb_aluno a " +
                     "INNER JOIN tb_plano p ON a.id_plano = p.id_plano";

        List<Aluno> alunos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Aluno a = new Aluno();
                
                a.setId(rset.getInt("id_aluno"));
                a.setNome(rset.getString("nome"));
                a.setCpf(rset.getString("cpf"));
                a.setDataNascimento(rset.getString("data_nascimento"));
                a.setTelefone(rset.getString("telefone"));
                a.setEmail(rset.getString("email"));
                a.setCaminhoFoto(rset.getString("caminho_foto"));
                
                a.setStatus(rset.getString("status"));
                a.setDataMatricula(rset.getString("data_matricula"));
                
                Plano p = new Plano();
                p.setId(rset.getInt("id_plano"));
                p.setNome(rset.getString("nome_plano"));
                p.setValor(rset.getDouble("valor_plano"));
                
                a.setPlano(p);
                
                alunos.add(a);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar alunos: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return alunos;
    }

    public Aluno buscarPorCpf(String cpf) {
        String sql = "SELECT a.*, p.nome AS nome_plano, p.valor AS valor_plano " +
                     "FROM tb_aluno a " +
                     "INNER JOIN tb_plano p ON a.id_plano = p.id_plano " +
                     "WHERE a.cpf = ?";
        
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Aluno alunoEncontrado = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, cpf);
            rset = pstm.executeQuery();

            if (rset.next()) {
                alunoEncontrado = new Aluno();
                alunoEncontrado.setId(rset.getInt("id_aluno"));
                alunoEncontrado.setNome(rset.getString("nome"));
                alunoEncontrado.setCpf(rset.getString("cpf"));
                alunoEncontrado.setDataNascimento(rset.getString("data_nascimento"));
                alunoEncontrado.setTelefone(rset.getString("telefone"));
                alunoEncontrado.setEmail(rset.getString("email"));
                alunoEncontrado.setCaminhoFoto(rset.getString("caminho_foto"));
                alunoEncontrado.setStatus(rset.getString("status"));
                alunoEncontrado.setDataMatricula(rset.getString("data_matricula"));
                
                Plano p = new Plano();
                p.setId(rset.getInt("id_plano"));
                p.setNome(rset.getString("nome_plano"));
                p.setValor(rset.getDouble("valor_plano"));
                
                alunoEncontrado.setPlano(p);
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
        return alunoEncontrado;
    }
    
    public int contarAlunosAtivos() {
        String sql = "SELECT COUNT(*) AS total FROM tb_aluno WHERE status = 'Ativo'";
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
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return total;
    }
}