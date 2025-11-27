/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.util;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Funcionario;

public class Sessao {
    
    private static Sessao instance;
    
    private Funcionario funcionarioLogado;

    private Sessao() {
    }

    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
        }
        return instance;
    }
    
    public Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;    
    }
        
    public boolean isFuncionario() {
        return funcionarioLogado != null;
    }
    
    public void limparSessao() {
        this.funcionarioLogado = null;
    }
    
    public String getNomeUsuario() {
        if (isFuncionario()) {
            return funcionarioLogado.getNome();
        }
        return "Desconhecido";
    }
}
