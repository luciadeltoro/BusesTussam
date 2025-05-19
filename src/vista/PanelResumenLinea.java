package vista;

import javax.swing.*;
import java.awt.*;

public class PanelResumenLinea extends JPanel {
    private JLabel titulo;
    private JTextArea areaResumen;
    private JButton botonVolver;

    public PanelResumenLinea() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Resumen de la línea", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        areaResumen = new JTextArea();
        areaResumen.setEditable(false);

        botonVolver = new JButton("Volver");

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);
        add(botonVolver, BorderLayout.SOUTH);
    }

    public void mostrarResumen(String texto) {
        areaResumen.setText(texto);
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
}
