/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

import java.time.LocalDateTime;
import java.util.List;


/**
 * A classe Aluno herda todos os atributos de Pessoa e adiciona
 * informações específicas de um aluno da academia.
 */
public class Aluno extends Pessoa {

    //private List<Matricula>;
    private LocalDateTime dataMatricula;

    // Construtor vazio
    public Aluno() {
        super(); // Chama o construtor da classe pai (Pessoa)
    }

    // Construtor com todos os campos
    public Aluno(String nome, String cpf, String telefone, String email, LocalDateTime dataMatricula) {
        // 'super(...)' chama o construtor da classe Pessoa para preencher os dados herdados
        //(nome, cpf, telefone, email); 
        this.dataMatricula = dataMatricula;
    }

    // --- Getters e Setters dos atributos específicos de Aluno ---

    public LocalDateTime getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDateTime dataMatricula) {
        this.dataMatricula = dataMatricula;
    }
}
