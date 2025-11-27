/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.model;

/**
 *
 * @author joaoreis699
 */

import java.util.ArrayList;
import java.util.List;

public class FichaTreino {
    private int id;
    private Aluno aluno;
    private String objetivo;
    private String dataCriacao;
    private String status;
    
    private List<ItemFicha> itens = new ArrayList<>();

    public FichaTreino() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<ItemFicha> getItens() { return itens; }
    public void setItens(List<ItemFicha> itens) { this.itens = itens; }
    
    public void adicionarItem(ItemFicha item) {
        this.itens.add(item);
    }
}
