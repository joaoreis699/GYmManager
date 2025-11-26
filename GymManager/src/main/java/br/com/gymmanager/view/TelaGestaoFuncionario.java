/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.util.List;

import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.dao.FuncionarioDAO;

// MUDANÇA 1: Extends JDialog
public class TelaGestaoFuncionario extends JDialog {

    // --- CONSTANTES DE ESTILO ---
    private static final Color COR_FUNDO = new Color(240, 242, 245);
    private static final Color COR_AZUL_PRINCIPAL = new Color(30, 90, 200);
    private static final Color COR_BRANCO = Color.WHITE;
    private static final Color COR_TEXTO_PADRAO = new Color(80, 80, 80);
    private static final Color COR_TEXTO_CAMPO = new Color(50, 50, 50);
    private static final Color COR_VERMELHO_REMOVER = new Color(210, 50, 50);

    private static final Font FONTE_TITULO_LOGO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONTE_TITULO_FORM = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_LABEL_FORM = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_CAMPO_FORM = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_NOME_CARD = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_INFO_CARD = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONTE_KPI_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    private static final Border BORDA_CAMPO_PADRAO = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(5, 5, 5, 5)
    );

    // --- Componentes do Lado Direito ---
    private CardLayout layoutLadoDireito;
    private JPanel painelLadoDireito;
    private JPanel painelFormulario;
    private JPanel painelPlaceholder;
    private static final String PAINEL_FORM = "FORMULARIO";
    private static final String PAINEL_PLACEHOLDER = "PLACEHOLDER";

    // --- Componentes do Formulário ---
    private JLabel labelTituloForm;
    private JTextField campoNome;
    private JFormattedTextField campoCpf, campoDataNasc, campoDataAdmissao;
    private JComboBox<String> comboCargo;
    private JPasswordField campoSenha, campoConfirmaSenha;
    private JFormattedTextField campoTelefone;
    private JTextField campoEmail;
    private JButton botaoAcaoPrimaria, botaoAcaoSecundaria, botaoSelecionarFoto;
    private JLabel labelFotoPreview;
    private String caminhoDaFotoSelecionada = null;

    // --- Componentes da Lista (Esquerda) ---
    private JPanel painelListaCards;
    private JScrollPane scrollLista;

    // --- Controle de Estado ---
    private Funcionario funcionarioSelecionadoParaEdicao = null;
    private FuncionarioDAO funcionarioDAO;

    // MUDANÇA 2: Construtor recebe JFrame parent e configura JDialog
    public TelaGestaoFuncionario(JFrame parent) {
        super(parent, "GymManager - Gestão de Funcionários", true);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // MUDANÇA 3: Maximizar manualmente usando Toolkit
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        this.funcionarioDAO = new FuncionarioDAO();

        JPanel painelFundo = new JPanel(new BorderLayout(20, 20));
        painelFundo.setBackground(COR_FUNDO);
        painelFundo.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelFundo);

        painelFundo.add(criarHeader(), BorderLayout.NORTH);
        painelFundo.add(criarPainelLadoEsquerdo(), BorderLayout.CENTER);
        painelFundo.add(criarPainelLadoDireito(), BorderLayout.EAST);

        JLabel labelRodape = new JLabel("© 2025 GymManager — Todos os direitos reservados", SwingConstants.CENTER);
        labelRodape.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelRodape.setForeground(COR_TEXTO_PADRAO);
        painelFundo.add(labelRodape, BorderLayout.SOUTH);

        atualizarListaFuncionarios();
    }

    private JPanel criarHeader() {
        JPanel painelHeader = new JPanel(new BorderLayout());
        painelHeader.setBackground(COR_FUNDO);

        JPanel painelLogoTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        painelLogoTitulo.setOpaque(false);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Imagens/logo.png"));
            Image logoRedimensionada = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel labelLogo = new JLabel(new ImageIcon(logoRedimensionada));
            painelLogoTitulo.add(labelLogo);
        } catch (Exception e) { }

        JLabel labelTitulo = new JLabel("GESTÃO DE FUNCIONÁRIOS");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);
        painelLogoTitulo.add(labelTitulo);

        painelHeader.add(painelLogoTitulo, BorderLayout.WEST);

        JButton botaoVoltar = new JButton("Voltar a Tela Principal");
        
        estilizarBotaoVoltar(botaoVoltar); 
        
        // MUDANÇA 4: Botão voltar simplificado (apenas dispose)
        botaoVoltar.addActionListener(e -> dispose());
        
        painelHeader.add(botaoVoltar, BorderLayout.EAST);

        return painelHeader;
    }

    private JPanel criarPainelLadoEsquerdo() {
        JPanel painelCentral = new JPanel(new BorderLayout(0, 10));
        painelCentral.setOpaque(false);

        JButton botaoAdicionar = new JButton("+ Adicionar Novo Funcionário");
        estilizarBotaoPrimario(botaoAdicionar); 
        botaoAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoAdicionar.setPreferredSize(new Dimension(0, 40));
        botaoAdicionar.addActionListener(e -> exibirModoAdicao());
        painelCentral.add(botaoAdicionar, BorderLayout.NORTH);

        painelListaCards = new JPanel();
        painelListaCards.setBackground(COR_FUNDO);
        painelListaCards.setLayout(new BoxLayout(painelListaCards, BoxLayout.Y_AXIS));

        JPanel wrapperPainelLista = new JPanel(new BorderLayout());
        wrapperPainelLista.setOpaque(false);
        wrapperPainelLista.add(painelListaCards, BorderLayout.NORTH);

        scrollLista = new JScrollPane(wrapperPainelLista);
        scrollLista.getViewport().setBackground(COR_FUNDO); 
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        scrollLista.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollLista.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);

        JPanel painelContainerLista = new JPanel(new BorderLayout());
        painelContainerLista.setOpaque(false);
        painelContainerLista.add(scrollLista, BorderLayout.CENTER);
        painelContainerLista.setBorder(BorderFactory.createTitledBorder("Funcionários Cadastrados"));

        painelCentral.add(painelContainerLista, BorderLayout.CENTER);
        return painelCentral;
    }

    private JPanel criarPainelLadoDireito() {
        layoutLadoDireito = new CardLayout();
        painelLadoDireito = new JPanel(layoutLadoDireito);
        painelLadoDireito.setOpaque(false);
        painelLadoDireito.setPreferredSize(new Dimension(450, 0)); 

        criarPainelFormulario();

        painelPlaceholder = new JPanel(new GridBagLayout());
        painelPlaceholder.setBackground(COR_FUNDO);
        JLabel labelPlaceholder = new JLabel("<html><center>Selecione um funcionário à esquerda para ver os detalhes<br>ou<br>Clique em '+ Adicionar Novo Funcionário' para cadastrar.</center></html>");
        labelPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelPlaceholder.setForeground(COR_TEXTO_PADRAO);
        labelPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        painelPlaceholder.add(labelPlaceholder);

        painelLadoDireito.add(painelPlaceholder, PAINEL_PLACEHOLDER);
        painelLadoDireito.add(painelFormulario, PAINEL_FORM);
        layoutLadoDireito.show(painelLadoDireito, PAINEL_PLACEHOLDER);

        return painelLadoDireito;
    }

    private void criarPainelFormulario() {
        painelFormulario = new JPanel(new BorderLayout(10, 10));
        painelFormulario.setBackground(COR_BRANCO);
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        
        labelTituloForm = new JLabel("Adicionar Novo Funcionário");
        labelTituloForm.setFont(FONTE_TITULO_FORM);
        labelTituloForm.setForeground(COR_AZUL_PRINCIPAL);
        labelTituloForm.setBorder(new EmptyBorder(0, 0, 10, 0));
        painelFormulario.add(labelTituloForm, BorderLayout.NORTH);

        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LINHA 0: Foto
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelFoto = new JLabel("Foto:"); estilizarLabel(labelFoto); camposPanel.add(labelFoto, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        labelFotoPreview = new JLabel(); labelFotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY)); labelFotoPreview.setPreferredSize(new Dimension(80, 80)); labelFotoPreview.setHorizontalAlignment(SwingConstants.CENTER); labelFotoPreview.setText("Sem Foto"); camposPanel.add(labelFotoPreview, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        botaoSelecionarFoto = new JButton("Selecionar..."); estilizarBotaoPadrao(botaoSelecionarFoto); 
        botaoSelecionarFoto.addActionListener(e -> selecionarFoto());
        camposPanel.add(botaoSelecionarFoto, gbc);

        // LINHA 1: Nome
        gbc.gridx = 0; gbc.gridy = 1; JLabel labelNome = new JLabel("Nome:"); estilizarLabel(labelNome); camposPanel.add(labelNome, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; campoNome = new JTextField(); estilizarCampo(campoNome); camposPanel.add(campoNome, gbc); gbc.gridwidth = 1;

        // LINHA 2: CPF 
        gbc.gridx = 0; gbc.gridy = 2; JLabel labelCpf = new JLabel("CPF (Login):"); estilizarLabel(labelCpf); camposPanel.add(labelCpf, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        try { campoCpf = new JFormattedTextField(new MaskFormatter("###.###.###-##")); } catch (ParseException e) { campoCpf = new JFormattedTextField(); }
        estilizarCampo(campoCpf); camposPanel.add(campoCpf, gbc); gbc.gridwidth = 1;

        // LINHA 3: Data Nasc
        gbc.gridx = 0; gbc.gridy = 3; JLabel labelNasc = new JLabel("Data Nasc:"); estilizarLabel(labelNasc); camposPanel.add(labelNasc, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        try { campoDataNasc = new JFormattedTextField(new MaskFormatter("##/##/####")); } catch (ParseException e) { campoDataNasc = new JFormattedTextField(); }
        estilizarCampo(campoDataNasc); camposPanel.add(campoDataNasc, gbc); gbc.gridwidth = 1;

        // --- NOVOS CAMPOS ---
        
        // LINHA 4: Telefone
        gbc.gridx = 0; gbc.gridy = 4; JLabel labelTel = new JLabel("Telefone:"); estilizarLabel(labelTel); camposPanel.add(labelTel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        try { campoTelefone = new JFormattedTextField(new MaskFormatter("(##) #####-####")); } catch (ParseException e) { campoTelefone = new JFormattedTextField(); }
        estilizarCampo(campoTelefone); camposPanel.add(campoTelefone, gbc); gbc.gridwidth = 1;
        
        // LINHA 5: Email
        gbc.gridx = 0; gbc.gridy = 5; JLabel labelEmail = new JLabel("Email:"); estilizarLabel(labelEmail); camposPanel.add(labelEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        campoEmail = new JTextField(); estilizarCampo(campoEmail); camposPanel.add(campoEmail, gbc); gbc.gridwidth = 1;

        // --------------------

        // LINHA 6: Cargo (Mudou de 4 para 6)
        gbc.gridx = 0; gbc.gridy = 6; JLabel labelCargo = new JLabel("Cargo:"); estilizarLabel(labelCargo); camposPanel.add(labelCargo, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        comboCargo = new JComboBox<>(new String[]{"Recepcionista", "Instrutor", "Gerente"}); 
        comboCargo.setFont(FONTE_CAMPO_FORM); comboCargo.setForeground(COR_TEXTO_CAMPO); comboCargo.setBackground(COR_BRANCO);
        camposPanel.add(comboCargo, gbc); gbc.gridwidth = 1;

        // LINHA 7: Admissão (Mudou de 5 para 7)
        gbc.gridx = 0; gbc.gridy = 7; JLabel labelAdm = new JLabel("Admissão:"); estilizarLabel(labelAdm); camposPanel.add(labelAdm, gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
        try { campoDataAdmissao = new JFormattedTextField(new MaskFormatter("##/##/####")); } catch (ParseException e) { campoDataAdmissao = new JFormattedTextField(); }
        estilizarCampo(campoDataAdmissao); camposPanel.add(campoDataAdmissao, gbc); gbc.gridwidth = 1;

        // LINHA 8: Senha (Mudou de 6 para 8)
        gbc.gridx = 0; gbc.gridy = 8; JLabel labelSenha = new JLabel("Senha:"); estilizarLabel(labelSenha); camposPanel.add(labelSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 8; gbc.gridwidth = 2; campoSenha = new JPasswordField(); estilizarCampo(campoSenha); camposPanel.add(campoSenha, gbc); gbc.gridwidth = 1;

        // LINHA 9: Confirma Senha (Mudou de 7 para 9)
        gbc.gridx = 0; gbc.gridy = 9; JLabel labelConf = new JLabel("Confirmar:"); estilizarLabel(labelConf); camposPanel.add(labelConf, gbc);
        gbc.gridx = 1; gbc.gridy = 9; gbc.gridwidth = 2; campoConfirmaSenha = new JPasswordField(); estilizarCampo(campoConfirmaSenha); camposPanel.add(campoConfirmaSenha, gbc); gbc.gridwidth = 1;

        gbc.gridy = 10; gbc.weighty = 1.0; camposPanel.add(new JLabel(), gbc);

        JScrollPane scrollForm = new JScrollPane(camposPanel);
        scrollForm.setOpaque(false); scrollForm.getViewport().setOpaque(false); scrollForm.setBorder(BorderFactory.createEmptyBorder());
        painelFormulario.add(scrollForm, BorderLayout.CENTER);

        JPanel painelBotoesForm = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoesForm.setOpaque(false);

        botaoAcaoPrimaria = new JButton("Salvar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        painelBotoesForm.add(botaoAcaoPrimaria);

        botaoAcaoSecundaria = new JButton("Cancelar");
        painelBotoesForm.add(botaoAcaoSecundaria);

        painelFormulario.add(painelBotoesForm, BorderLayout.SOUTH);
    }

    // Helpers de Estilização
    private void estilizarLabel(JLabel label) {
        label.setFont(FONTE_LABEL_FORM);
        label.setForeground(COR_TEXTO_PADRAO);
        label.setOpaque(false);
    }

    private void estilizarCampo(JTextComponent campo) {
        campo.setFont(FONTE_CAMPO_FORM);
        campo.setForeground(COR_TEXTO_CAMPO);
        campo.setBackground(COR_BRANCO);
        campo.setBorder(BORDA_CAMPO_PADRAO);
        campo.setCaretColor(COR_TEXTO_CAMPO);
    }
    
    private void estilizarBotaoPrimario(JButton botao) {
        botao.setBackground(COR_AZUL_PRINCIPAL);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
    }
    
    private void estilizarBotaoVoltar(JButton botao) {
        Color corFundoVoltar = new Color(80, 80, 80); 
        Color corHoverVoltar = new Color(110, 110, 110); 

        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(corFundoVoltar);
        botao.setForeground(Color.WHITE); 

        botao.setOpaque(true); 
        botao.setBorderPainted(false); 
        botao.setFocusPainted(false); 
        botao.setBorder(new EmptyBorder(8, 15, 8, 15)); 
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corHoverVoltar);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corFundoVoltar);
            }
        });
    }
    
    private void estilizarBotaoPadrao(JButton botao) {
        botao.setBackground(COR_BRANCO);
        botao.setForeground(COR_TEXTO_PADRAO);
        botao.setFocusPainted(false);
        botao.setOpaque(true);

        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10) 
        ));
        botao.setBorderPainted(true); 

        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }

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
    }

    private void estilizarBotaoRemover(JButton botao) {
        botao.setBackground(COR_VERMELHO_REMOVER);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        
        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }
    }

    private void atualizarListaFuncionarios() {
        painelListaCards.removeAll();
        
        List<Funcionario> funcionarios = funcionarioDAO.listarTodos();

        for (Funcionario f : funcionarios) {
            painelListaCards.add(criarCardFuncionario(f));
            painelListaCards.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        painelListaCards.revalidate();
        painelListaCards.repaint();
    }

    private JPanel criarCardFuncionario(Funcionario func) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        
        Dimension cardSize = new Dimension(Integer.MAX_VALUE, 130);
        card.setMaximumSize(cardSize);
        card.setMinimumSize(cardSize);
        card.setPreferredSize(cardSize);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel labelFoto = new JLabel();
        labelFoto.setPreferredSize(new Dimension(96, 96));
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
        labelFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        try {
            if (func.getCaminhoFoto() != null && !func.getCaminhoFoto().isEmpty()) {
                ImageIcon icon = new ImageIcon(func.getCaminhoFoto());
                Image img = icon.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH);
                labelFoto.setIcon(new ImageIcon(img));
            } else {
                 labelFoto.setText("Sem Foto");
            }
        } catch (Exception e) {
            labelFoto.setText("Sem Foto");
        }
        card.add(labelFoto, BorderLayout.WEST);

        JPanel painelInfo = new JPanel();
        painelInfo.setOpaque(false);
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel labelNome = new JLabel(func.getNome());
        labelNome.setFont(FONTE_NOME_CARD);
        labelNome.setForeground(COR_AZUL_PRINCIPAL);

        JLabel labelCargo = new JLabel(func.getCargo());
        labelCargo.setFont(FONTE_KPI_TITULO);
        labelCargo.setForeground(COR_TEXTO_PADRAO);
        
        JLabel labelInfo = new JLabel("CPF: " + func.getCpf() + " | Admissão: " + func.getDataAdmissao());
        labelInfo.setFont(FONTE_INFO_CARD);
        labelInfo.setForeground(Color.GRAY);
        
        JLabel labelInfo2 = new JLabel("Nascimento: " + func.getDataNascimento());
        labelInfo2.setFont(FONTE_INFO_CARD);
        labelInfo2.setForeground(Color.GRAY);

        painelInfo.add(labelNome);
        painelInfo.add(Box.createVerticalStrut(5));
        painelInfo.add(labelCargo);
        painelInfo.add(Box.createVerticalStrut(10));
        painelInfo.add(labelInfo);
        painelInfo.add(labelInfo2);

        card.add(painelInfo, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exibirModoEdicao(func);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(2, 2, 2, 2, COR_AZUL_PRINCIPAL),
                        new EmptyBorder(9, 14, 9, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder( 
                        BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                        new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return card;
    }

    private void exibirModoAdicao() {
        funcionarioSelecionadoParaEdicao = null;
        labelTituloForm.setText("Adicionar Novo Funcionário");
        limparCamposFormulario();
        
        botaoAcaoPrimaria.setText("Salvar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        
        botaoAcaoSecundaria.setText("Cancelar");
        
        estilizarBotaoPadrao(botaoAcaoSecundaria);
        
        removerActionListeners(botaoAcaoPrimaria);
        removerActionListeners(botaoAcaoSecundaria);
        
        botaoAcaoPrimaria.addActionListener(e -> salvarFuncionario());
        botaoAcaoSecundaria.addActionListener(e -> voltarAoPlaceholder());
        
        layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
    }

    private void exibirModoEdicao(Funcionario func) {
        funcionarioSelecionadoParaEdicao = func;
        labelTituloForm.setText("Editar: " + func.getNome());
        preencherCamposFormulario(func);
        
        botaoAcaoPrimaria.setText("Atualizar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        
        botaoAcaoSecundaria.setText("Remover");
        
        estilizarBotaoRemover(botaoAcaoSecundaria);
        
        removerActionListeners(botaoAcaoPrimaria);
        removerActionListeners(botaoAcaoSecundaria);
        
        botaoAcaoPrimaria.addActionListener(e -> salvarFuncionario());
        botaoAcaoSecundaria.addActionListener(e -> removerFuncionario(func));
        
        layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
    }
    
    private void removerActionListeners(JButton botao) {
        for (ActionListener al : botao.getActionListeners()) {
            botao.removeActionListener(al);
        }
    }

    private void selecionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione uma foto");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".png") ||
                       f.getName().toLowerCase().endsWith(".jpg") ||
                       f.isDirectory();
            }
            public String getDescription() {
                return "Imagens (PNG, JPG)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            caminhoDaFotoSelecionada = selectedFile.getAbsolutePath();
            
            ImageIcon icon = new ImageIcon(caminhoDaFotoSelecionada);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            labelFotoPreview.setIcon(new ImageIcon(img));
            labelFotoPreview.setText(null);
        }
    }


    private void salvarFuncionario() {
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String dataNasc = campoDataNasc.getText();
        String telefone = campoTelefone.getText(); // Novo
        String email = campoEmail.getText();       // Novo
        String cargo = (String) comboCargo.getSelectedItem();
        String dataAdmissao = campoDataAdmissao.getText();
        String senha = new String(campoSenha.getPassword());
        String confirmaSenha = new String(campoConfirmaSenha.getPassword());
        
        if (nome.isEmpty() || cpf.isEmpty() || dataNasc.isEmpty() || dataAdmissao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (funcionarioSelecionadoParaEdicao == null || !senha.isEmpty()) {
             if (senha.isEmpty() || !senha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas estão vazias ou não conferem.", "Erro de Senha", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
            
        boolean sucesso = false;
        cpf = cpf.replaceAll("[.-]", "");
        
        if (funcionarioSelecionadoParaEdicao == null) {
            // CRIAR NOVO - Agora setamos email e telefone
            Funcionario novoFunc = new Funcionario();
            novoFunc.setNome(nome);
            novoFunc.setCpf(cpf);
            novoFunc.setDataNascimento(dataNasc);
            novoFunc.setTelefone(telefone); // Novo
            novoFunc.setEmail(email);       // Novo
            novoFunc.setCargo(cargo);
            novoFunc.setDataAdmissao(dataAdmissao);
            novoFunc.setSenha(senha);
            novoFunc.setCaminhoFoto(caminhoDaFotoSelecionada);

            sucesso = funcionarioDAO.cadastrar(novoFunc);

        } else {
            // ATUALIZAR EXISTENTE
            funcionarioSelecionadoParaEdicao.setNome(nome);
            funcionarioSelecionadoParaEdicao.setCpf(cpf);
            funcionarioSelecionadoParaEdicao.setDataNascimento(dataNasc);
            funcionarioSelecionadoParaEdicao.setTelefone(telefone); // Novo
            funcionarioSelecionadoParaEdicao.setEmail(email);       // Novo
            funcionarioSelecionadoParaEdicao.setCargo(cargo);
            funcionarioSelecionadoParaEdicao.setDataAdmissao(dataAdmissao);
            
            if (!senha.isEmpty()) {
                funcionarioSelecionadoParaEdicao.setSenha(senha);
            }
            
            if (caminhoDaFotoSelecionada != null) {
                funcionarioSelecionadoParaEdicao.setCaminhoFoto(caminhoDaFotoSelecionada);
            }

            sucesso = funcionarioDAO.atualizar(funcionarioSelecionadoParaEdicao);
        }

        if(sucesso) {
            if(funcionarioSelecionadoParaEdicao == null) {
                JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            }
        
            voltarAoPlaceholder();
            atualizarListaFuncionarios();
        }
    }

    private void removerFuncionario(Funcionario func) {
        int resposta = JOptionPane.showConfirmDialog(
            this, 
            "Tem certeza que deseja remover o funcionário '" + func.getNome() + "'?\nEsta ação não pode ser desfeita.",
            "Confirmação de Remoção", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            funcionarioDAO.remover(func.getId());
            JOptionPane.showMessageDialog(this, "Funcionário removido.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            voltarAoPlaceholder();
            atualizarListaFuncionarios();
        }
    }

    private void preencherCamposFormulario(Funcionario func) {
        campoNome.setText(func.getNome());
        campoCpf.setText(func.getCpf());
        campoDataNasc.setText(func.getDataNascimento());
        campoTelefone.setText(func.getTelefone()); // Novo
        campoEmail.setText(func.getEmail());       // Novo
        campoDataAdmissao.setText(func.getDataAdmissao());
        comboCargo.setSelectedItem(func.getCargo());
        
        campoSenha.setText(func.getSenha());
        campoConfirmaSenha.setText(func.getSenha());
        
        caminhoDaFotoSelecionada = func.getCaminhoFoto();
        // ... resto da lógica da foto ...
        if (caminhoDaFotoSelecionada != null && !caminhoDaFotoSelecionada.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(caminhoDaFotoSelecionada);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                labelFotoPreview.setIcon(new ImageIcon(img));
                labelFotoPreview.setText(null);
            } catch (Exception e) {
                labelFotoPreview.setIcon(null);
                labelFotoPreview.setText("Sem Foto");
            }
        } else {
            labelFotoPreview.setIcon(null);
            labelFotoPreview.setText("Sem Foto");
        }
    }

    private void limparCamposFormulario() {
        campoNome.setText("");
        campoCpf.setText("");
        campoDataNasc.setText("");
        campoTelefone.setText(""); // Novo
        campoEmail.setText("");    // Novo
        campoDataAdmissao.setText("");
        comboCargo.setSelectedIndex(0);
        campoSenha.setText("");
        campoConfirmaSenha.setText("");
        
        labelFotoPreview.setIcon(null);
        labelFotoPreview.setText("Sem Foto");
        caminhoDaFotoSelecionada = null;
    }

    private void voltarAoPlaceholder() {
        layoutLadoDireito.show(painelLadoDireito, PAINEL_PLACEHOLDER);
        funcionarioSelecionadoParaEdicao = null;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("FormattedTextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        
        SwingUtilities.invokeLater(() -> {
            new TelaGestaoFuncionario(null).setVisible(true);
        });
    }
}