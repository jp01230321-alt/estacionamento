package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.EstacionamentoController;

/** Base das telas secundárias: layout padrão e retorno à tela principal. */
public abstract class TelaBase extends JFrame {

    private static final long serialVersionUID = 1L;
    protected final transient EstacionamentoController controller;

    protected TelaBase(String titulo, EstacionamentoController controller, TelaPrincipal principal) {
        super(titulo);
        this.controller = controller;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                principal.atualizarResumo();
                principal.setVisible(true);
            }
        });
    }

    /** Monta cabeçalho, cartão com o corpo e rodapé com as ações (o botão Voltar é sempre incluído). */
    protected void montarTela(String titulo, String subtitulo, JComponent corpo, JButton... acoes) {
        JPanel raiz = new JPanel(new BorderLayout(0, 14));
        raiz.setBackground(Tema.FUNDO);
        raiz.setBorder(new EmptyBorder(24, 32, 20, 32));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(Tema.esq(Tema.titulo(titulo, 28)));
        cabecalho.add(Box.createVerticalStrut(8));
        cabecalho.add(Tema.esq(Tema.barraLaranja()));
        cabecalho.add(Box.createVerticalStrut(10));
        cabecalho.add(Tema.esq(Tema.texto(subtitulo)));

        Tema.Cartao cartao = new Tema.Cartao(18, Tema.BRANCO, true, 18);
        cartao.setLayout(new BorderLayout());
        cartao.add(corpo, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        for (JButton b : acoes) {
            rodape.add(b);
        }
        JButton voltar = Tema.secundario("←  Voltar ao menu");
        voltar.addActionListener(e -> dispose());
        rodape.add(voltar);

        raiz.add(cabecalho, BorderLayout.NORTH);
        raiz.add(cartao, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    protected void exibir(int largura, int altura) {
        setSize(largura, altura);
        setLocationRelativeTo(null);
    }
}