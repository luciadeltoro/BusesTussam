package procesamiento;

import modelo.DatoGPS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProcesadorDatosGPS {

    // Validar un registro GPS
    public static boolean validarRegistro(DatoGPS dato) {
        try {
            LocalDateTime.parse(dato.getMarcaTiempo());
            if (dato.getLatitud() < -90 || dato.getLatitud() > 90) return false;
            if (dato.getLongitud() < -180 || dato.getLongitud() > 180) return false;
            if (dato.getVelocidad() < 0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Calcular velocidad media por bus
    public static void calcularVelocidadMedia(List<DatoGPS> datos) {
        Map<String, List<Double>> velocidadesPorBus = new HashMap<>();

        for (DatoGPS dato : datos) {
            if (!validarRegistro(dato)) continue;

            velocidadesPorBus
                    .computeIfAbsent(dato.getIdAutobus(), k -> new ArrayList<>())
                    .add(dato.getVelocidad());
        }

        for (String idBus : velocidadesPorBus.keySet()) {
            List<Double> velocidades = velocidadesPorBus.get(idBus);
            double suma = velocidades.stream().mapToDouble(Double::doubleValue).sum();
            double media = suma / velocidades.size();
            System.out.printf(Locale.US, "Bus %s - Velocidad media: %.2f km/h\n", idBus, media);
        }
    }

    // Contar paradas (velocidad = 0)
    public static void contarParadas(List<DatoGPS> datos) {
        Map<String, Integer> paradas = new HashMap<>();

        for (DatoGPS dato : datos) {
            if (!validarRegistro(dato)) continue;
            if (dato.getVelocidad() == 0) {
                paradas.put(dato.getIdAutobus(),
                        paradas.getOrDefault(dato.getIdAutobus(), 0) + 1);
            }
        }

        for (String id : paradas.keySet()) {
            System.out.println("Bus " + id + " - Paradas: " + paradas.get(id));
        }
    }

    // Filtrar datos por bus y rango horario
    public static List<DatoGPS> filtrarPorBusYRango(List<DatoGPS> datos, String busId, String inicioStr, String finStr) {
        List<DatoGPS> resultado = new ArrayList<>();
        DateTimeFormatter formato = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime inicio = LocalDateTime.parse(inicioStr, formato);
        LocalDateTime fin = LocalDateTime.parse(finStr, formato);

        for (DatoGPS d : datos) {
            if (!d.getIdAutobus().equalsIgnoreCase(busId)) continue;
            LocalDateTime timestamp = LocalDateTime.parse(d.getMarcaTiempo());
            if (!timestamp.isBefore(inicio) && !timestamp.isAfter(fin)) {
                resultado.add(d);
            }
        }

        return resultado;
    }

    // Obtener último dato de cada bus
    public static Map<String, DatoGPS> obtenerUltimaPosicionPorBus(List<DatoGPS> datos) {
        Map<String, DatoGPS> ultimos = new HashMap<>();
        for (DatoGPS d : datos) {
            ultimos.put(d.getIdAutobus(), d); // sobrescribe, quedando el último
        }
        return ultimos;
    }
}
