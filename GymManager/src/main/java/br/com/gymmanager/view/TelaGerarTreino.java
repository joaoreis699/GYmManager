/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.gymmanager.view;

/**
 *
 * @author joaoreis699
 */


import br.com.gymmanager.dao.ExercicioDAO;
import br.com.gymmanager.dao.TreinoDAO;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Exercicio;
import br.com.gymmanager.model.FichaTreino;
import br.com.gymmanager.model.ItemFicha;
import br.com.gymmanager.util.Sessao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaGerarTreino extends JDialog {

    private static final Color COR_FUNDO = new Color(240, 242, 245);
    private static final Color COR_AZUL = new Color(30, 90, 200);
    private static final Color COR_VERDE = new Color(46, 204, 113);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);

    private JTextField campoAlunoNome;
    private Aluno alunoSelecionado;
    private JTextField campoObjetivo;
    private JTabbedPane painelAbas;
    
    private Map<String, DefaultTableModel> mapaModelos = new HashMap<>();
    private Map<String, List<ItemFicha>> mapaItens = new HashMap<>();

    private ExercicioDAO exercicioDAO;
    private TreinoDAO treinoDAO;
    
    private JTextField campoBuscaExercicio;
    private TableRowSorter<DefaultTableModel> sorterCatalogo;
    private DefaultTableModel modeloCatalogoGeral; 

    public TelaGerarTreino(JFrame parent) {
        super(parent, "GymManager - Montagem de Treino Semanal", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        this.exercicioDAO = new ExercicioDAO();
        this.treinoDAO = new TreinoDAO();

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(painelPrincipal);

        painelPrincipal.add(criarPainelTopo(), BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                criarPainelCatalogo(), 
                criarPainelSemana());
        splitPane.setDividerLocation(400); 
        splitPane.setOpaque(false);
        
        painelPrincipal.add(splitPane, BorderLayout.CENTER);
        painelPrincipal.add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelTopo() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        JLabel lTitulo = new JLabel("Nova Ficha de Treino Semanal");
        lTitulo.setFont(FONTE_TITULO);
        lTitulo.setForeground(COR_AZUL);
        p.add(lTitulo, gbc);
        
        String nomeInstrutor = Sessao.getInstance().getNomeUsuario();
        JLabel lInstrutor = new JLabel("Instrutor: " + nomeInstrutor);
        lInstrutor.setForeground(Color.GRAY);
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.EAST;
        p.add(lInstrutor, gbc);

        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        p.add(new JLabel("Aluno:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoAlunoNome = new JTextField("Nenhum aluno selecionado");
        campoAlunoNome.setEditable(false);
        campoAlunoNome.setBackground(Color.WHITE);
        campoAlunoNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(campoAlunoNome, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JButton btnBuscar = new JButton("🔍 Buscar Aluno");
        btnBuscar.setBackground(COR_AZUL);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> abrirBuscaAluno());
        p.add(btnBuscar, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        p.add(new JLabel("Objetivo:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoObjetivo = new JTextField();
        p.add(campoObjetivo, gbc);

        return p;
    }
    
    private JPanel criarPainelCatalogo() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Catálogo de Exercícios"));
        p.setBackground(COR_FUNDO);

        JPanel pBusca = new JPanel(new BorderLayout(5, 0));
        pBusca.setOpaque(false);
        
        campoBuscaExercicio = new JTextField();
        pBusca.add(new JLabel("🔍"), BorderLayout.WEST);
        pBusca.add(campoBuscaExercicio, BorderLayout.CENTER);
        
        JButton btnNovoExercicio = new JButton("+");
        btnNovoExercicio.setBackground(COR_VERDE);
        btnNovoExercicio.setForeground(Color.WHITE);
        btnNovoExercicio.addActionListener(e -> abrirNovoExercicio());
        pBusca.add(btnNovoExercicio, BorderLayout.EAST);
        
        p.add(pBusca, BorderLayout.NORTH);

        modeloCatalogoGeral = new DefaultTableModel(new Object[]{"Nome", "Grupo", "OBJ"}, 0) {
             public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable tabela = new JTable(modeloCatalogoGeral);
        tabela.removeColumn(tabela.getColumnModel().getColumn(2)); 
        tabela.setRowHeight(25);
        tabela.setShowVerticalLines(false);
        
        sorterCatalogo = new TableRowSorter<>(modeloCatalogoGeral);
        tabela.setRowSorter(sorterCatalogo);
        
        campoBuscaExercicio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = campoBuscaExercicio.getText();
                if (texto.trim().length() == 0) {
                    sorterCatalogo.setRowFilter(null);
                } else {
                    sorterCatalogo.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        carregarExercicios();
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        JButton btnAdicionar = new JButton("Adicionar ao Dia Selecionado >>");
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdicionar.setBackground(COR_AZUL);
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        
        btnAdicionar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                int modelRow = tabela.convertRowIndexToModel(row);
                Exercicio ex = (Exercicio) modeloCatalogoGeral.getValueAt(modelRow, 2);
                adicionarExercicioAoDiaAtual(ex);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um exercício na lista.");
            }
        });
        
        p.add(btnAdicionar, BorderLayout.SOUTH);
        
        return p;
    }

    private JPanel criarPainelSemana() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COR_FUNDO);
        
        painelAbas = new JTabbedPane();
        painelAbas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] dias = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};
        
        for (String dia : dias) {
            painelAbas.addTab(dia, criarTabelaDia(dia));
            mapaItens.put(dia, new ArrayList<>());
        }
        
        p.add(painelAbas, BorderLayout.CENTER);
        return p;
    }
    
    private JPanel criarTabelaDia(String dia) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Exercício", "Séries", "Reps", "Tempo"}, 0);
        JTable tabela = new JTable(modelo);
        tabela.setRowHeight(25);
        
        mapaModelos.put(dia, modelo);
        
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);

        JButton btnLimpar = new JButton("Limpar / Nova Ficha");
        btnLimpar.setBackground(Color.ORANGE);
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.addActionListener(e -> limparTudo());
        p.add(btnLimpar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnSalvar = new JButton("Finalizar e Salvar Ficha");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setPreferredSize(new Dimension(220, 45));
        
        btnSalvar.addActionListener(e -> salvarFichaNoBanco());

        p.add(btnCancelar);
        p.add(btnSalvar);
        return p;
    }

    private void abrirBuscaAluno() {
        DialogoBuscaAluno dialogo = new DialogoBuscaAluno(null);
        dialogo.setVisible(true);
        
        Aluno selecionado = dialogo.getAlunoSelecionado();
        if (selecionado != null) {
            this.alunoSelecionado = selecionado;
            campoAlunoNome.setText(selecionado.getNome() + " (CPF: " + selecionado.getCpf() + ")");
            campoAlunoNome.setBorder(BorderFactory.createLineBorder(COR_VERDE, 2));
            
            carregarFichaDoAluno(selecionado.getId());
        }
    }
    
    private void abrirNovoExercicio() {
        DialogoNovoExercicio dialogo = new DialogoNovoExercicio(null);
        dialogo.setVisible(true);
        
        if (dialogo.isCadastrou()) {
            carregarExercicios();
        }
    }

    private void carregarExercicios() {
        modeloCatalogoGeral.setRowCount(0);
        List<Exercicio> lista = exercicioDAO.listarTodos();
        for (Exercicio ex : lista) {
            modeloCatalogoGeral.addRow(new Object[]{ex.getNome(), ex.getGrupoMuscular(), ex});
        }
    }

    private void adicionarExercicioAoDiaAtual(Exercicio exercicio) {
        int index = painelAbas.getSelectedIndex();
        String diaSelecionado = painelAbas.getTitleAt(index);
        
        JTextField txtSeries = new JTextField("3");
        JTextField txtReps = new JTextField("12");
        JTextField txtTempo = new JTextField("");
        
        Object[] msg = {"Configurar " + exercicio.getNome() + " para " + diaSelecionado, 
                        "Séries:", txtSeries, "Reps:", txtReps, "Tempo (Opcional):", txtTempo};
        
        int opt = JOptionPane.showConfirmDialog(this, msg, "Adicionar", JOptionPane.OK_CANCEL_OPTION);
        
        if (opt == JOptionPane.OK_OPTION) {
            ItemFicha item = new ItemFicha();
            item.setExercicio(exercicio);
            try { item.setSeries(Integer.parseInt(txtSeries.getText())); } catch(Exception e) { item.setSeries(0); }
            item.setRepeticoes(txtReps.getText());
            item.setTempo(txtTempo.getText());
            item.setDivisao(diaSelecionado);

            mapaItens.get(diaSelecionado).add(item);
            
            mapaModelos.get(diaSelecionado).addRow(new Object[]{
                exercicio.getNome(), item.getSeries(), item.getRepeticoes(), item.getTempo()
            });
        }
    }

    private void carregarFichaDoAluno(int idAluno) {
        limparTudo(false);
        
        FichaTreino ficha = treinoDAO.buscarFichaAtivaPorAluno(idAluno);
        
        if (ficha != null) {
            int resp = JOptionPane.showConfirmDialog(this, 
                "Este aluno já possui uma ficha ativa.\nDeseja carregar os dados para editar?", 
                "Ficha Encontrada", JOptionPane.YES_NO_OPTION);
                
            if (resp == JOptionPane.YES_OPTION) {
                campoObjetivo.setText(ficha.getObjetivo());
                
                for (ItemFicha item : ficha.getItens()) {
                    String dia = item.getDivisao();
                    
                    if (mapaItens.containsKey(dia)) {
                        mapaItens.get(dia).add(item);
                        
                        DefaultTableModel modelo = mapaModelos.get(dia);
                        if (modelo != null) {
                            modelo.addRow(new Object[]{
                                item.getExercicio().getNome(), 
                                item.getSeries(), 
                                item.getRepeticoes(), 
                                item.getTempo()
                            });
                        }
                    }
                }
            }
        }
    }

    private void limparTudo() {
        limparTudo(true);
    }

    private void limparTudo(boolean limparAluno) {
        if (limparAluno) {
            alunoSelecionado = null;
            campoAlunoNome.setText("Nenhum aluno selecionado");
            campoAlunoNome.setBorder(UIManager.getBorder("TextField.border"));
        }
        campoObjetivo.setText("");
        
        for (String dia : mapaModelos.keySet()) {
            mapaModelos.get(dia).setRowCount(0);
            mapaItens.get(dia).clear();
        }
    }

    private void salvarFichaNoBanco() {
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno!");
            return;
        }

        FichaTreino ficha = new FichaTreino();
        ficha.setAluno(alunoSelecionado);
        ficha.setObjetivo(campoObjetivo.getText());
        
        boolean temItens = false;

        for (String dia : mapaItens.keySet()) {
            List<ItemFicha> listaDoDia = mapaItens.get(dia);
            for (ItemFicha item : listaDoDia) {
                ficha.adicionarItem(item);
                temItens = true;
            }
        }

        if (!temItens) {
            JOptionPane.showMessageDialog(this, "A ficha está vazia. Adicione exercícios.");
            return;
        }

        treinoDAO.excluirFichaDoAluno(alunoSelecionado.getId());

        if (treinoDAO.salvarFichaCompleta(ficha)) {
            JOptionPane.showMessageDialog(this, "Ficha salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar ficha.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new TelaGerarTreino(null).setVisible(true));
    }
}