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

    // 1. Construtor Vazio (Necessário para o DAO)
    public Plano() {
    }

    // 2. Construtor Completo
    public Plano(int id, String nome, double valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    // --- GETTERS E SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    // --- IMPORTANTE PARA A TELA ---
    // Este método toString faz com que, quando colocarmos o objeto Plano
    // dentro de uma "caixa de seleção" (ComboBox), apareça o NOME dele,
    // e não um código estranho de memória.
    @Override
    public String toString() {
        return this.nome;
    }
}
