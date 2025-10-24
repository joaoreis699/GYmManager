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
    private String senha;
    private String caminhoFoto;
    
    public Funcionario() {
        super();
    }
    
    public Funcionario(String nome, String cpf, LocalDate dataNascimento, String telefone, String email, String cargo, String dataAdmissao, String senha, String caminhoFoto) {
        
        super(nome, dataNascimento, cpf, telefone, email);
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.senha = senha;
        this.caminhoFoto = caminhoFoto;
    }
    
    public String getCargo() { return cargo; }    
    public String getDataAdmissao() { return dataAdmissao; }
    public String getSenha() { return senha; }
    public String getCaminhoFoto() { return caminhoFoto; }

}



