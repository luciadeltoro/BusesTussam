package simulacion;

import lectura.LectorDatosGPS;
import lectura.LectorParadas;
import modelo.DatoGPS;
import modelo.Parada;
import procesamiento.ExportadorJSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GeneradorDatosGPS {

    private static final String CARPETA_SALIDA = "datos_simulados/";
    private static final String CARPETA_JSON = "datos_simulados_json/";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Map<String, String> BUSES = Map.of(
            "BUS27", "27",
            "BUS22", "22",
            "BUSB4", "B4"
    );

    private static final int MINUTOS_SIMULADOS = 60;
    private static final double VELOCIDAD_KMH = 25.0;

    public static void main(String[] args) {
        File carpeta = new File(CARPETA_SALIDA);
        if (!carpeta.exists()) carpeta.mkdirs();

        List<Parada> paradasTotales = LectorParadas.leerParadas("datos/paradas.csv");
        Map<String, List<Parada>> paradasPorLinea = paradasTotales.stream()
                .collect(Collectors.groupingBy(Parada::getLinea));

        Map<String, List<String>> busesPorLinea = new HashMap<>();
        for (Map.Entry<String, String> entry : BUSES.entrySet()) {
            busesPorLinea.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        LocalDateTime tiempoInicio = LocalDateTime.of(2025, 5, 1, 8, 0);
        List<DatoGPS> todosLosDatos = new ArrayList<>();

        for (String linea : paradasPorLinea.keySet()) {
            List<Parada> paradasLinea = paradasPorLinea.get(linea);
            paradasLinea.sort(Comparator.comparingInt(Parada::getOrden));

            if (paradasLinea.size() < 2) continue;

            try (FileWriter escritor = new FileWriter(CARPETA_SALIDA + "linea" + linea + ".csv")) {
                escritor.write("idAutobus,marcaTiempo,latitud,longitud,velocidad\n");

                for (String idBus : busesPorLinea.getOrDefault(linea, List.of())) {
                    int minutoGlobal = 0;
                    int totalTramos = paradasLinea.size() - 1;
                    int minutosPorTramo = MINUTOS_SIMULADOS / totalTramos;

                    for (int i = 0; i < paradasLinea.size() - 1; i++) {
                        Parada origen = paradasLinea.get(i);
                        Parada destino = paradasLinea.get(i + 1);

                        for (int m = 0; m < minutosPorTramo; m++) {
                            double fraccion = (double) m / minutosPorTramo;
                            double lat = origen.getLatitud() + fraccion * (destino.getLatitud() - origen.getLatitud());
                            double lon = origen.getLongitud() + fraccion * (destino.getLongitud() - origen.getLongitud());

                            LocalDateTime marcaTiempo = tiempoInicio.plusMinutes(minutoGlobal++);
                            DatoGPS dato = new DatoGPS(idBus, marcaTiempo.format(FORMATO), lat, lon, VELOCIDAD_KMH);
                            todosLosDatos.add(dato);
                            escritor.write(dato.toString() + "\n");
                        }
                    }

                    Parada ultima = paradasLinea.get(paradasLinea.size() - 1);
                    LocalDateTime marcaTiempo = tiempoInicio.plusMinutes(minutoGlobal++);
                    DatoGPS datoFinal = new DatoGPS(idBus, marcaTiempo.format(FORMATO),
                            ultima.getLatitud(), ultima.getLongitud(), 0.0);
                    todosLosDatos.add(datoFinal);
                    escritor.write(datoFinal.toString() + "\n");
                }

                System.out.println("✅ Archivo generado: linea" + linea + ".csv");

            } catch (IOException e) {
                System.out.println("❌ Error escribiendo archivo para línea " + linea + ": " + e.getMessage());
            }
        }

        // Exportar JSON por bus
        ExportadorJSON.exportarDatosPorBus(todosLosDatos, CARPETA_JSON);
    }
}
