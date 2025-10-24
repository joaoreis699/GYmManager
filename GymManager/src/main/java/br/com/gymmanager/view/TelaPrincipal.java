/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    // --- CONSTANTES DE ESTILO ---
    private static final Color COR_FUNDO = new Color(240, 242, 245);
    private static final Color COR_AZUL_PRINCIPAL = new Color(30, 90, 200);
    private static final Color COR_BRANCO = Color.WHITE;
    private static final Color COR_TEXTO_PADRAO = new Color(80, 80, 80);
    private static final Color COR_TEXTO_KPI = new Color(50, 50, 50);

    private static final Font FONTE_TITULO_LOGO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONTE_BOTAO_ACAO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONTE_KPI_TITULO = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONTE_KPI_VALOR = new Font("Segoe UI", Font.BOLD, 38);
    
    public TelaPrincipal() {
        setTitle("GymManager - Dashboard Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        
        // Painel principal que cobre toda a janela
        JPanel painelFundo = new JPanel(new BorderLayout(20, 20));
        painelFundo.setBackground(COR_FUNDO);
        painelFundo.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelFundo);

        // --- 1. CABEÇALHO (HEADER) ---
        JPanel painelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelHeader.setBackground(COR_FUNDO);

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagens/logo.png"));
        Image logoRedimensionada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel labelLogo = new JLabel(new ImageIcon(logoRedimensionada));
        
        JLabel labelTitulo = new JLabel("GYM MANAGER");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);

        painelHeader.add(labelLogo);
        painelHeader.add(labelTitulo);
        painelFundo.add(painelHeader, BorderLayout.NORTH);

        // --- 2. PAINEL DE AÇÕES E KPIS (CENTRO) ---
        JPanel painelDashboard = new JPanel(new BorderLayout(0, 20));
        painelDashboard.setBackground(COR_FUNDO);
        
        // --- 2.1 PAINEL DOS CARDS DE KPI (TOPO DO DASHBOARD) ---
        JPanel painelKpi = new JPanel(new GridBagLayout());
        painelKpi.setBackground(COR_FUNDO);
        GridBagConstraints gbcKpi = new GridBagConstraints();
        gbcKpi.insets = new Insets(0, 10, 0, 10);
        gbcKpi.fill = GridBagConstraints.BOTH;

        // ** Card Principal (Alunos Ativos) - Ocupa 2 colunas **
        gbcKpi.gridx = 0;
        gbcKpi.gridy = 0;
        gbcKpi.gridwidth = 2; // Ocupa o dobro do espaço
        gbcKpi.weightx = 0.66; // Ocupa 66% do espaço horizontal
        JPanel cardAlunos = criarCardKpi("Alunos Ativos", "142", "/Imagens/icon_alunos.png");
        painelKpi.add(cardAlunos, gbcKpi);
        
        // ** Card Secundário 1 (Pagamentos) **
        gbcKpi.gridx = 2;
        gbcKpi.gridy = 0;
        gbcKpi.gridwidth = 1; // Volta ao normal
        gbcKpi.weightx = 0.33; // Ocupa 33%
        JPanel cardPagamentos = criarCardKpi("Pagamentos Pendentes", "15", "/Imagens/icon_pagamento.png");
        painelKpi.add(cardPagamentos, gbcKpi);

        // ** Card Secundário 2 (Check-ins) **
        gbcKpi.gridx = 3;
        gbcKpi.gridy = 0;
        JPanel cardCheckins = criarCardKpi("Check-ins Hoje", "48", "/Imagens/icon_checkin.png");
        painelKpi.add(cardCheckins, gbcKpi);
        
        painelDashboard.add(painelKpi, BorderLayout.NORTH);

        // --- 2.2 PAINEL DOS BOTÕES DE AÇÃO (CENTRO DO DASHBOARD) ---
        JPanel painelAcoes = new JPanel(new GridLayout(2, 3, 20, 20)); // 2 linhas, 3 colunas, com espaçamento
        painelAcoes.setBackground(COR_FUNDO);
        painelAcoes.setBorder(new EmptyBorder(10, 10, 10, 10));

        painelAcoes.add(criarBotaoAcao("Gestão de Alunos", "/Imagens/btn_alunos.png"));
        painelAcoes.add(criarBotaoAcao("Controle de Caixa", "/Imagens/btn_caixa.png"));
        painelAcoes.add(criarBotaoAcao("Gerar Ficha de Treino", "/Imagens/btn_treino.png"));
        painelAcoes.add(criarBotaoAcao("Gestão de Funcionários", "/Imagens/btn_funcionarios.png"));
        painelAcoes.add(criarBotaoAcao("Consultar Planos", "/Imagens/btn_planos.png"));
        // Você pode adicionar mais um botão aqui para completar a grade ou deixar vazio.
        
        painelDashboard.add(painelAcoes, BorderLayout.CENTER);
        
        painelFundo.add(painelDashboard, BorderLayout.CENTER);

        // --- 3. RODAPÉ ---
        JLabel labelRodape = new JLabel("© 2025 GymManager — Todos os direitos reservados", SwingConstants.CENTER);
        labelRodape.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelRodape.setForeground(COR_TEXTO_PADRAO);
        painelFundo.add(labelRodape, BorderLayout.SOUTH);
    }
    
    /**
     * Método fábrica para criar os cards de KPI.
     */
    private JPanel criarCardKpi(String titulo, String valor, String caminhoIcone) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Ícone
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            JLabel labelIcone = new JLabel(new ImageIcon(img));
            card.add(labelIcone, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Ícone não encontrado: " + caminhoIcone);
        }

        // Textos (Título e Valor)
        JPanel painelTexto = new JPanel();
        painelTexto.setOpaque(false);
        painelTexto.setLayout(new BoxLayout(painelTexto, BoxLayout.Y_AXIS));
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(FONTE_KPI_TITULO);
        labelTitulo.setForeground(COR_TEXTO_PADRAO);
        
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(FONTE_KPI_VALOR);
        labelValor.setForeground(COR_TEXTO_KPI);

        painelTexto.add(labelTitulo);
        painelTexto.add(Box.createVerticalStrut(5));
        painelTexto.add(labelValor);
        
        card.add(painelTexto, BorderLayout.CENTER);
        return card;
    }

    /**
     * Método fábrica para criar os botões de ação do dashboard.
     */
    private JButton criarBotaoAcao(String texto, String caminhoIcone) {
        JButton botao = new JButton(texto);
        botao.setFont(FONTE_BOTAO_ACAO);
        botao.setBackground(COR_BRANCO);
        botao.setForeground(COR_TEXTO_PADRAO);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(20, 10, 20, 10)
        ));
        
        // Posiciona o texto abaixo do ícone
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(15);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Ícone não encontrado: " + caminhoIcone);
        }

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(COR_AZUL_PRINCIPAL);
                botao.setForeground(COR_BRANCO);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(COR_BRANCO);
                botao.setForeground(COR_TEXTO_PADRAO);
            }
        });
        return botao;
    }

    public static void main(String[] args) {
        // Define um Look and Feel mais moderno, se disponível
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
