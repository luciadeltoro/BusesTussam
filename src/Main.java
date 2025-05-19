import vista.Ventana;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new Ventana().setVisible(true);
        });    }
}
