/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

/**
 *
 * @author joaoreis699
 */


public class Plano {
    private int id;
    private String nome;
    private double valor;
    private int duracaoMeses;

    public Plano() {}

    public Plano(int id, String nome, double valor, int duracaoMeses) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.duracaoMeses = duracaoMeses;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    @Override
    public String toString() {
        return this.nome;
    }
}
