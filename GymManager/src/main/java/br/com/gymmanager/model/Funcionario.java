/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;
import java.time.LocalDate;
/**
 *
 * @author joaoreis699
 */
public class Funcionario extends Pessoa {
    
    private int id; 
    private String cargo;
    private String dataAdmissao;
    private String senha;
    private String caminhoFoto;
    
    public Funcionario() {
        super();
    }
    
    public Funcionario(String nome, String cpf, String dataNascimento, int id, String cargo, String dataAdmissao, String senha, String caminhoFoto) {
        
        super(nome, dataNascimento, cpf);
        this.id = id;
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
    }
    
    public Funcionario(String nome, String cpf, String dataNascimento, int id, String telefone, String email, String cargo, String dataAdmissao, String senha, String caminhoFoto) {
        
        super(nome, dataNascimento, cpf, telefone, email);
        this.id = id;
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCargo() { return cargo; } 
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getCaminhoFoto() { return caminhoFoto; }
    public void setCaminhoFoto(String caminhoFoto) { this.caminhoFoto = caminhoFoto; }

}



