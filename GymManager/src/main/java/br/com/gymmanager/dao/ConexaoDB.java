/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Pacote: com.mycompany.teladelogin
package br.com.gymmanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    // Coloque aqui os dados do seu banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/gym_manager_db";
    private static final String USUARIO = "root"; // seu usuário
    private static final String SENHA = "28072000"; // sua senha

    public static Connection getConexao() {
        try {
            // Carrega o driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Retorna a conexão com o banco de dados
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException | SQLException e) {
            // Lança uma exceção em caso de erro
            throw new RuntimeException("Erro na conexão com o banco de dados: ", e);
        }
    }
}