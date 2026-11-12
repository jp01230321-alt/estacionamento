package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import controller.EstacionamentoController;
import model.Perfil;

/** Acesso restrito a gerência e funcionários. */
public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private final transient EstacionamentoController controller;
    private final JComboBox<Perfil> cbPerfil = new JComboBox<>(Perfil.values());
    private final JPasswordField txtSenha = new JPasswordField();

    public TelaLogin(EstacionamentoController controller) {
        super("ParkControl — Acesso restrito");
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel raiz = new JPanel(new GridLayout(1, 2, 36, 0));
        raiz.setBackground(Tema.FUNDO);
        raiz.setBorder(new EmptyBorder(32, 40, 32, 40));
        raiz.add(montarEsquerda());
        raiz.add(new PainelHero());
        setContentPane(raiz);

        setSize(980, 580);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel montarEsquerda() {
        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setBorder(new EmptyBorder(0, 10, 0, 0));
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.add(Tema.esq(Tema.logo()));
        topo.add(Box.createVerticalStrut(30));
        topo.add(Tema.esq(Tema.titulo("Bem-vindo", 30)));
        topo.add(Tema.esq(Tema.titulo("de volta", 30)));
        topo.add(Box.createVerticalStrut(10));
        topo.add(Tema.esq(Tema.barraLaranja()));
        topo.add(Box.createVerticalStrut(12));
        topo.add(Tema.esq(Tema.texto("Acesso exclusivo para gerência e funcionários.")));

        Tema.combo(cbPerfil);
        Tema.campo(txtSenha);

        Tema.Cartao formulario = new Tema.Cartao(18, Tema.BRANCO, true, 16);
        formulario.setLayout(new GridLayout(2, 1, 0, 14));
        formulario.add(Tema.campoRotulado("Perfil de acesso", cbPerfil));
        formulario.add(Tema.campoRotulado("Senha", txtSenha));

        JButton btEntrar = Tema.primario("Entrar");
        JButton btFechar = Tema.secundario("Fechar sistema");
        btEntrar.addActionListener(e -> entrar());
        btFechar.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(btEntrar); // Enter confirma

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setOpaque(false);
        botoes.add(btEntrar);
        botoes.add(btFechar);

        JPanel meio = new JPanel(new BorderLayout(0, 18));
        meio.setOpaque(false);
        meio.add(formulario, BorderLayout.NORTH);
        meio.add(botoes, BorderLayout.CENTER);

        JPanel esquerda = new JPanel(new BorderLayout(0, 20));
        esquerda.setOpaque(false);
        esquerda.add(topo, BorderLayout.NORTH);
        esquerda.add(meio, BorderLayout.CENTER);
        return esquerda;
    }

    private void entrar() {
        Perfil perfil = (Perfil) cbPerfil.getSelectedItem();
        String senha = new String(txtSenha.getPassword());
        if (controller.autenticar(perfil, senha)) {
            new TelaPrincipal(controller).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Senha incorreta para o perfil selecionado.",
                    "Acesso negado", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
        }
    }
}