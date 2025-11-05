/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

import java.time.LocalDateTime;
import java.util.List;

public class Aluno extends Pessoa {

    private LocalDateTime dataMatricula;

    public Aluno() {
        super(); 
    }

    public Aluno(String nome, String cpf, String telefone, String email, LocalDateTime dataMatricula) {
        
        this.dataMatricula = dataMatricula;
    }


    public LocalDateTime getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDateTime dataMatricula) {
        this.dataMatricula = dataMatricula;
    }
}
