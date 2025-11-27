/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.dao.ExercicioDAO;
import br.com.gymmanager.model.Exercicio;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoNovoExercicio extends JDialog {

    private JTextField campoNome;
    private JComboBox<String> comboGrupo;
    private boolean cadastrou = false;

    public DialogoNovoExercicio(Window owner) {
        super(owner, "Novo Exercício", ModalityType.APPLICATION_MODAL);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel painel = new JPanel(new GridLayout(3, 1, 10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(Color.WHITE);

        // Campo Nome
        JPanel pNome = new JPanel(new BorderLayout());
        pNome.setOpaque(false);
        pNome.add(new JLabel("Nome do Exercício:"), BorderLayout.NORTH);
        campoNome = new JTextField();
        pNome.add(campoNome, BorderLayout.CENTER);
        painel.add(pNome);

        // Campo Grupo
        JPanel pGrupo = new JPanel(new BorderLayout());
        pGrupo.setOpaque(false);
        pGrupo.add(new JLabel("Grupo Muscular:"), BorderLayout.NORTH);
        String[] grupos = {"Peito", "Costas", "Pernas", "Ombros", "Bíceps", "Tríceps", "Abdômen", "Cardio", "Outros"};
        comboGrupo = new JComboBox<>(grupos);
        pGrupo.add(comboGrupo, BorderLayout.CENTER);
        painel.add(pGrupo);

        JButton btnSalvar = new JButton("Salvar Exercício");
        btnSalvar.setBackground(new Color(30, 90, 200));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnSalvar.addActionListener(e -> salvar());
        
        painel.add(btnSalvar); // Adiciona o botão no Grid

        add(painel, BorderLayout.CENTER);
    }

    private void salvar() {
        String nome = campoNome.getText();
        String grupo = (String) comboGrupo.getSelectedItem();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do exercício.");
            return;
        }

        Exercicio ex = new Exercicio();
        ex.setNome(nome);
        ex.setGrupoMuscular(grupo);

        ExercicioDAO dao = new ExercicioDAO();
        if (dao.cadastrar(ex)) {
            JOptionPane.showMessageDialog(this, "Exercício adicionado ao catálogo!");
            cadastrou = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar.");
        }
    }

    public boolean isCadastrou() {
        return cadastrou;
    }
}