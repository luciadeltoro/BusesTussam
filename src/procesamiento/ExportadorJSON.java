package procesamiento;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.DatoGPS;
import modelo.Parada;
import java.awt.Dimension;
import javax.swing.*;
import java.io.*;
import java.util.*;

//Clase para exportar la información de los autobuses en archivos JSON
public class ExportadorJSON {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Metodo para exportar lista completa de datos GPS a un solo archivo
    public static void exportarDatosGPS(List<DatoGPS> datos, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(datos, writer);
            System.out.println("✅ Datos GPS exportados a JSON: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("❌ Error al exportar JSON: " + e.getMessage());
        }
    }

    // Metodo para exportar lista completa de paradas a JSON
    public static void exportarParadas(List<Parada> paradas, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(paradas, writer);
            System.out.println("✅ Paradas exportadas a JSON: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("❌ Error al exportar JSON: " + e.getMessage());
        }
    }

    // Metodo para exportar un archivo JSON separado por cada autobús
    public static void exportarDatosPorBus(List<DatoGPS> datos, String carpetaDestino) {
        Map<String, List<DatoGPS>> datosPorBus = new HashMap<>();

        for (DatoGPS dato : datos) {
            datosPorBus.computeIfAbsent(dato.getIdAutobus(), k -> new ArrayList<>()).add(dato);
        }

        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) carpeta.mkdirs();

        for (Map.Entry<String, List<DatoGPS>> entry : datosPorBus.entrySet()) {
            String idBus = entry.getKey();
            List<DatoGPS> datosBus = entry.getValue();

            String nombreArchivo = carpetaDestino + File.separator + idBus + ".json";

            try (FileWriter writer = new FileWriter(nombreArchivo)) {
                gson.toJson(datosBus, writer);
                System.out.println("✅ Exportado: " + nombreArchivo);
            } catch (IOException e) {
                System.out.println("❌ Error al exportar " + idBus + ": " + e.getMessage());
            }
        }
    }

    // Metodo para mostrar el contenido de un JSON en un JOptionPane
    public static void mostrarDesdeJSON(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            JTextArea area = new JTextArea(contenido.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(null, scroll, "Contenido de " + rutaArchivo, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "❌ Error al leer JSON: " + e.getMessage());
        }
    }
}
