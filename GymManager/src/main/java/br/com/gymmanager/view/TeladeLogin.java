/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.com.gymmanager.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.gymmanager.dao.FuncionarioDAO;
import br.com.gymmanager.dao.AlunoDAO;

import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.model.Aluno;

import br.com.gymmanager.util.Sessao;

public class TeladeLogin extends JFrame {

    private JTextField campoCpf;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JLabel labelLogo, labelCpf, labelSenha, titulo;

    public TeladeLogin() {
        setTitle("GymManager - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza
        setResizable(false);

        Color azul = new Color(30, 90, 200);
        Color fundo = Color.WHITE;

        JPanel painel = new JPanel();
        painel.setBackground(fundo);
        painel.setLayout(null);
        add(painel);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagens/logo.png"));
            Image logoRedimensionada = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            labelLogo = new JLabel(new ImageIcon(logoRedimensionada));
            labelLogo.setBounds(140, 40, 120, 120);
            painel.add(labelLogo);
        } catch (Exception e) {
        }

        // Título
        titulo = new JLabel("GYM MANAGER");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(azul);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(100, 160, 200, 40);
        painel.add(titulo);

        // Label CPF
        labelCpf = new JLabel("CPF:");
        labelCpf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelCpf.setBounds(60, 220, 100, 25);
        painel.add(labelCpf);

        campoCpf = new JTextField();
        campoCpf.setBounds(60, 245, 280, 30);
        campoCpf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painel.add(campoCpf);

        // Label Senha
        labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelSenha.setBounds(60, 290, 100, 25);
        painel.add(labelSenha);

        // Campo Senha
        campoSenha = new JPasswordField();
        campoSenha.setBounds(60, 315, 280, 30);
        campoSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painel.add(campoSenha);

        // Botão Entrar
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoEntrar.setBackground(azul);
        botaoEntrar.setForeground(Color.WHITE);
        botaoEntrar.setFocusPainted(false);
        botaoEntrar.setBounds(60, 370, 280, 40);
        painel.add(botaoEntrar);
        
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpfBruto = campoCpf.getText();
                String senha = new String(campoSenha.getPassword());
                
                String cpfLimpo = cpfBruto.replaceAll("[^0-9]", "");

                if (cpfLimpo.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha CPF e Senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return; 
                }

                FuncionarioDAO funcDAO = new FuncionarioDAO();
                Funcionario funcLogado = funcDAO.verificarCredenciais(cpfLimpo, senha);

                if (funcLogado != null) {
                    
                    Sessao.getInstance().setFuncionarioLogado(funcLogado);
                    
                    JOptionPane.showMessageDialog(null, "Bem-vindo(a), " + funcLogado.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    TelaPrincipal telaPrincipal = new TelaPrincipal();
                    telaPrincipal.setVisible(true);
                    dispose(); 
                    return;   
                } else {
                    JOptionPane.showMessageDialog(null, "CPF ou Senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new TeladeLogin().setVisible(true);
        });
    }
}