/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.dao;

import br.com.gymmanager.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
/**
 *
 * @author joaoreis699
 */
public class FuncionarioDAO {
    
    public boolean cadastrar(Funcionario func) {
        String sql =  "INSERT INTO funcionarios (nome, cpf, data_nascimento, telefone, email, cargo, data_admissao, senha, caminho_foto) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, func.getNome());
            pstm.setString(2, func.getCpf());
            pstm.setString(3, func.getDataNascimento());
            pstm.setString(4, func.getTelefone());
            pstm.setString(5, func.getEmail());
            pstm.setString(6, func.getCargo());
            pstm.setString(7, func.getDataAdmissao());
            pstm.setString(8, func.getSenha());
            pstm.setString(9, func.getCaminhoFoto());
            
            pstm.execute();
            return true;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar funcionário: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } 
    }

    public boolean atualizar(Funcionario func) {
        String sql = "UPDATE funcionarios SET nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, email = ?, cargo = ?, data_admissao = ?, senha = ?, caminho_foto = ? " +
                    "WHERE id_funcionario = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, func.getNome());
            pstm.setString(2, func.getCpf());
            pstm.setString(3, func.getDataNascimento());
            pstm.setString(4, func.getTelefone());
            pstm.setString(5, func.getEmail());
            pstm.setString(6, func.getCargo());
            pstm.setString(7, func.getDataAdmissao());
            pstm.setString(8, func.getSenha());
            pstm.setString(9, func.getCaminhoFoto());

            pstm.setInt(10, func.getId());

            pstm.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar funcionario: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM funcionarios WHERE id_funcionario = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1, id);

            pstm.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover funcionário: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e ) {
                e.printStackTrace();
            }
        }
    }

    public List<Funcionario> listarTodos() {
        String sql = "SELECT * FROM funcionarios"; 

        List<Funcionario> funcionarios = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null; // guarda resultado da busca

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            rset = pstm.executeQuery(); // vai iniciar a consulta

            while(rset.next()) {
                Funcionario func = new Funcionario();

                func.setId(rset.getInt("id_funcionario"));
                func.setNome(rset.getString("nome"));
                func.setCpf(rset.getString("cpf"));
                func.setDataNascimento(rset.getString("data_nascimento"));
                func.setTelefone(rset.getString("telefone"));
                func.setEmail(rset.getString("email"));
                func.setCargo(rset.getString("cargo"));
                func.setDataAdmissao(rset.getString("data_admissao"));
                func.setSenha(rset.getString("senha"));
                func.setCaminhoFoto(rset.getString("caminho_foto"));

                funcionarios.add(func);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar funcionários: " + e.getMessage());
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return funcionarios;
    }
    public Funcionario verificarCredenciais(String cpf, String senha) {
        String sql = "SELECT * FROM funcionarios WHERE cpf = ? AND senha = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConexaoDAO.getConexao();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1, cpf);
            pstm.setString(2, senha);

            rset = pstm.executeQuery();

            if(rset.next()) {

                Funcionario func = new Funcionario();

                func.setId(rset.getInt("id_funcionario"));
                func.setNome(rset.getString("nome"));
                func.setCpf(rset.getString("cpf"));
                func.setDataNascimento(rset.getString("data_nascimento"));
                func.setTelefone(rset.getString("telefone"));
                func.setEmail(rset.getString("email"));
                func.setCargo(rset.getString("cargo"));
                func.setDataAdmissao(rset.getString("data_admissao"));
                func.setSenha(rset.getString("senha"));
                func.setCaminhoFoto(rset.getString("caminho_foto"));
                
                return func;

            } else {
                return null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao validar credenciais: " + e.getMessage());
            return null;
        } finally {
            try {
                if(rset != null) rset.close();
                if(pstm != null) pstm.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
            e.printStackTrace();
            }
        }
    }
} 
