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

    public Funcionario() {
        super();
    }

    public Funcionario(int id, String nome, String cpf, String dataNasc, String tel, String email, String foto, String cargo, String dataAdmissao, String senha) {
        super(id, nome, cpf, dataNasc, tel, email, foto);
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
        this.senha = senha;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}


