/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

import java.time.LocalDateTime;
import java.util.List;

public class Aluno extends Pessoa {
    private String status;
    private Plano plano; 
    private String dataMatricula;

    public Aluno() {
        super();
    }
    
    public Aluno(int id, String nome, String cpf, String dataNascimento, String telefone, String email, String senha, String foto, String status, Plano plano, String dataMatricula) {
        super(id, nome, cpf, dataNascimento, telefone, email, senha, foto);
        //this.status = status;
        //his.plano = plano;
        //this.dataMatricula = dataMatricula;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }

    public String getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(String dataMatricula) { this.dataMatricula = dataMatricula; }
}
