
package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.EstacionamentoController;
import model.Veiculo;


public class TabelaVeiculos extends JScrollPane {

    private static final long serialVersionUID = 1L;
    private final transient EstacionamentoController controller;
    private final DefaultTableModel modelo;
    private final JTable tabela;

    public TabelaVeiculos(EstacionamentoController controller) {
        this.controller = controller;
        modelo = new DefaultTableModel(
                new String[] { "Placa", "Modelo", "Entrada", "Saída", "Permanência", "Valor", "Situação" }, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(34);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setGridColor(new java.awt.Color(0xEDEEF2));
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setSelectionBackground(Tema.LARANJA_CLARO);
        tabela.setSelectionForeground(Tema.TEXTO);

        int[] larguras = { 90, 130, 130, 130, 100, 80, 100 };
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

       
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,
                    boolean foco, int linha, int coluna) {
                super.getTableCellRendererComponent(t, valor, sel, false, linha, coluna);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setFont(Tema.fonte(Font.PLAIN, 13));
                setForeground(Tema.TEXTO);
                if (coluna == 6 && valor != null) {
                    boolean estacionado = "Estacionado".equals(valor.toString());
                    setForeground(estacionado ? Tema.LARANJA : Tema.VERDE);
                    setFont(Tema.fonte(Font.BOLD, 13));
                }
                return this;
            }
        });

      
        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setPreferredSize(new Dimension(0, 38));
        cabecalho.setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,
                    boolean foco, int linha, int coluna) {
                super.getTableCellRendererComponent(t, valor, false, false, linha, coluna);
                setOpaque(true);
                setBackground(new java.awt.Color(0xF4F4F8));
                setForeground(Tema.TEXTO_SUAVE);
                setFont(Tema.fonte(Font.BOLD, 12));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        setBorder(BorderFactory.createEmptyBorder());
        getViewport().setBackground(Tema.BRANCO);
        setViewportView(tabela);
    }

    public void atualizar(List<Veiculo> lista) {
        modelo.setRowCount(0);
        for (Veiculo v : lista) {
            modelo.addRow(controller.linhaTabela(v));
        }
    }

    public String placaSelecionada() {
        int i = tabela.getSelectedRow();
        return i < 0 ? null : (String) modelo.getValueAt(i, 0);
    }

    public JTable getTabela() {
        return tabela;
    }
}
