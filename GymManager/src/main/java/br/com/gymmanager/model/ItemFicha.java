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
    private Exercicio exercicio;
    private int series;
    private String repeticoes;
    
    private String tempo;
    
    private String divisao; 

    public ItemFicha() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Exercicio getExercicio() { return exercicio; }
    public void setExercicio(Exercicio exercicio) { this.exercicio = exercicio; }

    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }

    public String getRepeticoes() { return repeticoes; }
    public void setRepeticoes(String repeticoes) { this.repeticoes = repeticoes; }

    public String getTempo() { return tempo; }
    public void setTempo(String tempo) { this.tempo = tempo; }

    public String getDivisao() { return divisao; }
    public void setDivisao(String divisao) { this.divisao = divisao; }
}