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
    private String cargo;
    private String dataAdmissao;
    
    public Funcionario() {
        super();
    }
    
    public Funcionario(int id, String nome, String cpf, String dataNascimento, String cargo, String dataAdmissao, String senha, String caminhoFoto) {
        super(id, nome, dataNascimento, cpf, senha, caminhoFoto);
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        
    }
    
    public Funcionario(String nome, String cpf, String dataNascimento, int id, String telefone, String email, String cargo, String dataAdmissao, String senha, String caminhoFoto) { 
        super(id, nome, dataNascimento, cpf, senha, telefone, email, caminhoFoto);
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
    }
    
    public String getCargo() { return cargo; } 
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String dataAdmissao) { this.dataAdmissao = dataAdmissao; }
}



