/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */

import br.com.gymmanager.dao.TreinoDAO;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.FichaTreino;
import br.com.gymmanager.model.ItemFicha;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TelaVisualizarFicha extends JDialog {

    private static final Color COR_FUNDO = new Color(240, 242, 245);
    private static final Color COR_AZUL = new Color(30, 90, 200);
    private static final Color COR_BRANCO = Color.WHITE;
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);

    private JTextField campoAlunoNome;
    private JEditorPane painelVisualizacao;
    private JButton btnImprimir;

    private Aluno alunoSelecionado;
    private TreinoDAO treinoDAO;

    public TelaVisualizarFicha(JFrame parent) {
        super(parent, "GymManager - Visualizar Ficha de Treino", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        this.treinoDAO = new TreinoDAO();

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarTopo(), BorderLayout.NORTH);
        painelPrincipal.add(criarAreaRelatorio(), BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);

        painelVisualizacao.setText(gerarHtmlVazio("Selecione um aluno para visualizar o treino."));
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel lTitulo = new JLabel("Visualização de Treino");
        lTitulo.setFont(FONTE_TITULO);
        lTitulo.setForeground(COR_AZUL);
        p.add(lTitulo, gbc);

        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 1;
        p.add(new JLabel("Aluno:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoAlunoNome = new JTextField("Nenhum aluno selecionado");
        campoAlunoNome.setEditable(false);
        campoAlunoNome.setBackground(Color.WHITE);
        campoAlunoNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(campoAlunoNome, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(COR_AZUL);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> abrirBuscaAluno());
        p.add(btnBuscar, gbc);

        return p;
    }

    private JScrollPane criarAreaRelatorio() {
        painelVisualizacao = new JEditorPane();
        painelVisualizacao.setContentType("text/html");
        painelVisualizacao.setEditable(false);
        painelVisualizacao.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        painelVisualizacao.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(painelVisualizacao);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return scroll;
    }

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        btnImprimir = new JButton("Imprimir / Salvar PDF");
        btnImprimir.setBackground(new Color(46, 204, 113));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImprimir.setEnabled(false);

        btnImprimir.addActionListener(e -> imprimirFicha());

        p.add(btnFechar);
        p.add(btnImprimir);
        return p;
    }

    private void abrirBuscaAluno() {
        DialogoBuscaAluno dialogo = new DialogoBuscaAluno(null);
        dialogo.setVisible(true);

        Aluno selecionado = dialogo.getAlunoSelecionado();
        if (selecionado != null) {
            this.alunoSelecionado = selecionado;
            campoAlunoNome.setText(selecionado.getNome());
            carregarFicha();
        }
    }

    private void carregarFicha() {
        FichaTreino ficha = treinoDAO.buscarFichaAtivaPorAluno(alunoSelecionado.getId());

        if (ficha != null) {
            ficha.setAluno(alunoSelecionado);

            String html = gerarHtmlFicha(ficha);
            painelVisualizacao.setText(html);
            painelVisualizacao.setCaretPosition(0);
            btnImprimir.setEnabled(true);
        } else {
            painelVisualizacao.setText(gerarHtmlVazio("Este aluno não possui uma ficha de treino ativa."));
            btnImprimir.setEnabled(false);
        }
    }

    private void imprimirFicha() {
        try {
            boolean complete = painelVisualizacao.print(
                new MessageFormat("Ficha de Treino - " + alunoSelecionado.getNome()),
                new MessageFormat("Página {0}")
            );
            if (complete) {
                JOptionPane.showMessageDialog(this, "Impressão concluída!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao imprimir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String gerarHtmlFicha(FichaTreino ficha) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><style>");
        sb.append("body { font-family: Segoe UI, sans-serif; color: #333; }");
        sb.append("h1 { color: #1E5Acd; border-bottom: 2px solid #1E5Acd; padding-bottom: 10px; }");
        sb.append("h2 { background-color: #eee; padding: 5px; border-left: 5px solid #1E5Acd; margin-top: 20px; }");
        sb.append("table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }");
        sb.append("th { background-color: #f2f2f2; text-align: left; padding: 8px; border-bottom: 1px solid #ddd; }");
        sb.append("td { padding: 8px; border-bottom: 1px solid #eee; }");
        sb.append(".info { color: #666; font-size: 12px; margin-bottom: 20px; }");
        sb.append("</style></head><body>");

        sb.append("<h1>Ficha de Treino</h1>");
        sb.append("<div class='info'>");

        sb.append("<b>Aluno:</b> ").append(ficha.getAluno().getNome()).append("<br>");

        sb.append("<b>Objetivo:</b> ").append(ficha.getObjetivo()).append("<br>");
        sb.append("<b>Data Emissão:</b> ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("<br>");
        sb.append("</div>");

        Map<String, List<ItemFicha>> treinoPorDia = new LinkedHashMap<>();

        String[] ordemDias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        for (String dia : ordemDias) treinoPorDia.put(dia, new ArrayList<>());

        for (ItemFicha item : ficha.getItens()) {
            String dia = item.getDivisao();
            if (!treinoPorDia.containsKey(dia)) {
                treinoPorDia.put(dia, new ArrayList<>());
            }
            treinoPorDia.get(dia).add(item);
        }

        for (String dia : treinoPorDia.keySet()) {
            List<ItemFicha> lista = treinoPorDia.get(dia);

            if (!lista.isEmpty()) {
                sb.append("<h2>").append(dia).append("</h2>");

                sb.append("<table>");
                sb.append("<tr><th width='40%'>Exercício</th><th width='20%'>Séries</th><th width='20%'>Reps</th><th width='20%'>Tempo/Obs</th></tr>");

                for (ItemFicha item : lista) {
                    sb.append("<tr>");
                    sb.append("<td>").append(item.getExercicio().getNome()).append("</td>");
                    sb.append("<td>").append(item.getSeries()).append("</td>");
                    sb.append("<td>").append(item.getRepeticoes()).append("</td>");

                    String tempo = (item.getTempo() == null) ? "-" : item.getTempo();
                    sb.append("<td>").append(tempo).append("</td>");

                    sb.append("</tr>");
                }
                sb.append("</table>");
            }
        }

        sb.append("<br><hr><center><small>Gerado por GymManager</small></center>");
        sb.append("</body></html>");

        return sb.toString();
    }

    private String gerarHtmlVazio(String mensagem) {
        return "<html><body style='font-family: sans-serif; color: #888; text-align: center; margin-top: 100px;'>"
             + "<h1>🏋️</h1><h2>" + mensagem + "</h2></body></html>";
    }

    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new TelaVisualizarFicha(null).setVisible(true));
    }
}