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
        String sql = "INSERT INTO tb_aluno (nome, cpf, data_nascimento, telefone, email, senha, caminho_foto, status, id_plano, data_matricula) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
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
            pstm.setString(6, aluno.getSenha());
            pstm.setString(7, aluno.getCaminhoFoto());
            pstm.setString(8, aluno.getStatus());
            pstm.setInt(9, aluno.getPlano().getId()); 
            pstm.setString(10, aluno.getDataMatricula());

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
                a.setSenha(rset.getString("senha"));
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
}
