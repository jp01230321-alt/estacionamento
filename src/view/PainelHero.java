package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

/** Cartão laranja com um carro desenhado em Java2D. */
public class PainelHero extends JPanel {

    private static final long serialVersionUID = 1L;

    public PainelHero() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Fundo laranja arredondado
        Shape cartao = new RoundRectangle2D.Float(0, 0, w, h, 28, 28);
        g2.setColor(Tema.LARANJA);
        g2.fill(cartao);

        // Marcas de pneu na parte de baixo
        g2.setClip(cartao);
        g2.clipRect(0, (int) (h * 0.68), w, h);
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(9f));
        for (int x = -h; x < w; x += 30) {
            g2.drawLine(x, h, x + (int) (h * 0.5), (int) (h * 0.6));
        }
        g2.setClip(null);

        // Selo "P" de estacionamento
        g2.setColor(Tema.INDIGO);
        g2.fill(new RoundRectangle2D.Float(24, 24, 44, 44, 12, 12));
        g2.setColor(Color.WHITE);
        g2.setFont(Tema.fonte(Font.BOLD, 26));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("P", 24 + (44 - fm.stringWidth("P")) / 2,
                24 + (44 + fm.getAscent() - fm.getDescent()) / 2);

        // Carro (desenhado em um espaço de 400 x 200 e escalado)
        double escala = Math.min(w * 0.86 / 400.0, h * 0.52 / 200.0);
        g2.translate((w - 400 * escala) / 2.0, h * 0.30);
        g2.scale(escala, escala);

        g2.setColor(new Color(150, 40, 10, 110));
        g2.fill(new Ellipse2D.Float(30, 168, 340, 26));

        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(25, 92, 350, 70, 34, 34));
        Path2D.Float cabine = new Path2D.Float();
        cabine.moveTo(95, 96);
        cabine.lineTo(135, 40);
        cabine.lineTo(262, 40);
        cabine.lineTo(325, 96);
        cabine.closePath();
        g2.fill(cabine);

        g2.setColor(Tema.INDIGO);
        Path2D.Float janela1 = new Path2D.Float();
        janela1.moveTo(110, 92);
        janela1.lineTo(140, 52);
        janela1.lineTo(188, 52);
        janela1.lineTo(188, 92);
        janela1.closePath();
        g2.fill(janela1);
        Path2D.Float janela2 = new Path2D.Float();
        janela2.moveTo(198, 92);
        janela2.lineTo(198, 52);
        janela2.lineTo(257, 52);
        janela2.lineTo(304, 92);
        janela2.closePath();
        g2.fill(janela2);

        g2.setColor(Tema.LARANJA);
        g2.fill(new RoundRectangle2D.Float(40, 126, 320, 8, 8, 8));

        g2.setColor(new Color(255, 214, 90));
        g2.fill(new RoundRectangle2D.Float(352, 104, 22, 14, 8, 8));
        g2.setColor(new Color(220, 50, 50));
        g2.fill(new RoundRectangle2D.Float(26, 104, 14, 14, 6, 6));

        desenharRoda(g2, 105, 162);
        desenharRoda(g2, 295, 162);

        g2.dispose();
    }

    private static void desenharRoda(Graphics2D g2, float cx, float cy) {
        g2.setColor(new Color(28, 28, 42));
        g2.fill(new Ellipse2D.Float(cx - 30, cy - 30, 60, 60));
        g2.setColor(new Color(205, 207, 216));
        g2.fill(new Ellipse2D.Float(cx - 17, cy - 17, 34, 34));
        g2.setColor(Tema.LARANJA);
        g2.fill(new Ellipse2D.Float(cx - 9, cy - 9, 18, 18));
        g2.setColor(new Color(28, 28, 42));
        g2.fill(new Ellipse2D.Float(cx - 3.5f, cy - 3.5f, 7, 7));
    }
}