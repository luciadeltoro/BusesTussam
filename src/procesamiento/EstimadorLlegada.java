package procesamiento;

import modelo.DatoGPS;
import modelo.Parada;

public class EstimadorLlegada {

    private static final double RADIO_TIERRA_KM = 6371.0;

    // Fórmula de Haversine para calcular distancia entre dos puntos GPS
    public static double calcularDistancia(Parada parada, DatoGPS posicionBus) {
        double lat1 = Math.toRadians(parada.getLatitud());
        double lon1 = Math.toRadians(parada.getLongitud());
        double lat2 = Math.toRadians(posicionBus.getLatitud());
        double lon2 = Math.toRadians(posicionBus.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }

    // Estimar minutos restantes usando velocidad actual (en km/h)
    public static int estimarMinutosRestantes(DatoGPS posicionBus, Parada destino) {
        double distancia = calcularDistancia(destino, posicionBus); // en km
        double velocidad = posicionBus.getVelocidad();

        if (velocidad <= 0) return -1; // el bus está parado o sin datos válidos

        double tiempoHoras = distancia / velocidad;
        return (int) Math.round(tiempoHoras * 60);
    }

    // Estimar minutos usando una velocidad fija (por si no se confía en la real)
    public static int estimarMinutosConVelocidadFija(Parada destino, DatoGPS posicionBus, double velocidadFija) {
        double distancia = calcularDistancia(destino, posicionBus);
        double tiempoHoras = distancia / velocidadFija;
        return (int) Math.round(tiempoHoras * 60);
    }
}
