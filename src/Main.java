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
                // usa o visual padrão
            }
            // Um único controller (e portanto um único Model) vive durante toda a execução,
            // então os dados permanecem ao fechar e reabrir as telas.
            EstacionamentoController controller = new EstacionamentoController();
            new TelaLogin(controller).setVisible(true);
        });
    }
}