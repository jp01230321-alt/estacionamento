package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.EstacionamentoController;

public class TelaRelatorio extends TelaBase {

    private static final long serialVersionUID = 1L;
    private final JLabel lblEstacionados = criarValor();
    private final JLabel lblVagas = criarValor();
    private final JLabel lblCapacidade = criarValor();
    private final JLabel lblAtendimentos = criarValor();
    private final JLabel lblTarifa = criarValor();
    private final JLabel lblFaturamento = criarValor();

    public TelaRelatorio(EstacionamentoController controller, TelaPrincipal principal) {
        super("Relatório operacional", controller, principal);

        JPanel corpo = new JPanel(new GridLayout(2, 3, 16, 16));
        corpo.setOpaque(false);
        corpo.add(criarCartao("VEÍCULOS ESTACIONADOS", lblEstacionados, false));
        corpo.add(criarCartao("VAGAS DISPONÍVEIS", lblVagas, false));
        corpo.add(criarCartao("CAPACIDADE TOTAL", lblCapacidade, false));
        corpo.add(criarCartao("TOTAL DE REGISTROS", lblAtendimentos, false));
        corpo.add(criarCartao("TARIFA POR HORA OU FRAÇÃO", lblTarifa, false));
        corpo.add(criarCartao("FATURAMENTO ACUMULADO", lblFaturamento, true));

        JButton btAtualizar = Tema.primario("Atualizar");
        btAtualizar.addActionListener(e -> atualizar());

        montarTela("Relatório operacional", "Visão geral da ocupação e do faturamento do estacionamento.",
                corpo, btAtualizar);

        atualizar();
        exibir(860, 480);
    }

    private static JLabel criarValor() {
        JLabel l = new JLabel("-");
        l.setFont(Tema.fonte(Font.BOLD, 26));
        return l;
    }

    private JPanel criarCartao(String rotulo, JLabel valor, boolean destaque) {
        Tema.Cartao c = new Tema.Cartao(16, destaque ? Tema.LARANJA : new Color(0xF6F6F9), false, 16);
        c.setLayout(new GridLayout(2, 1, 0, 4));
        JLabel r = Tema.rotulo(rotulo);
        r.setForeground(destaque ? new Color(255, 230, 222) : Tema.TEXTO_SUAVE);
        valor.setForeground(destaque ? Color.WHITE : Tema.TEXTO);
        c.add(r);
        c.add(valor);
        return c;
    }

    private void atualizar() {
        lblEstacionados.setText(String.valueOf(controller.getQuantidadeEstacionados()));
        lblVagas.setText(String.valueOf(controller.getVagasDisponiveis()));
        lblCapacidade.setText(String.valueOf(controller.getCapacidade()));
        lblAtendimentos.setText(String.valueOf(controller.getTotalAtendimentos()));
        lblTarifa.setText(controller.getTarifa());
        lblFaturamento.setText(controller.getFaturamento());
    }
}