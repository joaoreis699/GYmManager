/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

/**
 *
 * @author joaoreis699
 */

public class ItemFicha {
    private int id;
    private Exercicio exercicio; // O item TEM UM exercício
    private int series;
    private String repeticoes;
    private String carga;

    public ItemFicha() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Exercicio getExercicio() { return exercicio; }
    public void setExercicio(Exercicio exercicio) { this.exercicio = exercicio; }

    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }

    public String getRepeticoes() { return repeticoes; }
    public void setRepeticoes(String repeticoes) { this.repeticoes = repeticoes; }

    public String getCarga() { return carga; }
    public void setCarga(String carga) { this.carga = carga; }
}