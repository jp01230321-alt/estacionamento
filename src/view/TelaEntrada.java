package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.EstacionamentoController;

public class TelaEntrada extends TelaBase {

    private static final long serialVersionUID = 1L;
    private final JTextField txtPlaca = new JTextField();
    private final JTextField txtModelo = new JTextField();
    private final JTextField txtData = new JTextField();
    private final JTextField txtHora = new JTextField();
    private final JLabel lblVagas = new JLabel();

    public TelaEntrada(EstacionamentoController controller, TelaPrincipal principal) {
        super("Entrada de veículos", controller, principal);

        Tema.campo(txtPlaca);
        Tema.campo(txtModelo);
        Tema.campo(txtData);
        Tema.campo(txtHora);

        JPanel form = new JPanel(new GridLayout(2, 2, 18, 16));
        form.setOpaque(false);
        form.add(Tema.campoRotulado("PLACA (ABC1234 ou ABC1D23)", txtPlaca));
        form.add(Tema.campoRotulado("MODELO", txtModelo));
        form.add(Tema.campoRotulado("DATA DE ENTRADA (dd/MM/aaaa)", txtData));
        form.add(Tema.campoRotulado("HORÁRIO DE ENTRADA (HH:mm)", txtHora));

        lblVagas.setFont(Tema.fonte(Font.BOLD, 14));
        lblVagas.setForeground(Tema.LARANJA);

        JPanel corpo = new JPanel(new BorderLayout(0, 16));
        corpo.setOpaque(false);
        corpo.add(form, BorderLayout.NORTH);
        corpo.add(lblVagas, BorderLayout.SOUTH);

        JButton btRegistrar = Tema.primario("Registrar entrada");
        JButton btAgora = Tema.secundario("Usar data/hora atual");
        JButton btLimpar = Tema.secundario("Limpar");
        btRegistrar.addActionListener(e -> registrar());
        btAgora.addActionListener(e -> preencherAgora());
        btLimpar.addActionListener(e -> limpar());

        montarTela("Entrada de veículos", "Cadastre a placa, o modelo e o horário de chegada.",
                corpo, btRegistrar, btAgora, btLimpar);

        preencherAgora();
        atualizarVagas();
        exibir(780, 440);
    }

    private void registrar() {
        try {
            controller.registrarEntrada(txtPlaca.getText(), txtModelo.getText(), txtData.getText(), txtHora.getText());
            JOptionPane.showMessageDialog(this, "Entrada registrada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            limpar();
            atualizarVagas();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherAgora() {
        txtData.setText(controller.dataAtual());
        txtHora.setText(controller.horaAtual());
    }

    private void limpar() {
        txtPlaca.setText("");
        txtModelo.setText("");
        preencherAgora();
        txtPlaca.requestFocus();
    }

    private void atualizarVagas() {
        lblVagas.setText("Vagas disponíveis: " + controller.getVagasDisponiveis() + " de " + controller.getCapacidade());
    }
}