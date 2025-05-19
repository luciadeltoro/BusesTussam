package procesamiento;

import java.io.File;
import java.time.Instant;
import java.util.Date;

public class Archivador {

    public static void eliminarArchivosAntiguos(String rutaCarpeta, int diasMaximos) {
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists() || !carpeta.isDirectory()) return;

        long ahora = Instant.now().toEpochMilli();
        long limite = ahora - (long) diasMaximos * 24 * 60 * 60 * 1000;

        for (File archivo : carpeta.listFiles()) {
            if (archivo.isFile() && archivo.lastModified() < limite) {
                if (archivo.delete()) {
                    System.out.println("🗑️ Eliminado: " + archivo.getName());
                }
            }
        }
    }
}
