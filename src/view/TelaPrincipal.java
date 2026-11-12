package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import controller.EstacionamentoController;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int ALTURA_HERO = 260;

    private final transient EstacionamentoController controller;
    private final JLabel lblEstacionados = valorGrande();
    private final JLabel lblVagas = valorGrande();
    private final JLabel lblTarifa = valorGrande();

    public TelaPrincipal(EstacionamentoController controller) {
        super("ParkControl — Sistema de Controle de Estacionamento");
        this.controller = controller;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        
        JPanel raiz = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Tema.FUNDO);
                g.fillRect(0, 0, getWidth(), 290);
            }
        };

        raiz.add(montarCabecalho(), BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(0, 18));
        corpo.setOpaque(false);
        corpo.setBorder(new EmptyBorder(6, 40, 24, 40));

        JPanel hero = new JPanel(new GridLayout(1, 2, 40, 0));
        hero.setOpaque(false);
        hero.setPreferredSize(new Dimension(100, ALTURA_HERO));
        hero.add(montarTextoHero());
        hero.add(new PainelHero());
        corpo.add(hero, BorderLayout.NORTH);

        JPanel inferior = new JPanel(new BorderLayout(0, 22));
        inferior.setOpaque(false);
        inferior.add(montarBarraResumo(), BorderLayout.NORTH);
        inferior.add(montarPassos(), BorderLayout.CENTER);
        corpo.add(inferior, BorderLayout.CENTER);

        raiz.add(corpo, BorderLayout.CENTER);
        setContentPane(raiz);

        atualizarResumo();
        setSize(1000, 750);
        setLocationRelativeTo(null);
    }

   

    private JPanel montarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);
        cabecalho.setBorder(new EmptyBorder(14, 40, 14, 40));
        cabecalho.add(Tema.logo(), BorderLayout.WEST);

        JButton btEntrada = Tema.link("Entrada");
        JButton btConsulta = Tema.link("Consulta");
        JButton btSaida = Tema.link("Saída");
        JButton btRelatorio = Tema.link("Relatório");
        btEntrada.addActionListener(e -> abrir(new TelaEntrada(this.controller, this)));
        btConsulta.addActionListener(e -> abrir(new TelaConsulta(this.controller, this)));
        btSaida.addActionListener(e -> abrir(new TelaSaida(this.controller, this)));
        btRelatorio.addActionListener(e -> abrir(new TelaRelatorio(this.controller, this)));
        
        if (!controller.podeVerRelatorio()) {
            btRelatorio.setEnabled(false);
            btRelatorio.setToolTipText("Disponível somente para a gerência");
        }

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 22, 8));
        menu.setOpaque(false);
        menu.add(btEntrada);
        menu.add(btConsulta);
        menu.add(btSaida);
        menu.add(btRelatorio);
        cabecalho.add(menu, BorderLayout.CENTER);

        JLabel usuario = new JLabel(String.valueOf(controller.getPerfilAtual()));
        usuario.setFont(Tema.fonte(Font.BOLD, 14));
        usuario.setForeground(Tema.TEXTO);
        JButton btSessao = Tema.primario("Encerrar sessão");
        btSessao.addActionListener(e -> encerrarSessao());
        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        direita.setOpaque(false);
        direita.add(usuario);
        direita.add(btSessao);
        cabecalho.add(direita, BorderLayout.EAST);
        return cabecalho;
    }

    private JPanel montarTextoHero() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(18, 0, 0, 0));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(Tema.esq(Tema.titulo("Controle seu", 26)));
        p.add(Tema.esq(Tema.titulo("estacionamento", 26)));
        p.add(Tema.esq(Tema.titulo("com mais agilidade", 26)));
        p.add(Box.createVerticalStrut(10));
        p.add(Tema.esq(Tema.barraLaranja()));
        p.add(Box.createVerticalStrut(12));
        p.add(Tema.esq(Tema.texto("Registre entradas e saídas, acompanhe as vagas e")));
        p.add(Tema.esq(Tema.texto("calcule a cobrança de cada veículo em segundos.")));
        p.add(Box.createVerticalStrut(18));

        JButton btEntrada = Tema.primario("Registrar entrada");
        JButton btSaida = Tema.secundario("Registrar saída");
        btEntrada.addActionListener(e -> abrir(new TelaEntrada(this.controller, this)));
        btSaida.addActionListener(e -> abrir(new TelaSaida(this.controller, this)));
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setOpaque(false);
        botoes.add(btEntrada);
        botoes.add(btSaida);
        botoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        p.add(Tema.esq(botoes));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel montarBarraResumo() {
        Tema.Cartao barra = new Tema.Cartao(16, Tema.BRANCO, true, 8);
        barra.setLayout(new BorderLayout(20, 0));

        JPanel itens = new JPanel(new GridLayout(1, 3, 0, 0));
        itens.setOpaque(false);
        itens.add(itemResumo("VEÍCULOS ESTACIONADOS", lblEstacionados, false));
        itens.add(itemResumo("VAGAS DISPONÍVEIS", lblVagas, true));
        itens.add(itemResumo("TARIFA POR HORA", lblTarifa, true));
        barra.add(itens, BorderLayout.CENTER);

        JButton btConsulta = Tema.primario("Consultar veículos");
        btConsulta.addActionListener(e -> abrir(new TelaConsulta(this.controller, this)));
        JPanel lado = new JPanel(new BorderLayout());
        lado.setOpaque(false);
        lado.setBorder(new EmptyBorder(4, 0, 4, 4));
        lado.add(btConsulta, BorderLayout.CENTER);
        barra.add(lado, BorderLayout.EAST);
        return barra;
    }

    private JPanel itemResumo(String rotulo, JLabel valor, boolean separador) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, separador ? 1 : 0, 0, 0, Tema.BORDA),
                new EmptyBorder(2, 18, 2, 10)));
        p.add(Tema.rotulo(rotulo));
        p.add(valor);
        return p;
    }

    private JPanel montarPassos() {
        JPanel secao = new JPanel(new BorderLayout(0, 18));
        secao.setOpaque(false);

        JLabel pequeno = Tema.rotulo("COMO FUNCIONA");
        pequeno.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel grande = Tema.titulo("Gerencie em 3 passos", 24);
        grande.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 4));
        titulos.setOpaque(false);
        titulos.add(pequeno);
        titulos.add(grande);
        secao.add(titulos, BorderLayout.NORTH);

        JPanel passos = new JPanel(new GridLayout(1, 3, 30, 0));
        passos.setOpaque(false);
        passos.add(criarPasso("1", "Registre a entrada",
                "Informe placa, modelo, data e", "horário de chegada do veículo.", false,
                () -> abrir(new TelaEntrada(this.controller, this))));
        passos.add(criarPasso("2", "Acompanhe as vagas",
                "Consulte os veículos estacionados", "e pesquise por placa.", true,
                () -> abrir(new TelaConsulta(this.controller, this))));
        passos.add(criarPasso("3", "Registre a saída",
                "Calcule a permanência e a cobrança", "por hora ou fração.", false,
                () -> abrir(new TelaSaida(this.controller, this))));
        secao.add(passos, BorderLayout.CENTER);
        return secao;
    }

    private JPanel criarPasso(String numero, String titulo, String linha1, String linha2,
            boolean destaque, Runnable acao) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Ladrilho ladrilho = new Ladrilho(numero, destaque);
        ladrilho.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel t = Tema.titulo(titulo, 16);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d1 = Tema.texto(linha1);
        d1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d2 = Tema.texto(linha2);
        d2.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(ladrilho);
        p.add(Box.createVerticalStrut(8));
        p.add(t);
        p.add(Box.createVerticalStrut(6));
        p.add(d1);
        p.add(d2);
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                acao.run();
            }
        });
        return p;
    }

    private static JLabel valorGrande() {
        JLabel l = new JLabel("-");
        l.setFont(Tema.fonte(Font.BOLD, 22));
        l.setForeground(Tema.TEXTO);
        return l;
    }

  
    public void atualizarResumo() {
        lblEstacionados.setText(String.valueOf(controller.getQuantidadeEstacionados()));
        lblVagas.setText(controller.getVagasDisponiveis() + " de " + controller.getCapacidade());
        lblTarifa.setText(controller.getTarifa());
    }

    private void abrir(TelaBase tela) {
        setVisible(false);
        tela.setVisible(true);
    }

    private void encerrarSessao() {
        controller.sair();
        new TelaLogin(controller).setVisible(true);
        dispose();
    }

   
    private static class Ladrilho extends JPanel {

        private static final long serialVersionUID = 1L;
        private final String numero;
        private final boolean destaque;

        Ladrilho(String numero, boolean destaque) {
            this.numero = numero;
            this.destaque = destaque;
            setOpaque(false);
            Dimension d = new Dimension(72, 76);
            setPreferredSize(d);
            setMaximumSize(d);
            setMinimumSize(d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(40, 40, 100, 18));
            g2.fill(new RoundRectangle2D.Float(4, 8, 64, 64, 18, 18));
            g2.setColor(destaque ? Tema.LARANJA : new Color(0xF3F3F6));
            g2.fill(new RoundRectangle2D.Float(4, 4, 64, 64, 18, 18));
            g2.setColor(destaque ? Color.WHITE : Tema.LARANJA);
            g2.setFont(Tema.fonte(Font.BOLD, 26));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(numero, 4 + (64 - fm.stringWidth(numero)) / 2,
                    4 + (64 + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }
}
