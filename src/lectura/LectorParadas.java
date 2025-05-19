package lectura;

import modelo.Parada;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LectorParadas {

    public static ArrayList<Parada> leerParadas(String rutaArchivo) {
        ArrayList<Parada> paradas = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaArchivo), StandardCharsets.UTF_8))) {

            String linea;
            lector.readLine();

            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.trim().split(",");
                if (partes.length == 5) {
                    try {
                        String nombre = partes[0].trim();
                        String lineaBus = partes[1].trim();
                        double lat = Double.parseDouble(partes[2].trim());
                        double lon = Double.parseDouble(partes[3].trim());
                        int orden = Integer.parseInt(partes[4].trim());

                        paradas.add(new Parada(nombre, lineaBus, lat, lon, orden));
                    } catch (NumberFormatException e) {
                        System.out.println("Error en datos numéricos: " + linea);
                    }
                } else {
                    System.out.println("Línea mal formada: " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paradas;
    }
}
