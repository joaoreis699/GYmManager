/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;
import java.time.LocalDate;

/**
 * Classe Abstrata que serve como base para Aluno, Funcionario, etc.
 * Contém os atributos comuns a todas as pessoas no sistema.
 */
public abstract class Pessoa {

    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private String telefone;
    private String email;

    // Construtor vazio (boa prática ter um)
    public Pessoa() {
    }

    // Construtor com todos os campos para facilitar a criação de objetos
    public Pessoa(String nome, LocalDate dataNascimento, String cpf, String telefone, String email) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    // --- Getters e Setters ---
    // Métodos para acessar e modificar os atributos de forma controlada

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
