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
import br.com.gymmanager.dao.PlanoDAO;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Plano;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaGestaoAlunos extends JDialog {

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

    // --- Componentes ---
    private CardLayout layoutLadoDireito;
    private JPanel painelLadoDireito, painelFormulario, painelPlaceholder, painelListaCards;
    private JScrollPane scrollLista;

    // Campos do Formulário
    private JLabel labelTituloForm, labelFotoPreview;
    private JTextField campoNome, campoEmail;
    private JFormattedTextField campoCpf, campoDataNasc, campoTelefone, campoDataMatricula;
    private JComboBox<Plano> comboPlano; // ComboBox Especial para Planos
    private JComboBox<String> comboStatus;
    private JButton botaoAcaoPrimaria, botaoAcaoSecundaria, botaoSelecionarFoto;
    
    private String caminhoDaFotoSelecionada = null;

    // DAOs e Estado
    private Aluno alunoSelecionadoParaEdicao = null;
    private AlunoDAO alunoDAO;
    private PlanoDAO planoDAO;

    // Construtor
    public TelaGestaoAlunos(JFrame parent) {
        super(parent, "GymManager - Gestão de Alunos", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        this.alunoDAO = new AlunoDAO();
        this.planoDAO = new PlanoDAO(); // Necessário para listar os planos

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

        atualizarListaAlunos();
    }

    // --- HEADER ---
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

        JLabel labelTitulo = new JLabel("GESTÃO DE ALUNOS");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);
        painelLogoTitulo.add(labelTitulo);

        painelHeader.add(painelLogoTitulo, BorderLayout.WEST);

        JButton botaoVoltar = new JButton("Voltar para tela principal");
        estilizarBotaoVoltar(botaoVoltar);
        botaoVoltar.addActionListener(e -> dispose());
        painelHeader.add(botaoVoltar, BorderLayout.EAST);

        return painelHeader;
    }

    // --- PAINEL ESQUERDO (LISTA) ---
    private JPanel criarPainelLadoEsquerdo() {
        JPanel painelCentral = new JPanel(new BorderLayout(0, 10));
        painelCentral.setOpaque(false);

        JButton botaoAdicionar = new JButton("+ Adicionar Novo Aluno");
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
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);

        JPanel painelContainerLista = new JPanel(new BorderLayout());
        painelContainerLista.setOpaque(false);
        painelContainerLista.add(scrollLista, BorderLayout.CENTER);
        painelContainerLista.setBorder(BorderFactory.createTitledBorder("Alunos Cadastrados"));

        painelCentral.add(painelContainerLista, BorderLayout.CENTER);
        return painelCentral;
    }

    // --- PAINEL DIREITO (CARD LAYOUT) ---
    private JPanel criarPainelLadoDireito() {
        layoutLadoDireito = new CardLayout();
        painelLadoDireito = new JPanel(layoutLadoDireito);
        painelLadoDireito.setOpaque(false);
        painelLadoDireito.setPreferredSize(new Dimension(450, 0));

        criarPainelFormulario();

        painelPlaceholder = new JPanel(new GridBagLayout());
        painelPlaceholder.setBackground(COR_FUNDO);
        JLabel labelPlaceholder = new JLabel("<html><center>Selecione um aluno para editar<br>ou clique em '+ Adicionar'.</center></html>");
        labelPlaceholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelPlaceholder.setForeground(COR_TEXTO_PADRAO);
        painelPlaceholder.add(labelPlaceholder);

        painelLadoDireito.add(painelPlaceholder, "PLACEHOLDER");
        painelLadoDireito.add(painelFormulario, "FORMULARIO");
        layoutLadoDireito.show(painelLadoDireito, "PLACEHOLDER");

        return painelLadoDireito;
    }

    // --- FORMULÁRIO ---
    private void criarPainelFormulario() {
        painelFormulario = new JPanel(new BorderLayout(10, 10));
        painelFormulario.setBackground(COR_BRANCO);
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
        new EmptyBorder(15, 20, 15, 20)));

        labelTituloForm = new JLabel("Adicionar Novo Aluno");
        labelTituloForm.setFont(FONTE_TITULO_FORM);
        labelTituloForm.setForeground(COR_AZUL_PRINCIPAL);
        painelFormulario.add(labelTituloForm, BorderLayout.NORTH);

        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // FOTO
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelFoto = new JLabel("Foto:");
        estilizarLabel(labelFoto);
        camposPanel.add(labelFoto, gbc);

        gbc.gridx = 1; labelFotoPreview = new JLabel("Sem Foto");
        labelFotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        labelFotoPreview.setPreferredSize(new Dimension(80, 80));
        labelFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
        camposPanel.add(labelFotoPreview, gbc);

        gbc.gridx = 2; botaoSelecionarFoto = new JButton("Selecionar...");
        estilizarBotaoPadrao(botaoSelecionarFoto);
        botaoSelecionarFoto.addActionListener(e -> selecionarFoto());
        camposPanel.add(botaoSelecionarFoto, gbc);

        // NOME
        addCampo(camposPanel, gbc, 1, "Nome:", campoNome = new JTextField());
        
        // CPF
        try { campoCpf = new JFormattedTextField(new MaskFormatter("###.###.###-##")); } catch (Exception e) {}
        addCampo(camposPanel, gbc, 2, "CPF:", campoCpf);

        // DATA NASC
        try { campoDataNasc = new JFormattedTextField(new MaskFormatter("##/##/####")); } catch (Exception e) {}
        addCampo(camposPanel, gbc, 3, "Data Nasc:", campoDataNasc);

        // TELEFONE
        try { campoTelefone = new JFormattedTextField(new MaskFormatter("(##) #####-####")); } catch (Exception e) {}
        addCampo(camposPanel, gbc, 4, "Telefone:", campoTelefone);

        // EMAIL
        addCampo(camposPanel, gbc, 5, "Email:", campoEmail = new JTextField());

        // --- COMBOBOX DE PLANOS ---
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel labelPlano = new JLabel("Plano:");
        estilizarLabel(labelPlano);
        camposPanel.add(labelPlano, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        comboPlano = new JComboBox<>(); // Será preenchido dinamicamente
        comboPlano.setFont(FONTE_CAMPO_FORM);
        comboPlano.setBackground(COR_BRANCO);
        camposPanel.add(comboPlano, gbc);
        gbc.gridwidth = 1; // Reseta

        // STATUS
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel labelStatus = new JLabel("Status:");
        estilizarLabel(labelStatus);
        camposPanel.add(labelStatus, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Pendente"});
        comboStatus.setFont(FONTE_CAMPO_FORM);
        comboStatus.setBackground(COR_BRANCO);
        camposPanel.add(comboStatus, gbc);
        gbc.gridwidth = 1;

        // DATA MATRÍCULA
        try { campoDataMatricula = new JFormattedTextField(new MaskFormatter("##/##/####")); } catch (Exception e) {}
        // Preenche com a data de hoje por padrão
        campoDataMatricula.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        addCampo(camposPanel, gbc, 8, "Matrícula:", campoDataMatricula);

        gbc.gridy = 9; gbc.weighty = 1.0;
        camposPanel.add(new JLabel(), gbc); // Espaçador

        JScrollPane scrollForm = new JScrollPane(camposPanel);
        scrollForm.setOpaque(false); scrollForm.getViewport().setOpaque(false);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        painelFormulario.add(scrollForm, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        painelBotoes.setOpaque(false);
        botaoAcaoPrimaria = new JButton("Salvar"); estilizarBotaoPrimario(botaoAcaoPrimaria);
        botaoAcaoSecundaria = new JButton("Cancelar"); estilizarBotaoPadrao(botaoAcaoSecundaria);
        painelBotoes.add(botaoAcaoPrimaria); painelBotoes.add(botaoAcaoSecundaria);
        painelFormulario.add(painelBotoes, BorderLayout.SOUTH);
    }

    // --- LÓGICA DE DADOS ---

    private void preencherComboPlanos() {
        comboPlano.removeAllItems();
        List<Plano> planos = planoDAO.listarTodos();
        for (Plano p : planos) {
            comboPlano.addItem(p); // O toString() do Plano mostrará o nome
        }
    }

    private void salvarAluno() {
        // 1. Coletar Dados
        String nome = campoNome.getText();
        String cpfFormatado = campoCpf.getText();
        String cpfLimpo = cpfFormatado.replaceAll("[^0-9]", ""); // Só números
        String dataNasc = campoDataNasc.getText();
        String telefone = campoTelefone.getText();
        String email = campoEmail.getText();
        Plano planoSelecionado = (Plano) comboPlano.getSelectedItem();
        String status = (String) comboStatus.getSelectedItem();
        String dataMatricula = campoDataMatricula.getText();

        // 2. Validação
        if (nome.isEmpty() || cpfLimpo.length() != 11 || dataNasc.trim().length() < 10) {
            JOptionPane.showMessageDialog(this, "Preencha Nome, CPF e Data de Nascimento corretamente.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (planoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Plano.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. LÓGICA DA SENHA (4 últimos dígitos do CPF)
        String senhaGerada = "";
        if (cpfLimpo.length() >= 4) {
            senhaGerada = cpfLimpo.substring(cpfLimpo.length() - 4);
        } else {
            senhaGerada = cpfLimpo;
        }

        boolean sucesso = false;

        if (alunoSelecionadoParaEdicao == null) {
            // CADASTRO
            Aluno novoAluno = new Aluno();
            novoAluno.setNome(nome);
            novoAluno.setCpf(cpfLimpo);
            novoAluno.setDataNascimento(dataNasc);
            novoAluno.setTelefone(telefone);
            novoAluno.setEmail(email);
            novoAluno.setPlano(planoSelecionado);
            novoAluno.setStatus(status);
            novoAluno.setDataMatricula(dataMatricula);
            novoAluno.setSenha(senhaGerada); // Senha Automática
            novoAluno.setCaminhoFoto(caminhoDaFotoSelecionada);

            sucesso = alunoDAO.cadastrar(novoAluno);
        } else {
            // EDIÇÃO (Implementar no AlunoDAO depois)
            JOptionPane.showMessageDialog(this, "Edição ainda não implementada no DAO.");
        }

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!\nSenha inicial: " + senhaGerada);
            voltarAoPlaceholder();
            atualizarListaAlunos();
        }
    }

    private void atualizarListaAlunos() {
        painelListaCards.removeAll();
        List<Aluno> alunos = alunoDAO.listarTodos();
        for (Aluno a : alunos) {
            painelListaCards.add(criarCardAluno(a));
            painelListaCards.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        painelListaCards.revalidate(); painelListaCards.repaint();
    }

    private JPanel criarCardAluno(Aluno a) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
        new EmptyBorder(10, 15, 10, 15)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Info do Card
        JPanel info = new JPanel(); info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        
        JLabel lNome = new JLabel(a.getNome()); lNome.setFont(FONTE_NOME_CARD); lNome.setForeground(COR_AZUL_PRINCIPAL);
        
        // Mostra o Plano e o Status
        String nomePlano = (a.getPlano() != null) ? a.getPlano().getNome() : "Sem Plano";
        JLabel lPlano = new JLabel(nomePlano + " - " + a.getStatus()); 
        lPlano.setFont(FONTE_KPI_TITULO); lPlano.setForeground(COR_TEXTO_PADRAO);
        
        JLabel lCpf = new JLabel("CPF: " + a.getCpf()); lCpf.setFont(FONTE_INFO_CARD); lCpf.setForeground(Color.GRAY);

        info.add(lNome); info.add(Box.createVerticalStrut(5));
        info.add(lPlano); info.add(Box.createVerticalStrut(5));
        info.add(lCpf);
        
        card.add(info, BorderLayout.CENTER);
        
        // Ação de clique (Exibir Edição)
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exibirModoEdicao(a);
            }
        });
        return card;
    }

    // --- CONTROLE DE FLUXO ---
    private void exibirModoAdicao() {
        alunoSelecionadoParaEdicao = null;
        labelTituloForm.setText("Adicionar Novo Aluno");
        limparCampos();
        preencherComboPlanos(); // Atualiza a lista de planos ao abrir
        
        botaoAcaoPrimaria.setText("Salvar");
        botaoAcaoSecundaria.setText("Cancelar"); estilizarBotaoPadrao(botaoAcaoSecundaria);
        
        limparListeners(botaoAcaoPrimaria); limparListeners(botaoAcaoSecundaria);
        botaoAcaoPrimaria.addActionListener(e -> salvarAluno());
        botaoAcaoSecundaria.addActionListener(e -> voltarAoPlaceholder());
        
        layoutLadoDireito.show(painelLadoDireito, "FORMULARIO");
    }
    
    private void exibirModoEdicao(Aluno a) {
        alunoSelecionadoParaEdicao = a;
        labelTituloForm.setText("Editar: " + a.getNome());
        preencherComboPlanos();
        preencherCampos(a);
        
        botaoAcaoPrimaria.setText("Atualizar");
        botaoAcaoSecundaria.setText("Remover"); estilizarBotaoRemover(botaoAcaoSecundaria);
        
        limparListeners(botaoAcaoPrimaria); limparListeners(botaoAcaoSecundaria);
        botaoAcaoPrimaria.addActionListener(e -> salvarAluno());
        // botaoAcaoSecundaria.addActionListener(e -> removerAluno(a)); // Implementar depois
        
        layoutLadoDireito.show(painelLadoDireito, "FORMULARIO");
    }

    private void limparCampos() {
        campoNome.setText(""); campoCpf.setText(""); campoDataNasc.setText("");
        campoTelefone.setText(""); campoEmail.setText(""); caminhoDaFotoSelecionada = null;
        labelFotoPreview.setIcon(null); labelFotoPreview.setText("Sem Foto");
    }
    
    private void preencherCampos(Aluno a) {
        campoNome.setText(a.getNome()); campoCpf.setText(a.getCpf()); campoDataNasc.setText(a.getDataNascimento());
        campoTelefone.setText(a.getTelefone()); campoEmail.setText(a.getEmail());
        comboStatus.setSelectedItem(a.getStatus());
        campoDataMatricula.setText(a.getDataMatricula());
        
        // Selecionar o plano correto no ComboBox
        if (a.getPlano() != null) {
            for (int i = 0; i < comboPlano.getItemCount(); i++) {
                Plano p = comboPlano.getItemAt(i);
                if (p.getId() == a.getPlano().getId()) {
                    comboPlano.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Foto...
    }

    private void voltarAoPlaceholder() {
        layoutLadoDireito.show(painelLadoDireito, "PLACEHOLDER");
        alunoSelecionadoParaEdicao = null;
    }
    
    // --- HELPERS DE ESTILO E UTILITÁRIOS ---
    private void addCampo(JPanel p, GridBagConstraints gbc, int y, String label, Component c) {
        gbc.gridx = 0; gbc.gridy = y;
        JLabel l = new JLabel(label); estilizarLabel(l); p.add(l, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        if(c instanceof JTextField) estilizarCampo((JTextField)c);
        p.add(c, gbc); gbc.gridwidth = 1;
    }
    private void estilizarLabel(JLabel l) { l.setFont(FONTE_LABEL_FORM); l.setForeground(COR_TEXTO_PADRAO); }
    private void estilizarCampo(JTextField c) { c.setFont(FONTE_CAMPO_FORM); c.setForeground(COR_TEXTO_CAMPO); c.setBackground(COR_BRANCO); c.setBorder(BORDA_CAMPO_PADRAO); }
    private void estilizarBotaoPrimario(JButton b) { b.setBackground(COR_AZUL_PRINCIPAL); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setOpaque(true); b.setBorderPainted(false); }
    private void estilizarBotaoPadrao(JButton b) { b.setBackground(COR_BRANCO); b.setForeground(COR_TEXTO_PADRAO); b.setFocusPainted(false); b.setOpaque(true); b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(200,200,200)), new EmptyBorder(5,10,5,10))); b.setBorderPainted(true); }
    private void estilizarBotaoVoltar(JButton b) { b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.setBackground(new Color(80,80,80)); b.setForeground(Color.WHITE); b.setOpaque(true); b.setBorderPainted(false); b.setFocusPainted(false); b.setBorder(new EmptyBorder(8, 15, 8, 15)); }
    private void estilizarBotaoRemover(JButton b) { b.setBackground(COR_VERMELHO_REMOVER); b.setForeground(Color.WHITE); b.setOpaque(true); b.setBorderPainted(false); }
    private void limparListeners(JButton b) { for(ActionListener al: b.getActionListeners()) b.removeActionListener(al); }
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
    // Main para teste
    public static void main(String[] args) {
        try { for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; } } catch (Exception e) {}
        UIManager.put("TextField.background", Color.WHITE);
        SwingUtilities.invokeLater(() -> new TelaGestaoAlunos(null).setVisible(true));
    }
}
