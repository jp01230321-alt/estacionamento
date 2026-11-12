package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.EstacionamentoController;
import model.Veiculo;

public class TelaConsulta extends TelaBase {

    private static final long serialVersionUID = 1L;
    private final JTextField txtPlaca = new JTextField(12);
    private final JComboBox<String> cbFiltro =
            new JComboBox<>(new String[] { "Veículos estacionados", "Todos os registros" });
    private final TabelaVeiculos tabela;

    public TelaConsulta(EstacionamentoController controller, TelaPrincipal principal) {
        super("Consulta de veículos", controller, principal);
        tabela = new TabelaVeiculos(controller);

        Tema.campo(txtPlaca);
        Tema.combo(cbFiltro);

        JButton btPesquisar = Tema.primario("Pesquisar por placa");
        JButton btListar = Tema.secundario("Listar");
        btPesquisar.addActionListener(e -> pesquisar());
        btListar.addActionListener(e -> listar());

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtros.setOpaque(false);
        filtros.add(Tema.rotulo("PLACA"));
        filtros.add(txtPlaca);
        filtros.add(btPesquisar);
        filtros.add(Tema.rotulo("   EXIBIR"));
        filtros.add(cbFiltro);
        filtros.add(btListar);

        JPanel corpo = new JPanel(new BorderLayout(0, 14));
        corpo.setOpaque(false);
        corpo.add(filtros, BorderLayout.NORTH);
        corpo.add(tabela, BorderLayout.CENTER);

        montarTela("Consulta de veículos", "Pesquise por placa e veja a situação de cada veículo.", corpo);

        listar();
        exibir(1000, 540);
    }

    private void listar() {
        txtPlaca.setText("");
        boolean estacionados = cbFiltro.getSelectedIndex() == 0;
        tabela.atualizar(estacionados ? controller.listarEstacionados() : controller.listarTodos());
    }

    private void pesquisar() {
        try {
            List<Veiculo> resultado = controller.pesquisar(txtPlaca.getText());
            tabela.atualizar(resultado);
            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum veículo encontrado para essa placa.",
                        "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}