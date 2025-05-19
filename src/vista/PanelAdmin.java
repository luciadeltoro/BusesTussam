package vista;

import modelo.DatoGPS;
import procesamiento.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;



public class PanelAdmin extends JPanel {
    private JButton btnVelocidadMedia;
    private JButton btnContarParadas;
    private JButton btnFiltrarPorHora;
    private JButton btnUltimaPosicion;
    private JButton btnSimularCambio;
    private JButton btnArchivar;
    private JButton btnVolver;

    private Ventana ventana;
    private List<DatoGPS> datos;

    public PanelAdmin(Ventana ventana, List<DatoGPS> datos) {
        this.ventana = ventana;
        this.datos = datos;

        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        btnVelocidadMedia = new JButton("1. Ver velocidad media");
        btnContarParadas = new JButton("2. Ver paradas por autobús");
        btnFiltrarPorHora = new JButton("3. Filtrar por autobús y hora");
        btnUltimaPosicion = new JButton("4. Ver última posición desde JSON");
        btnSimularCambio = new JButton("5. Simular cambio de recorrido");
        btnArchivar = new JButton("6. Eliminar archivos antiguos");
        btnVolver = new JButton("Volver");

        add(btnVelocidadMedia);
        add(btnContarParadas);
        add(btnFiltrarPorHora);
        add(btnUltimaPosicion);
        add(btnSimularCambio);
        add(btnArchivar);
        add(btnVolver);

        configurarEventos();
    }

    private void configurarEventos() {
        btnVelocidadMedia.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            Map<String, List<Double>> velocidadesPorBus = new HashMap<>();

            for (DatoGPS dato : datos) {
                if (!ProcesadorDatosGPS.validarRegistro(dato)) continue;
                velocidadesPorBus
                        .computeIfAbsent(dato.getIdAutobus(), k -> new ArrayList<>())
                        .add(dato.getVelocidad());
            }

            for (String idBus : velocidadesPorBus.keySet()) {
                List<Double> velocidades = velocidadesPorBus.get(idBus);
                double suma = velocidades.stream().mapToDouble(Double::doubleValue).sum();
                double media = suma / velocidades.size();
                sb.append("Bus ").append(idBus.replace("BUS", "")).append(" - Velocidad media: ")
                        .append(String.format(Locale.US, "%.2f", media)).append(" km/h\n");
            }

            if (sb.isEmpty()) sb.append("No hay datos disponibles.");
            mostrarTexto("Velocidad media por autobús", sb.toString());
        });

        btnContarParadas.addActionListener(e -> ventana.mostrarPanel("seleccionLinea"));

        btnFiltrarPorHora.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog(this, "ID del autobús (ej. BUS27):");
                if (id == null) return;

                String fechaInicio = JOptionPane.showInputDialog(this, "Fecha de inicio (YYYY-MM-DD):");
                if (fechaInicio == null) return;
                String horaInicio = JOptionPane.showInputDialog(this, "Hora de inicio (HH:MM:SS):");
                if (horaInicio == null) return;
                String inicio = fechaInicio + "T" + horaInicio;

                String fechaFin = JOptionPane.showInputDialog(this, "Fecha de fin (YYYY-MM-DD):");
                if (fechaFin == null) return;
                String horaFin = JOptionPane.showInputDialog(this, "Hora de fin (HH:MM:SS):");
                if (horaFin == null) return;
                String fin = fechaFin + "T" + horaFin;

                List<DatoGPS> filtrados = ProcesadorDatosGPS.filtrarPorBusYRango(datos, id, inicio, fin);
                if (filtrados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (DatoGPS d : filtrados) {
                        sb.append(d).append("\n");
                    }
                    mostrarTexto("Resultados filtrados", sb.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error en el formato de fecha/hora.");
            }
        });

        btnUltimaPosicion.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "ID del autobús (ej. BUS27):");
            if (id == null) return;
            String ruta = "datos_simulados_json/" + id.toUpperCase() + ".json";
            ExportadorJSON.mostrarDesdeJSON(ruta);
        });

        btnSimularCambio.addActionListener(e -> {
            try {
                String id = JOptionPane.showInputDialog(this, "ID del autobús:");
                if (id == null) return;

                String fechaInicio = JOptionPane.showInputDialog(this, "Fecha de inicio (YYYY-MM-DD):");
                if (fechaInicio == null) return;
                String horaInicio = JOptionPane.showInputDialog(this, "Hora de inicio (HH:MM:SS):");
                if (horaInicio == null) return;
                String inicio = fechaInicio + "T" + horaInicio;

                String fechaFin = JOptionPane.showInputDialog(this, "Fecha de fin (YYYY-MM-DD):");
                if (fechaFin == null) return;
                String horaFin = JOptionPane.showInputDialog(this, "Hora de fin (HH:MM:SS):");
                if (horaFin == null) return;
                String fin = fechaFin + "T" + horaFin;

                double dLat = Double.parseDouble(JOptionPane.showInputDialog(this, "Desplazamiento LATITUD:"));
                double dLon = Double.parseDouble(JOptionPane.showInputDialog(this, "Desplazamiento LONGITUD:"));

                EditorRecorrido.modificarRecorrido("src/datos_gps.csv", id, inicio, fin, dLat, dLon);
                JOptionPane.showMessageDialog(this, "Recorrido modificado.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error en los datos.");
            }
        });

        btnArchivar.addActionListener(e -> Archivador.eliminarArchivosAntiguos("datos_simulados", 7));
        btnVolver.addActionListener(e -> ventana.mostrarPanel("seleccionLinea"));
    }

    private void mostrarTexto(String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
