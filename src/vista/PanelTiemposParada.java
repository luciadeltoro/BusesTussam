package vista;

import javax.swing.*;
import java.awt.*;

public class PanelTiemposParada extends JPanel {
    private JLabel titulo;
    private JTextArea areaTiempos;
    private JButton botonVolver;

    public PanelTiemposParada() {
        setLayout(new BorderLayout());

        titulo = new JLabel("Tiempos de llegada", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        areaTiempos = new JTextArea();
        areaTiempos.setEditable(false);

        botonVolver = new JButton("Volver");

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(areaTiempos), BorderLayout.CENTER);
        add(botonVolver, BorderLayout.SOUTH);
    }

    public void mostrarTiempos(String texto) {
        areaTiempos.setText(texto);
    }

    public JButton getBotonVolver() {
        return botonVolver;
    }
}
