package procesamiento;

import modelo.DatoGPS;
import lectura.LectorDatosGPS;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

//Clase para editar el recorrido de un autobús
public class EditorRecorrido {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void modificarRecorrido(String ruta, String idBus, String inicioStr, String finStr, double dLat, double dLon) {
        List<DatoGPS> datos = LectorDatosGPS.leerDatosGPS(ruta);
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, FORMATO);
        LocalDateTime fin = LocalDateTime.parse(finStr, FORMATO);

        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println("idAutobus,marcaTiempo,latitud,longitud,velocidad");

            for (DatoGPS d : datos) {
                LocalDateTime t = LocalDateTime.parse(d.getMarcaTiempo());

                if (d.getIdAutobus().equalsIgnoreCase(idBus) && !t.isBefore(inicio) && !t.isAfter(fin)) {
                    double nuevaLat = d.getLatitud() + dLat;
                    double nuevaLon = d.getLongitud() + dLon;
                    writer.printf(Locale.US, "%s,%s,%.6f,%.6f,%.1f%n",
                            d.getIdAutobus(), d.getMarcaTiempo(), nuevaLat, nuevaLon, d.getVelocidad());
                } else {
                    writer.println(d.toString());
                }
            }

            System.out.println("✅ Recorrido modificado correctamente.");
        } catch (IOException e) {
            System.out.println("❌ Error al escribir el archivo: " + e.getMessage());
        }
    }
}
