package vista;

import lectura.LectorParadas;
import modelo.Parada;
import procesamiento.ControladorTiempos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class PanelSeleccionLinea extends JPanel {

    private JComboBox<String> comboLinea;
    private JComboBox<String> comboParada;
    private JButton botonConsultar;
    private JButton botonVolver;

    private Ventana ventana;
    private List<Parada> todasLasParadas;

    public PanelSeleccionLinea(Ventana ventana) {
        this.ventana = ventana;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Consulta de Autobuses");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Línea:"), gbc);

        comboLinea = new JComboBox<>(new String[]{"27", "22", "B4"});
        gbc.gridx = 1;
        add(comboLinea, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Parada:"), gbc);

        comboParada = new JComboBox<>();
        comboParada.setEnabled(false);
        gbc.gridx = 1;
        add(comboParada, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        botonConsultar = new JButton("Consultar llegada");
        botonConsultar.setEnabled(false);
        add(botonConsultar, gbc);

        gbc.gridy++;
        botonVolver = new JButton("Volver");
        add(botonVolver, gbc);

        // Leer todas las paradas desde archivo
        todasLasParadas = LectorParadas.leerParadas("datos/paradas.csv");

        // Al cambiar de línea, cargar paradas
        comboLinea.addActionListener(e -> cargarParadasDeLinea());

        // Acción del botón "Consultar llegada"
        botonConsultar.addActionListener((ActionEvent e) -> {
            String linea = (String) comboLinea.getSelectedItem();
            String parada = (String) comboParada.getSelectedItem();

            if (linea != null && parada != null) {
                String resultado = ControladorTiempos.calcularTiemposLlegada(linea, parada);

                PanelTiemposParada panelTiempos = ventana.getPanelTiemposParada();
                panelTiempos.mostrarTiempos(resultado);

                ventana.mostrarPanel("tiempos");
            }
        });

        // Acción del botón "Volver" al menú principal
        botonVolver.addActionListener(e -> ventana.mostrarPanel("admin"));
    }

    private void cargarParadasDeLinea() {
        String lineaSeleccionada = (String) comboLinea.getSelectedItem();

        if (lineaSeleccionada != null) {
            List<String> paradasLinea = todasLasParadas.stream()
                    .filter(p -> p.getLinea().equals(lineaSeleccionada))
                    .sorted((p1, p2) -> Integer.compare(p1.getOrden(), p2.getOrden()))
                    .map(Parada::getNombre)
                    .distinct()
                    .collect(Collectors.toList());

            comboParada.removeAllItems();
            for (String parada : paradasLinea) {
                comboParada.addItem(parada);
            }

            comboParada.setEnabled(true);
            botonConsultar.setEnabled(true);
        }
    }
}
