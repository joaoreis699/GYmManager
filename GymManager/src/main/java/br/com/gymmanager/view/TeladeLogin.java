/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.com.gymmanager.view;

import br.com.gymmanager.dao.UsuarioDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;

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

        // Cores principais
        Color azul = new Color(30, 90, 200);
        Color fundo = Color.WHITE;

        // Painel principal
        JPanel painel = new JPanel();
        painel.setBackground(fundo);
        painel.setLayout(null);
        add(painel);

        // Logo
        // O MAIS RECOMENDADO
        // O caminho deve ser a estrutura de pacotes que leva ao arquivo.
        
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagens/logo.png"));
        Image logoRedimensionada = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        labelLogo = new JLabel(new ImageIcon(logoRedimensionada));
        labelLogo.setBounds(140, 40, 120, 120);
        painel.add(labelLogo);

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

        // Campo CPF
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
            // Pega os dados digitados pelo usuário
            String cpf = campoCpf.getText();
            String senha = new String(campoSenha.getPassword());

            // Verifica se os campos não estão vazios
            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha CPF e Senha.", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
                return; // Para a execução
            }

            // Cria uma instância do DAO para verificar as credenciais
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            if (usuarioDAO.verificarCredenciais(cpf, senha)) {
    // Se as credenciais estiverem corretas
    JOptionPane.showMessageDialog(null, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

    // PASSO 1: Cria uma instância da nossa nova tela
    TelaPrincipal telaPrincipal = new TelaPrincipal();
    
    // PASSO 2: Torna a tela principal visível
    telaPrincipal.setVisible(true);

    // PASSO 3: Fecha a tela de login atual
    dispose(); 

} else {
    // Se as credenciais estiverem incorretas
    JOptionPane.showMessageDialog(null, "CPF ou Senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
}
        }
    });
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TeladeLogin().setVisible(true);
        });
    }
}
