/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// DAOs
import br.com.gymmanager.dao.AlunoDAO;
import br.com.gymmanager.dao.PagamentoDAO;

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
    
    private JLabel labelValorAlunos;
    private JLabel labelValorPagamentos;
    
    public TelaPrincipal() {
        setTitle("GymManager - Dashboard Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        
        // Painel principal
        JPanel painelFundo = new JPanel(new BorderLayout(20, 20));
        painelFundo.setBackground(COR_FUNDO);
        painelFundo.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelFundo);

        // --- 1. CABEÇALHO ---
        JPanel painelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelHeader.setBackground(COR_FUNDO);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagens/logo.png"));
            Image logoRedimensionada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel labelLogo = new JLabel(new ImageIcon(logoRedimensionada));
            painelHeader.add(labelLogo);
        } catch (Exception e) {}
        
        JLabel labelTitulo = new JLabel("GYM MANAGER");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);

        painelHeader.add(labelTitulo);
        painelFundo.add(painelHeader, BorderLayout.NORTH);

        // --- 2. DASHBOARD ---
        JPanel painelDashboard = new JPanel(new BorderLayout(0, 20));
        painelDashboard.setBackground(COR_FUNDO);
        
        // --- 2.1 CARDS KPI ---
        // --- 2.1 CARDS KPI ---
        // --- 2.1 CARDS KPI ---
        
        // MUDANÇA: Usamos GridLayout(1, 3) -> 1 Linha, 3 Colunas IGUAIS
        // O terceiro parâmetro (20) é o espaçamento horizontal entre eles
        JPanel painelKpi = new JPanel(new GridLayout(1, 3, 20, 0));
        painelKpi.setBackground(COR_FUNDO);
        
        // (Não precisamos mais de GridBagConstraints aqui!)

        // Inicializa as variáveis dos labels
        labelValorAlunos = new JLabel("...");
        labelValorPagamentos = new JLabel("...");

        // --- CARD 1: ALUNOS ---
        JPanel cardAlunos = criarCardKpi("Alunos Ativos", labelValorAlunos, "/Imagens/icon_alunos.png");
        painelKpi.add(cardAlunos); // Apenas adiciona, o Grid cuida do tamanho
        
        // --- CARD 2: PAGAMENTOS ---
        JPanel cardPagamentos = criarCardKpi("Pagamentos Pendentes", labelValorPagamentos, "/Imagens/icon_pagamento.png");
        painelKpi.add(cardPagamentos);

        // --- CARD 3: CHECK-INS ---
        JPanel cardCheckins = criarCardKpi("Check-ins Hoje", new JLabel("0"), "/Imagens/icon_checkin.png");
        painelKpi.add(cardCheckins);
        
        painelDashboard.add(painelKpi, BorderLayout.NORTH);

        // --- 2.2 BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new GridLayout(2, 3, 20, 20)); 
        painelAcoes.setBackground(COR_FUNDO);
        painelAcoes.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Botão 1: Gestão de Alunos
        JButton botaoGestaoAlunos = criarBotaoAcao("Gestão de Alunos", "/Imagens/btn_alunos.png");
        botaoGestaoAlunos.addActionListener(e -> {
            TelaGestaoAlunos tela = new TelaGestaoAlunos(TelaPrincipal.this);
            tela.setVisible(true);
            atualizarKPIs(); // Atualiza ao voltar
        });
        painelAcoes.add(botaoGestaoAlunos);

        // Botão 2: Controle de Caixa
        JButton botaoCaixa = criarBotaoAcao("Controle de Caixa", "/Imagens/btn_caixa.png");
        botaoCaixa.addActionListener(e -> {
            TelaControleCaixa tela = new TelaControleCaixa(TelaPrincipal.this);
            tela.setVisible(true);
            atualizarKPIs(); // Atualiza ao voltar
        });
        painelAcoes.add(botaoCaixa);

        // Botão 3: Treino (Fictício por enquanto)
        painelAcoes.add(criarBotaoAcao("Gerar Ficha de Treino", "/Imagens/btn_treino.png"));

        // Botão 4: Gestão de Funcionários
        JButton botaoGestaoFuncionarios = criarBotaoAcao("Gestão de Funcionários", "/Imagens/btn_funcionarios.png");
        botaoGestaoFuncionarios.addActionListener(e -> {
            TelaGestaoFuncionario tela = new TelaGestaoFuncionario(TelaPrincipal.this);
            tela.setVisible(true);
        });
        painelAcoes.add(botaoGestaoFuncionarios);

        // Botão 5: Planos (Fictício)
        painelAcoes.add(criarBotaoAcao("Consultar Planos", "/Imagens/btn_planos.png"));
        
        // Botão 6: Visualizar Ficha (Fictício)
        JButton botaoVisualizarFicha = criarBotaoAcao("Visualizar Ficha", "/Imagens/btn_clipboard.png");
        botaoVisualizarFicha.addActionListener(e -> JOptionPane.showMessageDialog(this, "Em desenvolvimento"));
        painelAcoes.add(botaoVisualizarFicha);
        
        painelDashboard.add(painelAcoes, BorderLayout.CENTER);
        painelFundo.add(painelDashboard, BorderLayout.CENTER);

        // --- 3. RODAPÉ ---
        JLabel labelRodape = new JLabel("© 2025 GymManager — Todos os direitos reservados", SwingConstants.CENTER);
        labelRodape.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelRodape.setForeground(COR_TEXTO_PADRAO);
        painelFundo.add(labelRodape, BorderLayout.SOUTH);
        
        // Carrega os números iniciais
        atualizarKPIs();
    }
    
    /**
     * Método novo para atualizar os números do banco.
     */
    private void atualizarKPIs() {
        // 1. Atualiza Alunos
        try {
            AlunoDAO alunoDAO = new AlunoDAO();
            int totalAlunos = alunoDAO.contarAlunosAtivos();
            labelValorAlunos.setText(String.valueOf(totalAlunos));
        } catch (Exception e) {
            labelValorAlunos.setText("Erro");
        }

        // 2. Atualiza Pagamentos
        try {
            PagamentoDAO pagamentoDAO = new PagamentoDAO();
            int totalPendencias = pagamentoDAO.contarPendencias();
            labelValorPagamentos.setText(String.valueOf(totalPendencias));
        } catch (Exception e) {
            labelValorPagamentos.setText("Erro");
        }
    }
    
    /**
     * Método fábrica atualizado: Recebe JLabel em vez de String.
     */
    private JPanel criarCardKpi(String titulo, JLabel labelValorObjeto, String caminhoIcone) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            JLabel labelIcone = new JLabel(new ImageIcon(img));
            card.add(labelIcone, BorderLayout.WEST);
        } catch (Exception e) {}

        JPanel painelTexto = new JPanel();
        painelTexto.setOpaque(false);
        painelTexto.setLayout(new BoxLayout(painelTexto, BoxLayout.Y_AXIS));
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(FONTE_KPI_TITULO);
        labelTitulo.setForeground(COR_TEXTO_PADRAO);
        
        // Configura o label recebido
        labelValorObjeto.setFont(FONTE_KPI_VALOR);
        labelValorObjeto.setForeground(COR_TEXTO_KPI);

        painelTexto.add(labelTitulo);
        painelTexto.add(Box.createVerticalStrut(5));
        painelTexto.add(labelValorObjeto);
        
        card.add(painelTexto, BorderLayout.CENTER);
        return card;
    }

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
        
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setIconTextGap(15);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(caminhoIcone));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            botao.setIcon(new ImageIcon(img));
        } catch (Exception e) {}

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
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
