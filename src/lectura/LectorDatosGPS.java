package lectura;

import modelo.DatoGPS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorDatosGPS {

    public static ArrayList<DatoGPS> leerDatosGPS(String rutaArchivo) {
        ArrayList<DatoGPS> lista = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            lector.readLine();

            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.trim().split(",");
                if (partes.length == 5) {
                    try {
                        String id = partes[0].trim();
                        String timestamp = partes[1].trim();
                        double lat = Double.parseDouble(partes[2].trim());
                        double lon = Double.parseDouble(partes[3].trim());
                        double velocidad = Double.parseDouble(partes[4].trim());

                        lista.add(new DatoGPS(id, timestamp, lat, lon, velocidad));
                    } catch (NumberFormatException e) {
                        System.out.println("Línea con error numérico: " + linea);
                    }
                } else {
                    System.out.println("Línea mal formada: " + linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
