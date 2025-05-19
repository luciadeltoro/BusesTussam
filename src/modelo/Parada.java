package modelo;

public class Parada {
    private String nombre;
    private String linea;
    private double latitud;
    private double longitud;
    private int orden;

    public Parada(String nombre, String linea, double latitud, double longitud, int orden) {
        this.nombre = nombre;
        this.linea = linea;
        this.latitud = latitud;
        this.longitud = longitud;
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLinea() {
        return linea;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public int getOrden() {
        return orden;
    }

    @Override
    public String toString() {
        return orden + ". " + nombre + " (" + latitud + ", " + longitud + ")";
    }
}
