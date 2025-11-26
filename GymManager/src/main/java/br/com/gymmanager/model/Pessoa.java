/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;
//import java.time.LocalDate;

public abstract class Pessoa {

    private int id; 
    private String nome;
    private String dataNascimento;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private String caminhoFoto;


    public Pessoa() {
    }
    
    public Pessoa(int id, String nome, String dataNascimento, String cpf, String senha, String caminhoFoto) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
    }
    

    public Pessoa(int id, String nome, String dataNascimento, String cpf, String telefone, String email, String senha, String caminhoFoto) {
        this(id, nome, dataNascimento, cpf, senha, caminhoFoto);
        this.telefone = telefone;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCaminhoFoto() { return caminhoFoto; }
    public void setCaminhoFoto(String caminhoFoto) { this.caminhoFoto = caminhoFoto; }
}
