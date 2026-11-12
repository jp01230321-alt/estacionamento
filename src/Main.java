import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import controller.EstacionamentoController;
import view.TelaLogin;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
               
            }
           
            EstacionamentoController controller = new EstacionamentoController();
            new TelaLogin(controller).setVisible(true);
        });
    }
}
