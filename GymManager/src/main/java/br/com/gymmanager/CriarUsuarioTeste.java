/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Pacote: com.mycompany.teladelogin
package br.com.gymmanager;

import br.com.gymmanager.dao.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class CriarUsuarioTeste {

    public static void main(String[] args) {
        // --- DEFINA AQUI OS DADOS DO SEU PRIMEIRO USUÁRIO ---
        String nome = "Administrador";
        String cpf = "18478861750"; // Use um CPF de 11 dígitos, sem pontos ou traços
        String senhaPura = "61750"; // Esta será a senha para logar

        // =======================================================
        // O CÓDIGO ABAIXO FAZ A MÁGICA
        // =======================================================

        // Gera o "sal" e o hash da senha usando jBCrypt
        String senhaHash = BCrypt.hashpw(senhaPura, BCrypt.gensalt(12)); // O '12' é a força do hash

        System.out.println("--- Criando Usuário de Teste ---");
        System.out.println("CPF: " + cpf);
        System.out.println("Senha Pura: " + senhaPura);
        System.out.println("Hash Gerado (que será salvo no banco): " + senhaHash);

        // SQL para inserir o novo usuário no banco
        String sql = "INSERT INTO usuarios (cpf, senha_hash, nome) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Substitui os '?' pelos valores
            pstmt.setString(1, cpf);
            pstmt.setString(2, senhaHash);
            pstmt.setString(3, nome);

            // Executa o comando de inserção
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("\n>>> Usuário de teste inserido com SUCESSO no banco de dados!");
            } else {
                System.out.println("\n>>> FALHA ao inserir o usuário.");
            }

        } catch (SQLException e) {
            // Trata erros comuns, como CPF duplicado
            if (e.getErrorCode() == 1062) { // Código de erro para entrada duplicada no MySQL
                System.err.println("\nERRO: O CPF '" + cpf + "' já existe no banco de dados.");
            } else {
                System.err.println("\nERRO de SQL ao inserir usuário: " + e.getMessage());
            }
        }
    }
}