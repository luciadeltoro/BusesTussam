package modelo;

public class DatoGPS {
    private String idAutobus;
    private String marcaTiempo; // Formato ISO 8601
    private double latitud;
    private double longitud;
    private double velocidad; // en km/h

    public DatoGPS(String idAutobus, String marcaTiempo, double latitud, double longitud, double velocidad) {
        this.idAutobus = idAutobus;
        this.marcaTiempo = marcaTiempo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
    }

    public String getIdAutobus() {
        return idAutobus;
    }

    public String getMarcaTiempo() {
        return marcaTiempo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getVelocidad() {
        return velocidad;
    }
    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return idAutobus + "," + marcaTiempo + "," + latitud + "," + longitud + "," + velocidad;
    }
}
