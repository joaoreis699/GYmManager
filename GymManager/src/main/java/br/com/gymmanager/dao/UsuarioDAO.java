/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Pacote: com.mycompany.teladelogin
// Pacote: com.mycompany.teladelogin
package br.com.gymmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt; // Verifique se esta importação não está com erro

public class UsuarioDAO {

    /**
     * Verifica se o CPF e a senha correspondem a um usuário no banco de dados.
     * @param cpf O CPF a ser verificado.
     * @param senha A senha pura (não criptografada) a ser verificada.
     * @return true se as credenciais forem válidas, false caso contrário.
     */
    public boolean verificarCredenciais(String cpf, String senha) {
        String sql = "SELECT senha_hash FROM usuarios WHERE cpf = ?";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpf);
            ResultSet rs = pstmt.executeQuery();

            // Verifica se encontrou um usuário com o CPF fornecido
            if (rs.next()) {
                String senhaHashDoBanco = rs.getString("senha_hash");

                // Compara a senha digitada com o hash salvo no banco
                if (BCrypt.checkpw(senha, senhaHashDoBanco)) {
                    return true; // Senha correta
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar credenciais: " + e.getMessage());
            // Você pode querer lançar uma exceção ou exibir um JOptionPane aqui
        }

        return false; // Retorna falso se o CPF não for encontrado ou a senha estiver incorreta
    }
}