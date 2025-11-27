/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.dao.PlanoDAO;
import br.com.gymmanager.model.Plano;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaGestaoPlanos extends JDialog {

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
    private static final Font FONTE_INFO_CARD = new Font("Segoe UI", Font.PLAIN, 14);

    private static final Border BORDA_CAMPO_PADRAO = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
            new EmptyBorder(5, 5, 5, 5)
    );

    private CardLayout layoutLadoDireito;
    private JPanel painelLadoDireito, painelFormulario, painelPlaceholder, painelListaCards;
    private JScrollPane scrollLista;

    private JLabel labelTituloForm;
    private JTextField campoNome, campoValor, campoDuracao;
    private JButton botaoAcaoPrimaria, botaoAcaoSecundaria;

    private Plano planoSelecionado = null;
    private PlanoDAO planoDAO;

    private static final String PAINEL_FORM = "FORMULARIO";
    private static final String PAINEL_PLACEHOLDER = "PLACEHOLDER";

    public TelaGestaoPlanos(JFrame parent) {
        super(parent, "GymManager - Gestão de Planos", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        this.planoDAO = new PlanoDAO();

        JPanel painelFundo = new JPanel(new BorderLayout(20, 20));
        painelFundo.setBackground(COR_FUNDO);
        painelFundo.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelFundo);

        painelFundo.add(criarHeader(), BorderLayout.NORTH);
        painelFundo.add(criarPainelLadoEsquerdo(), BorderLayout.CENTER);
        painelFundo.add(criarPainelLadoDireito(), BorderLayout.EAST);

        atualizarLista();
    }

    private JPanel criarHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COR_FUNDO);

        JLabel labelTitulo = new JLabel("GESTÃO DE PLANOS");
        labelTitulo.setFont(FONTE_TITULO_LOGO);
        labelTitulo.setForeground(COR_AZUL_PRINCIPAL);
        p.add(labelTitulo, BorderLayout.WEST);

        JButton botaoVoltar = new JButton("Voltar a Tela Principal");
        
        estilizarBotaoVoltar(botaoVoltar);
        botaoVoltar.addActionListener(e -> dispose());
        
        p.add(botaoVoltar, BorderLayout.EAST);

        return p;
    }

    private JPanel criarPainelLadoEsquerdo() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);

        JButton botaoAdicionar = new JButton("+ Criar Novo Plano");
        estilizarBotaoPrimario(botaoAdicionar);
        botaoAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botaoAdicionar.setPreferredSize(new Dimension(0, 40));
        botaoAdicionar.addActionListener(e -> exibirModoAdicao());
        p.add(botaoAdicionar, BorderLayout.NORTH);

        painelListaCards = new JPanel();
        painelListaCards.setBackground(COR_FUNDO);
        painelListaCards.setLayout(new BoxLayout(painelListaCards, BoxLayout.Y_AXIS));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(painelListaCards, BorderLayout.NORTH);

        scrollLista = new JScrollPane(wrapper);
        scrollLista.getViewport().setBackground(COR_FUNDO);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        scrollLista.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(scrollLista, BorderLayout.CENTER);
        container.setBorder(BorderFactory.createTitledBorder("Planos Ativos"));

        p.add(container, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarPainelLadoDireito() {
        layoutLadoDireito = new CardLayout();
        painelLadoDireito = new JPanel(layoutLadoDireito);
        painelLadoDireito.setOpaque(false);
        painelLadoDireito.setPreferredSize(new Dimension(400, 0));

        criarPainelFormulario();

        painelPlaceholder = new JPanel(new GridBagLayout());
        painelPlaceholder.setBackground(COR_FUNDO);
        JLabel lbl = new JLabel("<html><center>Selecione um plano para editar<br>ou crie um novo.</center></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(COR_TEXTO_PADRAO);
        painelPlaceholder.add(lbl);

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
                new EmptyBorder(15, 20, 15, 20)));

        labelTituloForm = new JLabel("Novo Plano");
        labelTituloForm.setFont(FONTE_TITULO_FORM);
        labelTituloForm.setForeground(COR_AZUL_PRINCIPAL);
        painelFormulario.add(labelTituloForm, BorderLayout.NORTH);

        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Nome
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lNome = new JLabel("Nome do Plano:"); estilizarLabel(lNome); camposPanel.add(lNome, gbc);
        
        gbc.gridy = 1;
        campoNome = new JTextField(); estilizarCampo(campoNome); camposPanel.add(campoNome, gbc);

        gbc.gridy = 2;
        JLabel lValor = new JLabel("Valor (R$):"); estilizarLabel(lValor); camposPanel.add(lValor, gbc);
        
        gbc.gridy = 3;
        campoValor = new JTextField(); estilizarCampo(campoValor); 
        campoValor.setToolTipText("Use ponto ou vírgula (ex: 99.90)");
        camposPanel.add(campoValor, gbc);

        gbc.gridy = 4;
        JLabel lDuracao = new JLabel("Duração (meses):"); estilizarLabel(lDuracao); camposPanel.add(lDuracao, gbc);

        gbc.gridy = 5;
        campoDuracao = new JTextField(); estilizarCampo(campoDuracao); 
        campoDuracao.setToolTipText("Ex: 1, 3, 12");
        camposPanel.add(campoDuracao, gbc);

        gbc.gridy = 6; gbc.weighty = 1.0; // Espaçador vai para a linha 6
        camposPanel.add(new JLabel(), gbc);

        painelFormulario.add(camposPanel, BorderLayout.CENTER);

        JPanel pBotoes = new JPanel(new GridLayout(1, 2, 10, 0));
        pBotoes.setOpaque(false);
        
        botaoAcaoPrimaria = new JButton("Salvar");
        estilizarBotaoPrimario(botaoAcaoPrimaria);
        pBotoes.add(botaoAcaoPrimaria);

        botaoAcaoSecundaria = new JButton("Cancelar");
        estilizarBotaoPadrao(botaoAcaoSecundaria);
        pBotoes.add(botaoAcaoSecundaria);

        painelFormulario.add(pBotoes, BorderLayout.SOUTH);
    }


    private void atualizarLista() {
        painelListaCards.removeAll();
        List<Plano> lista = planoDAO.listarTodos();
        for (Plano p : lista) {
            painelListaCards.add(criarCardPlano(p));
            painelListaCards.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        painelListaCards.revalidate(); painelListaCards.repaint();
    }

    private JPanel criarCardPlano(Plano p) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lNome = new JLabel(p.getNome());
        lNome.setFont(FONTE_NOME_CARD);
        lNome.setForeground(COR_AZUL_PRINCIPAL);
        
        JLabel lValor = new JLabel(String.format("R$ %.2f", p.getValor()));
        lValor.setFont(FONTE_INFO_CARD);
        lValor.setForeground(COR_TEXTO_PADRAO);

        card.add(lNome, BorderLayout.CENTER);
        card.add(lValor, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { exibirModoEdicao(p); }
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, COR_AZUL_PRINCIPAL), new EmptyBorder(14, 19, 14, 19))); }
            public void mouseExited(java.awt.event.MouseEvent e) { card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)), new EmptyBorder(15, 20, 15, 20))); }
        });
        return card;
    }

    private void exibirModoAdicao() {
        planoSelecionado = null;
        labelTituloForm.setText("Novo Plano");
        campoNome.setText(""); campoValor.setText("");
        
        botaoAcaoPrimaria.setText("Salvar");
        botaoAcaoSecundaria.setText("Cancelar"); estilizarBotaoPadrao(botaoAcaoSecundaria);
        
        limparListeners(botaoAcaoPrimaria); limparListeners(botaoAcaoSecundaria);
        botaoAcaoPrimaria.addActionListener(e -> salvarPlano());
        botaoAcaoSecundaria.addActionListener(e -> voltarAoPlaceholder());
        
        layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
    }

    private void exibirModoEdicao(Plano p) {
    planoSelecionado = p;

    labelTituloForm.setText("Editar Plano");
    campoNome.setText(p.getNome());
    campoValor.setText(String.valueOf(p.getValor()).replace('.', ','));
    campoDuracao.setText(String.valueOf(p.getDuracaoMeses()));  // <-- FALTANDO

    botaoAcaoPrimaria.setText("Atualizar");
    botaoAcaoSecundaria.setText("Remover");
    estilizarBotaoRemover(botaoAcaoSecundaria);

    limparListeners(botaoAcaoPrimaria);
    limparListeners(botaoAcaoSecundaria);

    botaoAcaoPrimaria.addActionListener(e -> salvarPlano());
    botaoAcaoSecundaria.addActionListener(e -> removerPlano(p));

    layoutLadoDireito.show(painelLadoDireito, PAINEL_FORM);
}


    private void salvarPlano() {
        String nome = campoNome.getText();
        String valorStr = campoValor.getText().replace(',', '.');
        String duracaoStr = campoDuracao.getText(); 

        if (nome.isEmpty() || valorStr.isEmpty() || duracaoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos."); return;
        }

        double valor;
        int duracao;
        try { 
            valor = Double.parseDouble(valorStr); 
            duracao = Integer.parseInt(duracaoStr); // Novo parse
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Valor ou Duração inválidos."); return;
        }

        boolean ok = false;

        // 1. Cria/Atualiza Plano
        Plano p = (planoSelecionado == null) ? new Plano() : planoSelecionado;
        p.setNome(nome); 
        p.setValor(valor);
        p.setDuracaoMeses(duracao); // Novo set

        if (planoSelecionado == null) {
            ok = planoDAO.cadastrar(p);
        } else {
            ok = planoDAO.atualizar(p);
        }

        if (ok) {
            JOptionPane.showMessageDialog(this, "Sucesso!");
            voltarAoPlaceholder();
            atualizarLista();
        }
    }
    
    private void removerPlano(Plano p) {
        Object[] options = { "Sim", "Não" };
        int r = JOptionPane.showOptionDialog(this, "Remover o plano '" + p.getNome() + "'?", "Confirmar", 0, 3, null, options, options[0]);
        if (r == 0) {
            if (planoDAO.remover(p.getId())) {
                JOptionPane.showMessageDialog(this, "Plano removido.");
                voltarAoPlaceholder();
                atualizarLista();
            }
        }
    }

    private void voltarAoPlaceholder() { layoutLadoDireito.show(painelLadoDireito, PAINEL_PLACEHOLDER); planoSelecionado = null; }
    private void limparListeners(JButton b) { for(ActionListener al: b.getActionListeners()) b.removeActionListener(al); }

    private void estilizarLabel(JLabel l) { l.setFont(FONTE_LABEL_FORM); l.setForeground(COR_TEXTO_PADRAO); l.setOpaque(false); }
    private void estilizarCampo(JTextComponent c) { c.setFont(FONTE_CAMPO_FORM); c.setForeground(COR_TEXTO_CAMPO); c.setBackground(COR_BRANCO); c.setBorder(BORDA_CAMPO_PADRAO); c.setCaretColor(COR_TEXTO_CAMPO); }
    private void estilizarBotaoPrimario(JButton b) { b.setBackground(COR_AZUL_PRINCIPAL); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setOpaque(true); b.setBorderPainted(false); }
    private void estilizarBotaoPadrao(JButton b) { b.setBackground(COR_BRANCO); b.setForeground(COR_TEXTO_PADRAO); b.setFocusPainted(false); b.setOpaque(true); b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(200,200,200)), new EmptyBorder(5,10,5,10))); b.setBorderPainted(true); }
    private void estilizarBotaoRemover(JButton b) { b.setBackground(COR_VERMELHO_REMOVER); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setOpaque(true); b.setBorderPainted(false); }

    private void estilizarBotaoVoltar(JButton botao) {
        Color corFundoVoltar = new Color(80, 80, 80); 
        Color corHoverVoltar = new Color(110, 110, 110); 

        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(corFundoVoltar);
        botao.setForeground(Color.WHITE); 

        botao.setOpaque(true); 
        botao.setBorderPainted(false); 
        botao.setFocusPainted(false); 
        
        botao.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        
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

    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new TelaGestaoPlanos(null).setVisible(true));
    }
}