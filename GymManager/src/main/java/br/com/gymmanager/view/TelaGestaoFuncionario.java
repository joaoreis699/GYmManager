// O nome do seu pacote
package br.com.gymmanager.view;

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
import java.util.ArrayList;
import java.util.List;

// --- Imports das suas outras classes (ajuste o pacote se necessário) ---
// (Você precisará criar estas classes ou importá-las do pacote correto)
// import br.com.gymmanager.model.Funcionario;
// import br.com.gymmanager.dao.FuncionarioDAO;


/**
 * Tela de Gerenciamento de Funcionários (CRUD).
 * Nome do arquivo: TelaGestaoFuncionario.java
 *
 * * v4.0 - Versão Final com Look and Feel "Nimbus"
 * - Corrigido Look and Feel no 'main' para usar "Nimbus".
 * - Criado 'estilizarBotaoPadrao' (branco com hover azul)
 * - Aplicado estilo aos botões 'Voltar', 'Selecionar' e 'Cancelar'.
 * - Corrigido 'estilizarBotaoRemover' para forçar a cor.
 */
public class TelaGestaoFuncionario extends JFrame { // <--- NOME DA CLASSE ATUALIZADO

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

    // --- Componentes do Lado Direito (Gerenciador de Painéis) ---
    private CardLayout layoutLadoDireito;
    private JPanel painelLadoDireito;
    private JPanel painelFormulario;
    private JPanel painelPlaceholder;
    private static final String PAINEL_FORM = "FORMULARIO";
    private static final String PAINEL_PLACEHOLDER = "PLACEHOLDER";

    // --- Componentes do Formulário ---
    private JLabel labelTituloForm;
    private JTextField campoNome, campoDataNasc, campoDataAdmissao;
    private JFormattedTextField campoCpf;
    private JComboBox<String> comboCargo;
    private JPasswordField campoSenha, campoConfirmaSenha;
    private JButton botaoAcaoPrimaria, botaoAcaoSecundaria, botaoSelecionarFoto;
    private JLabel labelFotoPreview;
    private String caminhoDaFotoSelecionada = null;

    // --- Componentes da Lista (Esquerda) ---
    private JPanel painelListaCards;
    private JScrollPane scrollLista;

    // --- Controle de Estado ---
    private Funcionario funcionarioSelecionadoParaEdicao = null;
    private FuncionarioDAO funcionarioDAO;

    public TelaGestaoFuncionario() { // <--- NOME DA CLASSE ATUALIZADO
        setTitle("GymManager - Gestão de Funcionários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        this.funcionarioDAO = new FuncionarioDAO();

        JPanel painelFundo = new JPanel(new BorderLayout(20, 20));
        painelFundo.setBackground(COR_FUNDO);
        painelFundo.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelFundo);

        // --- 1. CABEÇALHO (HEADER) ---
        painelFundo.add(criarHeader(), BorderLayout.NORTH);

        // --- 2. PAINEL LADO ESQUERDO (LISTA E BOTÃO ADICIONAR) ---
        painelFundo.add(criarPainelLadoEsquerdo(), BorderLayout.CENTER);

        // --- 3. PAINEL LADO DIREITO (FORMULÁRIO DINÂMICO) ---
        painelFundo.add(criarPainelLadoDireito(), BorderLayout.EAST);

        // --- 4. RODAPÉ ---
        JLabel labelRodape = new JLabel("© 2025 GymManager — Todos os direitos reservados", SwingConstants.CENTER);
        labelRodape.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelRodape.setForeground(COR_TEXTO_PADRAO);
        painelFundo.add(labelRodape, BorderLayout.SOUTH);

        // --- Ações Iniciais ---
        atualizarListaFuncionarios();
    }

    /**
     * Cria o cabeçalho padrão.
     * ATUALIZADO: Botão 'Voltar' usa o novo estilo 'estilizarBotaoPadrao'.
     */
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
        } catch (Exception e) {
            System.err.println("Ícone /Imagens/logo.png não encontrado.");
        }

        JLabel labelTitulo = new JLabel("GESTÃO DE FUNCIONÁRIOS");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);
        painelLogoTitulo.add(labelTitulo);

        painelHeader.add(painelLogoTitulo, BorderLayout.WEST);

        JButton botaoVoltar = new JButton("Voltar a Tela Principal");
        
        // --- ESTILO CORRIGIDO ---
        estilizarBotaoVoltar(botaoVoltar); 
        
        botaoVoltar.addActionListener(e -> dispose());
        painelHeader.add(botaoVoltar, BorderLayout.EAST);

        return painelHeader;
    }

    /**
     * Cria o painel da esquerda (Botão Adicionar + Lista).
     * ATUALIZADO: Fundo do Viewport do Scroll corrigido.
     */
    private JPanel criarPainelLadoEsquerdo() {
        JPanel painelCentral = new JPanel(new BorderLayout(0, 10));
        painelCentral.setOpaque(false);

        JButton botaoAdicionar = new JButton("+ Adicionar Novo Funcionário");
        estilizarBotaoPrimario(botaoAdicionar); // Botão principal é azul
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
        
        // --- CORREÇÃO FUNDO PRETO ---
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

    /**
     * Cria o painel da direita com CardLayout (Placeholder e Formulário).
     */
    private JPanel criarPainelLadoDireito() {
        layoutLadoDireito = new CardLayout();
        painelLadoDireito = new JPanel(layoutLadoDireito);
        painelLadoDireito.setOpaque(false);
        painelLadoDireito.setPreferredSize(new Dimension(450, 0)); // Largura fixa

        // 1. Cria o Painel de Formulário
        criarPainelFormulario();

        // 2. Cria o Painel Placeholder (estado inicial)
        painelPlaceholder = new JPanel(new GridBagLayout());
        painelPlaceholder.setBackground(COR_FUNDO);
        JLabel labelPlaceholder = new JLabel("<html><center>Selecione um funcionário à esquerda para ver os detalhes<br>ou<br>Clique em '+ Adicionar Novo Funcionário' para cadastrar.</center></html>");
        labelPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelPlaceholder.setForeground(COR_TEXTO_PADRAO);
        labelPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        painelPlaceholder.add(labelPlaceholder);

        // 3. Adiciona os dois painéis ao CardLayout
        painelLadoDireito.add(painelPlaceholder, PAINEL_PLACEHOLDER);
        painelLadoDireito.add(painelFormulario, PAINEL_FORM);

        // 4. Define o Placeholder como o painel inicial
        layoutLadoDireito.show(painelLadoDireito, PAINEL_PLACEHOLDER);

        return painelLadoDireito;
    }

    /**
     * Apenas CRIA o painelFormulario.
     * ATUALIZADO: Botão 'Selecionar' usa o novo estilo 'estilizarBotaoPadrao'.
     */
    private void criarPainelFormulario() {
        painelFormulario = new JPanel(new BorderLayout(10, 10));
        painelFormulario.setBackground(COR_BRANCO);
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Título do Formulário
        labelTituloForm = new JLabel("Adicionar Novo Funcionário");
        labelTituloForm.setFont(FONTE_TITULO_FORM);
        labelTituloForm.setForeground(COR_AZUL_PRINCIPAL);
        labelTituloForm.setBorder(new EmptyBorder(0, 0, 10, 0));
        painelFormulario.add(labelTituloForm, BorderLayout.NORTH);

        // Painel com os campos
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- LINHA 0: Foto ---
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelFoto = new JLabel("Foto:");
        estilizarLabel(labelFoto);
        camposPanel.add(labelFoto, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        labelFotoPreview = new JLabel();
        labelFotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelFotoPreview.setPreferredSize(new Dimension(80, 80));
        labelFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPreview.setText("Sem Foto");
        camposPanel.add(labelFotoPreview, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        botaoSelecionarFoto = new JButton("Selecionar...");
        
        // --- ESTILO CORRIGIDO ---
        estilizarBotaoPadrao(botaoSelecionarFoto); 
        
        botaoSelecionarFoto.addActionListener(e -> selecionarFoto());
        camposPanel.add(botaoSelecionarFoto, gbc);

        // --- LINHA 1: Nome ---
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel labelNome = new JLabel("Nome:");
        estilizarLabel(labelNome);
        camposPanel.add(labelNome, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        campoNome = new JTextField();
        estilizarCampo(campoNome);
        camposPanel.add(campoNome, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 2: CPF ---
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel labelCpf = new JLabel("CPF (Login):");
        estilizarLabel(labelCpf);
        camposPanel.add(labelCpf, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        try { campoCpf = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
        } catch (ParseException e) { campoCpf = new JFormattedTextField(); }
        estilizarCampo(campoCpf);
        camposPanel.add(campoCpf, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 3: Data Nasc. ---
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel labelNasc = new JLabel("Data Nasc:");
        estilizarLabel(labelNasc);
        camposPanel.add(labelNasc, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        try { campoDataNasc = new JFormattedTextField(new MaskFormatter("##/##/####"));
        } catch (ParseException e) { campoDataNasc = new JTextField(); }
        estilizarCampo(campoDataNasc);
        camposPanel.add(campoDataNasc, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 4: Cargo ---
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel labelCargo = new JLabel("Cargo:");
        estilizarLabel(labelCargo);
        camposPanel.add(labelCargo, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        String[] cargos = {"Recepcionista", "Instrutor", "Gerente"};
        comboCargo = new JComboBox<>(cargos);
        comboCargo.setFont(FONTE_CAMPO_FORM);
        comboCargo.setForeground(COR_TEXTO_CAMPO);
        comboCargo.setBackground(COR_BRANCO);
        camposPanel.add(comboCargo, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 5: Data Admissão ---
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel labelAdm = new JLabel("Admissão:");
        estilizarLabel(labelAdm);
        camposPanel.add(labelAdm, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        try { campoDataAdmissao = new JFormattedTextField(new MaskFormatter("##/##/####"));
        } catch (ParseException e) { campoDataAdmissao = new JTextField(); }
        estilizarCampo(campoDataAdmissao);
        camposPanel.add(campoDataAdmissao, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 6: Senha ---
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel labelSenha = new JLabel("Senha:");
        estilizarLabel(labelSenha);
        camposPanel.add(labelSenha, gbc);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2;
        campoSenha = new JPasswordField();
        estilizarCampo(campoSenha);
        camposPanel.add(campoSenha, gbc);
        gbc.gridwidth = 1;

        // --- LINHA 7: Confirma Senha ---
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel labelConf = new JLabel("Confirmar:");
        estilizarLabel(labelConf);
        camposPanel.add(labelConf, gbc);

        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
        campoConfirmaSenha = new JPasswordField();
        estilizarCampo(campoConfirmaSenha);
        camposPanel.add(campoConfirmaSenha, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 8; gbc.weighty = 1.0;
        camposPanel.add(new JLabel(), gbc);

        JScrollPane scrollForm = new JScrollPane(camposPanel);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setOpaque(false);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        painelFormulario.add(scrollForm, BorderLayout.CENTER);

        // Painel de Botões Dinâmicos
        JPanel painelBotoesForm = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoesForm.setOpaque(false);

        botaoAcaoPrimaria = new JButton("Salvar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        painelBotoesForm.add(botaoAcaoPrimaria);

        botaoAcaoSecundaria = new JButton("Cancelar");
        // O estilo será definido dinamicamente
        painelBotoesForm.add(botaoAcaoSecundaria);

        painelFormulario.add(painelBotoesForm, BorderLayout.SOUTH);
    }

    // --- Helpers de Estilização (Correção Dark Mode) ---

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
    
    /**
     * NOVO MÉTODO: Estilo exclusivo para o botão "Voltar".
     * (Design "Link" - sem fundo, sem borda, sublinhado no hover)
     */
    /**
     * NOVO MÉTODO: Estilo exclusivo para o botão "Voltar".
     * (Design "Flat/Flutuante" - cor cinza escuro, sem borda)
     */
    private void estilizarBotaoVoltar(JButton botao) {
        // Define a cor "preta bem clara" (cinza escuro)
        Color corFundoVoltar = new Color(80, 80, 80); // <= COR_TEXTO_PADRAO
        // Define uma cor de hover (um cinza um pouco mais claro)
        Color corHoverVoltar = new Color(110, 110, 110); 

        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(corFundoVoltar);
        botao.setForeground(Color.WHITE); // Texto branco para contrastar

        // --- Remove a borda e força o estilo "flat" ---
        botao.setOpaque(true); // Essencial para o setBackground funcionar
        botao.setBorderPainted(false); // Remove a borda
        botao.setFocusPainted(false); // Remove a borda de foco
        
        // Adiciona um padding (espaçamento interno) para dar tamanho
        botao.setBorder(new EmptyBorder(8, 15, 8, 15)); // (topo/baixo, esq/dir)
        
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Remove listeners de hover antigos
        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }

        // Efeito Hover (Muda para a cor mais clara)
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
    
    /**
     * NOVO MÉTODO DE ESTILO: Padrão (Branco/Azul Hover)
     * Usado para 'Voltar', 'Selecionar' e 'Cancelar'.
     */
    private void estilizarBotaoPadrao(JButton botao) {
        botao.setBackground(COR_BRANCO);
        botao.setForeground(COR_TEXTO_PADRAO);
        botao.setFocusPainted(false);
        botao.setOpaque(true); // Força a cor de fundo

        // Borda padrão igual da TelaPrincipal
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(5, 10, 5, 10) // Padding
        ));
        botao.setBorderPainted(true); // Garante que a borda seja pintada

        // Remove listeners antigos para evitar duplicação
        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }

        // Efeito Hover
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
    
    /**
     * ATUALIZADO: Botão 'Remover' (Vermelho)
     */
    private void estilizarBotaoRemover(JButton botao) {
        botao.setBackground(COR_VERMELHO_REMOVER);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        
        // --- ADIÇÃO PARA CORRIGIR COR PRETA (DARK MODE) ---
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        // --- FIM DA ADIÇÃO ---
        
        // Remove listeners de hover do estilo padrão
        for (java.awt.event.MouseListener ml : botao.getMouseListeners()) {
            if (ml instanceof java.awt.event.MouseAdapter) {
                botao.removeMouseListener(ml);
            }
        }
    }


    /**
     * Carrega a lista de funcionários do DAO e recria os cards.
     */
    private void atualizarListaFuncionarios() {
        painelListaCards.removeAll();
        // Você precisa ter a classe FuncionarioDAO importada ou no mesmo pacote
        List<Funcionario> funcionarios = funcionarioDAO.listarTodos();

        for (Funcionario f : funcionarios) {
            painelListaCards.add(criarCardFuncionario(f));
            painelListaCards.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        painelListaCards.revalidate();
        painelListaCards.repaint();
    }

    /**
     * Método Fábrica para criar um card de funcionário (SEM botões).
     * Adiciona um MouseListener para ativar o modo de edição.
     */
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

        // --- LADO ESQUERDO: FOTO ---
        JLabel labelFoto = new JLabel();
        labelFoto.setPreferredSize(new Dimension(96, 96));
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
        labelFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        try {
            // Garante que o caminho da foto não seja nulo
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

        // --- CENTRO: INFORMAÇÕES ---
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

        // --- LÓGICA DE CLIQUE NO CARD ---
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exibirModoEdicao(func);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(2, 2, 2, 2, COR_AZUL_PRINCIPAL), // Borda azul
                        new EmptyBorder(9, 14, 9, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder( // Borda padrão
                        BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
                        new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });

        return card;
    }

    // --- MÉTODOS DE CONTROLE DE FLUXO DA UI ---

    /**
     * Alterna para o painel de formulário em modo de ADIÇÃO.
     * ATUALIZADO: 'Cancelar' usa o novo estilo 'estilizarBotaoPadrao'.
     */
    private void exibirModoAdicao() {
        funcionarioSelecionadoParaEdicao = null;
        labelTituloForm.setText("Adicionar Novo Funcionário");
        limparCamposFormulario();
        
        // Configura botões para "Salvar" e "Cancelar"
        botaoAcaoPrimaria.setText("Salvar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        
        botaoAcaoSecundaria.setText("Cancelar");
        
        // --- ESTILO CORRIGIDO ---
        estilizarBotaoPadrao(botaoAcaoSecundaria);
        
        removerActionListeners(botaoAcaoPrimaria);
        removerActionListeners(botaoAcaoSecundaria);
        
        botaoAcaoPrimaria.addActionListener(e -> salvarFuncionario());
        botaoAcaoSecundaria.addActionListener(e -> voltarAoPlaceholder());
        
        layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
    }

    /**
     * Alterna para o painel de formulário em modo de EDIÇÃO.
     * ATUALIZADO: 'Remover' usa o estilo 'estilizarBotaoRemover'.
     */
    private void exibirModoEdicao(Funcionario func) {
        funcionarioSelecionadoParaEdicao = func;
        labelTituloForm.setText("Editar: " + func.getNome());
        preencherCamposFormulario(func);
        
        // Configura botões para "Atualizar" e "Remover"
        botaoAcaoPrimaria.setText("Atualizar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        
        botaoAcaoSecundaria.setText("Remover");
        
        // --- ESTILO CORRIGIDO ---
        estilizarBotaoRemover(botaoAcaoSecundaria);
        
        removerActionListeners(botaoAcaoPrimaria);
        removerActionListeners(botaoAcaoSecundaria);
        
        botaoAcaoPrimaria.addActionListener(e -> salvarFuncionario());
        botaoAcaoSecundaria.addActionListener(e -> removerFuncionario(func));
        
        layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
    }
    
    /**
     * Helper para limpar todos os ActionListeners de um botão.
     */
    private void removerActionListeners(JButton botao) {
        for (ActionListener al : botao.getActionListeners()) {
            botao.removeActionListener(al);
        }
    }

    /**
     * Ação do botão "Selecionar Foto...".
     */
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

    /**
     * Ação do botão "Salvar" ou "Atualizar".
     */
    private void salvarFuncionario() {
        // 1. Coletar Dados
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String dataNasc = campoDataNasc.getText();
        String cargo = (String) comboCargo.getSelectedItem();
        String dataAdmissao = campoDataAdmissao.getText();
        String senha = new String(campoSenha.getPassword());
        String confirmaSenha = new String(campoConfirmaSenha.getPassword());
        
        // 2. Validação
        if (nome.isEmpty() || cpf.isEmpty() || dataNasc.isEmpty() || dataAdmissao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se for um novo funcionário OU se a senha foi alterada (não está vazia)
        if (funcionarioSelecionadoParaEdicao == null || !senha.isEmpty()) {
             if (senha.isEmpty() || !senha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas estão vazias ou não conferem.", "Erro de Senha", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // 3. Salvar (Criar ou Editar)
        if (funcionarioSelecionadoParaEdicao == null) {
            // --- CRIAR NOVO ---
            Funcionario novoFunc = new Funcionario();
            novoFunc.setId(0); // DAO simula auto-incremento
            novoFunc.setNome(nome);
            novoFunc.setCpf(cpf);
            novoFunc.setDataNascimento(dataNasc);
            novoFunc.setCargo(cargo);
            novoFunc.setDataAdmissao(dataAdmissao);
            novoFunc.setSenha(senha); // Em um sistema real, use Hash!
            novoFunc.setCaminhoFoto(caminhoDaFotoSelecionada);

            funcionarioDAO.cadastrar(novoFunc);
            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } else {
            // --- EDITAR EXISTENTE ---
            funcionarioSelecionadoParaEdicao.setNome(nome);
            funcionarioSelecionadoParaEdicao.setCpf(cpf);
            funcionarioSelecionadoParaEdicao.setDataNascimento(dataNasc);
            funcionarioSelecionadoParaEdicao.setCargo(cargo);
            funcionarioSelecionadoParaEdicao.setDataAdmissao(dataAdmissao);
            
            // Só atualiza a senha se o campo não estiver vazio
            if (!senha.isEmpty()) {
                funcionarioSelecionadoParaEdicao.setSenha(senha);
            }
            
            // Só atualiza a foto se uma nova foi selecionada
            if (caminhoDaFotoSelecionada != null) {
                funcionarioSelecionadoParaEdicao.setCaminhoFoto(caminhoDaFotoSelecionada);
            }

            funcionarioDAO.atualizar(funcionarioSelecionadoParaEdicao);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }

        // 4. Limpar e Atualizar
        voltarAoPlaceholder();
        atualizarListaFuncionarios();
    }

    /**
     * Ação do botão "Remover" do formulário.
     */
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
    
    /**
     * Preenche o formulário com dados de um funcionário (Modo Edição).
     */
    private void preencherCamposFormulario(Funcionario func) {
        campoNome.setText(func.getNome());
        campoCpf.setText(func.getCpf());
        campoDataNasc.setText(func.getDataNascimento());
        campoDataAdmissao.setText(func.getDataAdmissao());
        comboCargo.setSelectedItem(func.getCargo());
        
        // Carrega a senha para ser modificada
        campoSenha.setText(func.getSenha());
        campoConfirmaSenha.setText(func.getSenha());
        
        caminhoDaFotoSelecionada = func.getCaminhoFoto();
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

    /**
     * Limpa os campos do formulário (Modo Adição).
     */
    private void limparCamposFormulario() {
        campoNome.setText("");
        campoCpf.setText("");
        campoDataNasc.setText("");
        campoDataAdmissao.setText("");
        comboCargo.setSelectedIndex(0);
        campoSenha.setText("");
        campoConfirmaSenha.setText("");
        
        labelFotoPreview.setIcon(null);
        labelFotoPreview.setText("Sem Foto");
        caminhoDaFotoSelecionada = null;
    }
    
    /**
     * Retorna a UI para o estado inicial (painel cinza).
     */
    private void voltarAoPlaceholder() {
        layoutLadoDireito.show(painelLadoDireito, PAINEL_PLACEHOLDER);
        funcionarioSelecionadoParaEdicao = null;
    }


    /**
     * Método main para testar esta tela isoladamente.
     * *** VERSÃO CORRIGIDA COM LOOK AND FEEL "NIMBUS" ***
     */
    public static void main(String[] args) {
        try {
            // Tenta definir o Look and Feel "Nimbus"
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se Nimbus não estiver disponível, usa o padrão do Java (Metal)
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Garante que todos os campos de texto sejam brancos
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("FormattedTextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        
        SwingUtilities.invokeLater(() -> {
            new TelaGestaoFuncionario().setVisible(true); // <--- NOME DA CLASSE ATUALIZADO
        });
    }
}

/**
 * POJO (Modelo) que representa um Funcionário.
 * (Certifique-se de que esta classe esteja no pacote br.com.gymmanager.model)
 */
class Funcionario {
    private int id;
    private String nome;
    private String cpf;
    private String dataNascimento; // Simplificado como String
    private String cargo;
    private String dataAdmissao; // Simplificado como String
    private String senha;
    private String caminhoFoto;

    // Getters e Setters (Necessários para o código funcionar)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCaminhoFoto() { return caminhoFoto; }
    public void setCaminhoFoto(String caminhoFoto) { this.caminhoFoto = caminhoFoto; }
}

/**
 * DAO (Data Access Object) simulado para Funcionário.
 * (Certifique-se de que esta classe esteja no pacote br.com.gymmanager.dao)
 */
class FuncionarioDAO {
    private static List<Funcionario> bancoDeDadosSimulado = new ArrayList<>();
    private static int proximoId = 1;

    // Adiciona alguns dados de exemplo
    static {
        Funcionario f1 = new Funcionario();
        f1.setId(proximoId++);
        f1.setNome("Ana Silva (Exemplo)");
        f1.setCpf("111.111.111-11");
        f1.setCargo("Recepcionista");
        f1.setDataAdmissao("01/03/2024");
        f1.setDataNascimento("15/05/1998");
        f1.setSenha("123");
        f1.setCaminhoFoto(null); // Sem foto
        bancoDeDadosSimulado.add(f1);
        
        Funcionario f2 = new Funcionario();
        f2.setId(proximoId++);
        f2.setNome("Bruno Costa (Exemplo)");
        f2.setCpf("222.222.222-22");
        f2.setCargo("Instrutor");
        f2.setDataAdmissao("10/01/2023");
        f2.setDataNascimento("20/11/1995");
        f2.setSenha("abc");
        f2.setCaminhoFoto(null); // Sem foto
        bancoDeDadosSimulado.add(f2);
    }

    public List<Funcionario> listarTodos() {
        return new ArrayList<>(bancoDeDadosSimulado); // Retorna uma cópia
    }

    public void cadastrar(Funcionario f) {
        f.setId(proximoId++);
        bancoDeDadosSimulado.add(f);
    }

    public void atualizar(Funcionario f) {
        for (int i = 0; i < bancoDeDadosSimulado.size(); i++) {
            if (bancoDeDadosSimulado.get(i).getId() == f.getId()) {
                bancoDeDadosSimulado.set(i, f);
                return;
            }
        }
    }

    public void remover(int id) {
        bancoDeDadosSimulado.removeIf(f -> f.getId() == id);
    }
}