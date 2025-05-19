package procesamiento;

import javax.swing.*;
import java.io.File;
import java.time.Instant;

//Clase para eliminar archivos antiguos.
public class Archivador {

    public static void eliminarArchivosAntiguos(String rutaCarpeta, int diasMaximos) {
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            JOptionPane.showMessageDialog(null, "❌ La carpeta no existe: " + rutaCarpeta);
            return;
        }

        long ahora = Instant.now().toEpochMilli();
        long limite = ahora - (long) diasMaximos * 24 * 60 * 60 * 1000;
        int eliminados = 0;

        for (File archivo : carpeta.listFiles()) {
            if (archivo.isFile() && archivo.lastModified() < limite) {
                if (archivo.delete()) {
                    eliminados++;
                    System.out.println("🗑️ Eliminado: " + archivo.getName());
                }
            }
        }

        String mensaje = (eliminados > 0)
                ? "🧹 Archivos eliminados: " + eliminados
                : "✅ No hay archivos antiguos para eliminar.";

        JOptionPane.showMessageDialog(null, mensaje);
    }
}
