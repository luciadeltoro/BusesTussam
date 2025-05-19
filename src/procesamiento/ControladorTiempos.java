package procesamiento;

import lectura.LectorDatosGPS;
import lectura.LectorParadas;
import modelo.DatoGPS;
import modelo.Parada;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

//Clase para controlar los tiempos entre buses y paradas
public class ControladorTiempos {

    private static final String RUTA_DATOS_GPS = "datos_simulados/";
    private static final String RUTA_PARADAS = "datos/paradas.csv";

    public static String calcularTiemposLlegada(String linea, String nombreParada) {
        StringBuilder resultado = new StringBuilder();

        String archivoLinea = RUTA_DATOS_GPS + "linea" + linea + ".csv";
        File archivo = new File(archivoLinea);
        if (!archivo.exists()) {
            return "❌ No se encontró el archivo para la línea " + linea;
        }

        List<DatoGPS> datosGPS = LectorDatosGPS.leerDatosGPS(archivoLinea);
        if (datosGPS.isEmpty()) {
            return "⚠️ No hay datos GPS disponibles para la línea " + linea;
        }

        List<Parada> paradas = LectorParadas.leerParadas(RUTA_PARADAS);
        List<Parada> paradasLinea = paradas.stream()
                .filter(p -> p.getLinea().equals(linea))
                .collect(Collectors.toList());

        Optional<Parada> paradaSeleccionada = paradasLinea.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreParada))
                .findFirst();

        if (paradaSeleccionada.isEmpty()) {
            return "❌ Parada no encontrada para la línea " + linea;
        }

        Parada destino = paradaSeleccionada.get();

        Map<String, DatoGPS> ultimaPosicionPorBus = new HashMap<>();
        for (DatoGPS dato : datosGPS) {
            if (dato.getVelocidad() > 0) {
                ultimaPosicionPorBus.put(dato.getIdAutobus(), dato);
            }
        }

        if (ultimaPosicionPorBus.isEmpty()) {
            return "⚠️ No hay buses en movimiento en esta línea.";
        }

        for (Map.Entry<String, DatoGPS> entry : ultimaPosicionPorBus.entrySet()) {
            String idBus = entry.getKey();
            DatoGPS gps = entry.getValue();
            int minutos = EstimadorLlegada.estimarMinutosRestantes(gps, destino);

            String idLimpio = idBus.replaceAll("BUS", "");

            if (minutos < 0) {
                resultado.append("Bus ").append(idLimpio).append(": parado o sin datos válidos\n");
            } else {
                resultado.append("Bus ").append(idLimpio).append(": llega en ").append(minutos).append(" min\n");
            }
        }

        return resultado.toString();
    }
}
