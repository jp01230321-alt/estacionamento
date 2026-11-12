package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

public final class Tema {

    private Tema() { }

   
    public static final Color LARANJA = new Color(0xFF5B35);
    public static final Color LARANJA_CLARO = new Color(0xFFE6DE);
    public static final Color INDIGO = new Color(0x464A9E);
    public static final Color INDIGO_ESCURO = new Color(0x373A82);
    public static final Color FUNDO = new Color(0xEFEFF2);
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x15151F);
    public static final Color TEXTO_SUAVE = new Color(0x7B8090);
    public static final Color BORDA = new Color(0xDADCE3);
    public static final Color VERDE = new Color(0x1E9E62);

    public static Font fonte(int estilo, int tamanho) {
        return new Font("Segoe UI", estilo, tamanho);
    }

 
    public static JLabel titulo(String texto, int tamanho) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.BOLD, tamanho));
        l.setForeground(TEXTO);
        return l;
    }

    public static JLabel texto(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.PLAIN, 14));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    public static JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte(Font.BOLD, 12));
        l.setForeground(TEXTO_SUAVE);
        return l;
    }

    public static <T extends JComponent> T esq(T componente) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        return componente;
    }

    public static JPanel barraLaranja() {
        JPanel p = new JPanel();
        p.setBackground(LARANJA);
        Dimension d = new Dimension(44, 4);
        p.setPreferredSize(d);
        p.setMaximumSize(d);
        p.setMinimumSize(d);
        return p;
    }

    public static JPanel logo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel nome = titulo("ParkControl", 24);
        JLabel ponto = titulo(".", 24);
        ponto.setForeground(LARANJA);
        p.add(nome);
        p.add(ponto);
        return p;
    }

   
    public static void campo(JTextField c) {
        c.setFont(fonte(Font.PLAIN, 15));
        c.setForeground(TEXTO);
        c.setBackground(BRANCO);
        c.setCaretColor(INDIGO);
        c.setBorder(BorderFactory.createCompoundBorder(
                new BordaArredondada(12, BORDA), new EmptyBorder(9, 12, 9, 12)));
    }

    public static void combo(JComboBox<?> c) {
        c.setFont(fonte(Font.PLAIN, 14));
        c.setBackground(BRANCO);
        c.setForeground(TEXTO);
    }

    public static JPanel campoRotulado(String rotulo, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.add(rotulo(rotulo), BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

   
    public static Botao primario(String texto) { return new Botao(texto, Botao.Tipo.PRIMARIO); }
    public static Botao secundario(String texto) { return new Botao(texto, Botao.Tipo.SECUNDARIO); }
    public static Botao destaque(String texto) { return new Botao(texto, Botao.Tipo.LARANJA); }

    
    public static JButton link(String texto) {
        JButton b = new JButton(texto);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setFont(fonte(Font.PLAIN, 15));
        b.setForeground(TEXTO);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setForeground(LARANJA);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setForeground(TEXTO);
            }
        });
        return b;
    }

  
    public static class Botao extends JButton {

        private static final long serialVersionUID = 1L;

        public enum Tipo { PRIMARIO, SECUNDARIO, LARANJA }

        private final Tipo tipo;

        public Botao(String texto, Tipo tipo) {
            super(texto);
            this.tipo = tipo;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setFont(fonte(Font.BOLD, 14));
            setForeground(tipo == Tipo.SECUNDARIO ? TEXTO : BRANCO);
            setBorder(new EmptyBorder(11, 22, 11, 22));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean hover = getModel().isRollover();
            Color base;
            if (!isEnabled()) {
                base = new Color(0xD5D7DE);
            } else if (tipo == Tipo.PRIMARIO) {
                base = hover ? INDIGO_ESCURO : INDIGO;
            } else if (tipo == Tipo.LARANJA) {
                base = hover ? LARANJA.darker() : LARANJA;
            } else {
                base = hover ? new Color(0xF3F3F6) : BRANCO;
            }
            if (getModel().isPressed()) {
                base = base.darker();
            }
            g2.setColor(base);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            if (tipo == Tipo.SECUNDARIO) {
                g2.setColor(BORDA);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 14, 14));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class Cartao extends JPanel {

        private static final long serialVersionUID = 1L;
        private static final int MARGEM_SOMBRA = 10;
        private final int raio;
        private final Color fundo;
        private final boolean sombra;

        public Cartao(int raio, Color fundo, boolean sombra, int padding) {
            this.raio = raio;
            this.fundo = fundo;
            this.sombra = sombra;
            setOpaque(false);
            int m = sombra ? MARGEM_SOMBRA : 0;
            setBorder(new EmptyBorder(m + padding, m + padding, m + padding, m + padding));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int m = sombra ? MARGEM_SOMBRA : 0;
            int w = getWidth() - 2 * m;
            int h = getHeight() - 2 * m;
            if (sombra) {
                for (int i = 0; i < m; i++) {
                    g2.setColor(new Color(40, 40, 100, 6));
                    g2.fill(new RoundRectangle2D.Float(m - i, m - i + 2, w + 2 * i, h + 2 * i,
                            raio + i * 2, raio + i * 2));
                }
            }
            g2.setColor(fundo);
            g2.fill(new RoundRectangle2D.Float(m, m, w, h, raio, raio));
            g2.dispose();
        }
    }

   
    public static class BordaArredondada extends AbstractBorder {

        private static final long serialVersionUID = 1L;
        private final int raio;
        private final Color cor;

        public BordaArredondada(int raio, Color cor) {
            this.raio = raio;
            this.cor = cor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int largura, int altura) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, largura - 1, altura - 1, raio, raio));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }
}
