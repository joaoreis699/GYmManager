/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.dao.AlunoDAO;
import br.com.gymmanager.model.Aluno;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class DialogoBuscaAluno extends JDialog {

    private JTextField campoBusca;
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private JButton botaoSelecionar;
    private Aluno alunoSelecionado = null;
    private List<Aluno> todosAlunos; // Cache para não ir no banco toda hora

    public DialogoBuscaAluno(Window parent) {
        super(parent, "Selecionar Aluno", ModalityType.APPLICATION_MODAL);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel painelFundo = new JPanel(new BorderLayout(10, 10));
        painelFundo.setBackground(Color.WHITE);
        painelFundo.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(painelFundo);

        JPanel pTopo = new JPanel(new BorderLayout(5, 0));
        pTopo.setOpaque(false);
        pTopo.add(new JLabel("Buscar por Nome ou CPF:"), BorderLayout.NORTH);
        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar(campoBusca.getText());
            }
        });
        pTopo.add(campoBusca, BorderLayout.CENTER);
        painelFundo.add(pTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setRowHeight(25);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        painelFundo.add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);

        botaoSelecionar = new JButton("Selecionar Aluno");
        botaoSelecionar.setBackground(new Color(30, 90, 200));
        botaoSelecionar.setForeground(Color.WHITE);
        botaoSelecionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoSelecionar.addActionListener(e -> confirmarSelecao());
        painelFundo.add(botaoSelecionar, BorderLayout.SOUTH);

        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        AlunoDAO dao = new AlunoDAO();
        todosAlunos = dao.listarTodos();
        atualizarTabela(todosAlunos);
    }

    private void filtrar(String texto) {
        if (todosAlunos == null) return;
        String termo = texto.toLowerCase();
        
        List<Aluno> filtrados = todosAlunos.stream()
                .filter(a -> a.getNome().toLowerCase().contains(termo) || a.getCpf().contains(termo))
                .collect(Collectors.toList());
        
        atualizarTabela(filtrados);
    }

    private void atualizarTabela(List<Aluno> lista) {
        modeloTabela.setRowCount(0);
        for (Aluno a : lista) {
            modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getCpf()});
        }
    }

    private void confirmarSelecao() {
        int row = tabelaAlunos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na lista.");
            return;
        }
        
        int id = (int) tabelaAlunos.getValueAt(row, 0);
        for (Aluno a : todosAlunos) {
            if (a.getId() == id) {
                this.alunoSelecionado = a;
                break;
            }
        }
        dispose(); 
    }

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }
}
