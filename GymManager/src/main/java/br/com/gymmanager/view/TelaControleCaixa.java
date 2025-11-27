/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import br.com.gymmanager.dao.PagamentoDAO;
import br.com.gymmanager.model.Pagamento;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaControleCaixa extends JDialog {

    private static final Color COR_FUNDO = new Color(240, 242, 245);
    private static final Color COR_AZUL_PRINCIPAL = new Color(30, 90, 200);
    private static final Color COR_BRANCO = Color.WHITE;
    private static final Color COR_VERDE_SUCESSO = new Color(46, 204, 113);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONTE_TABELA = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_CABECALHO = new Font("Segoe UI", Font.BOLD, 14);

    private JTable tabelaPagamentos;
    private DefaultTableModel modeloTabela;
    private JButton botaoBaixar, botaoVoltar;
    
    private PagamentoDAO pagamentoDAO;

    public TelaControleCaixa(JFrame parent) {
        super(parent, "GymManager - Controle de Caixa", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);

        this.pagamentoDAO = new PagamentoDAO();

        JPanel painelPrincipal = new JPanel(new BorderLayout(20, 20));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);

        painelPrincipal.add(criarPainelTabela(), BorderLayout.CENTER);

        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);

        atualizarTabela();
    }

    private JPanel criarHeader() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        
        JLabel lTitulo = new JLabel("CONTROLE DE CAIXA - PAGAMENTOS PENDENTES");
        lTitulo.setFont(FONTE_TITULO);
        lTitulo.setForeground(COR_AZUL_PRINCIPAL);
        
        p.add(lTitulo);
        return p;
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"ID", "Aluno", "CPF", "Vencimento", "Valor (R$)", "Status"};
        
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPagamentos = new JTable(modeloTabela);
        tabelaPagamentos.setFont(FONTE_TABELA);
        tabelaPagamentos.setRowHeight(30);
        tabelaPagamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaPagamentos.setFillsViewportHeight(true);
        tabelaPagamentos.setShowVerticalLines(false);
        tabelaPagamentos.setGridColor(new Color(230, 230, 230));

        JTableHeader header = tabelaPagamentos.getTableHeader();
        header.setFont(FONTE_CABECALHO);
        header.setBackground(COR_BRANCO);
        header.setForeground(new Color(80, 80, 80));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < colunas.length; i++) {
            tabelaPagamentos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(tabelaPagamentos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scroll.getViewport().setBackground(COR_BRANCO);
        
        return scroll;
    }

    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        p.setOpaque(false);

        botaoVoltar = new JButton("Voltar");
        estilizarBotao(botaoVoltar, new Color(100, 100, 100));
        botaoVoltar.addActionListener(e -> dispose());

        botaoBaixar = new JButton("Confirmar Recebimento");
        estilizarBotao(botaoBaixar, COR_VERDE_SUCESSO);
        botaoBaixar.addActionListener(e -> baixarPagamentoSelecionado());

        p.add(botaoVoltar);
        p.add(botaoBaixar);
        return p;
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        
        List<Pagamento> lista = pagamentoDAO.listarPendentes();
        
        DateTimeFormatter formatoBanco = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Pagamento p : lista) {
            
            String dataBonita = p.getDataVencimento(); 
            try {
                if (dataBonita != null && !dataBonita.isEmpty()) {
                    LocalDate dataObj = LocalDate.parse(dataBonita, formatoBanco);
                    dataBonita = dataObj.format(formatoBr);
                }
            } catch (Exception e) {
            }

            String valorBonito = String.format("R$ %.2f", p.getValor());

            Object[] linha = {
                p.getId(),
                p.getAluno().getNome(),
                p.getAluno().getCpf(),
                dataBonita,  
                valorBonito,  
                p.getStatus()
            };
            modeloTabela.addRow(linha);
        }
    }

    private void baixarPagamentoSelecionado() {
        int linhaSelecionada = tabelaPagamentos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um pagamento na lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPagamento = (int) tabelaPagamentos.getValueAt(linhaSelecionada, 0);
        String nomeAluno = (String) tabelaPagamentos.getValueAt(linhaSelecionada, 1);
        String valor = (String) tabelaPagamentos.getValueAt(linhaSelecionada, 4);

        Object[] options = {"Dinheiro", "Pix", "Cartão", "Cancelar"};
        int resposta = JOptionPane.showOptionDialog(this,
            "Confirmar recebimento de R$ " + valor + " referente a " + nomeAluno + "?\n\nSelecione a forma de pagamento:",
            "Baixa de Pagamento",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (resposta >= 0 && resposta < 3) {
            String formaEscolhida = (String) options[resposta];
            
            boolean sucesso = pagamentoDAO.baixarPagamento(idPagamento, formaEscolhida);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Pagamento registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela(); 
            }
        }
    }

    private void estilizarBotao(JButton b, Color cor) {
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new TelaControleCaixa(null).setVisible(true));
    }
}
