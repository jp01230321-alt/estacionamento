package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.EstacionamentoController;

public class TelaSaida extends TelaBase {

    private static final long serialVersionUID = 1L;
    private final JTextField txtPlaca = new JTextField();
    private final JTextField txtData = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JTextArea areaResumo = new JTextArea(7, 30);
    private final TabelaVeiculos tabela;

    public TelaSaida(EstacionamentoController controller, TelaPrincipal principal) {
        super("Registro de saída", controller, principal);
        tabela = new TabelaVeiculos(controller);

        Tema.campo(txtPlaca);
        Tema.campo(txtData);
        Tema.campo(txtHora);

        JPanel form = new JPanel(new GridLayout(1, 3, 14, 0));
        form.setOpaque(false);
        form.add(Tema.campoRotulado("PLACA (clique na tabela para preencher)", txtPlaca));
        form.add(Tema.campoRotulado("DATA DE SAÍDA", txtData));
        form.add(Tema.campoRotulado("HORÁRIO (HH:mm)", txtHora));

        areaResumo.setEditable(false);
        areaResumo.setOpaque(false);
        areaResumo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResumo.setForeground(Tema.TEXTO);

        Tema.Cartao resumo = new Tema.Cartao(14, new Color(0xF6F6F9), false, 12);
        resumo.setLayout(new BorderLayout(0, 6));
        resumo.add(Tema.rotulo("PERMANÊNCIA E COBRANÇA"), BorderLayout.NORTH);
        resumo.add(areaResumo, BorderLayout.CENTER);

        JPanel corpo = new JPanel(new BorderLayout(0, 14));
        corpo.setOpaque(false);
        corpo.add(form, BorderLayout.NORTH);
        corpo.add(tabela, BorderLayout.CENTER);
        corpo.add(resumo, BorderLayout.SOUTH);

        JButton btAgora = Tema.secundario("Usar data/hora atual");
        JButton btCalcular = Tema.secundario("Calcular cobrança");
        JButton btConfirmar = Tema.primario("Confirmar saída");
        btAgora.addActionListener(e -> preencherAgora());
        btCalcular.addActionListener(e -> calcular());
        btConfirmar.addActionListener(e -> confirmar());

        montarTela("Registro de saída", "Selecione o veículo, informe o horário e confirme a cobrança.",
                corpo, btAgora, btCalcular, btConfirmar);

        tabela.getTabela().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.placaSelecionada() != null) {
                txtPlaca.setText(tabela.placaSelecionada());
            }
        });

        preencherAgora();
        tabela.atualizar(controller.listarEstacionados());
        exibir(1000, 720);
    }

    private void preencherAgora() {
        txtData.setText(controller.dataAtual());
        txtHora.setText(controller.horaAtual());
    }

    private void calcular() {
        try {
            areaResumo.setText(controller.previaSaida(txtPlaca.getText(), txtData.getText(), txtHora.getText()));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmar() {
        try {
            String previa = controller.previaSaida(txtPlaca.getText(), txtData.getText(), txtHora.getText());
            int op = JOptionPane.showConfirmDialog(this, previa + "\n\nConfirmar a saída?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
            String resumo = controller.registrarSaida(txtPlaca.getText(), txtData.getText(), txtHora.getText());
            areaResumo.setText(resumo);
            JOptionPane.showMessageDialog(this, "Saída registrada!\n\n" + resumo, "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            txtPlaca.setText("");
            tabela.atualizar(controller.listarEstacionados());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}