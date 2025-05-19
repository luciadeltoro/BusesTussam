package vista;

import lectura.LectorDatosGPS;
import modelo.DatoGPS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//Ventana para poder ejecutar la parte visual del programa
public class Ventana extends JFrame {
    private CardLayout layout;
    private JPanel panelContenedor;

    // Paneles reutilizables
    private PanelSeleccionLinea panelSeleccion;
    private PanelTiemposParada panelTiemposParada;
    private PanelResumenLinea panelResumenLinea;
    private PanelAdmin panelAdmin;

    public Ventana() {
        setTitle("Buses Tussam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        panelContenedor = new JPanel(layout);

        // Leer datos de las tres líneas para panelAdmin
        ArrayList<DatoGPS> datosAdmin = new ArrayList<>();
        datosAdmin.addAll(LectorDatosGPS.leerDatosGPS("datos_simulados/linea27.csv"));
        datosAdmin.addAll(LectorDatosGPS.leerDatosGPS("datos_simulados/linea22.csv"));
        datosAdmin.addAll(LectorDatosGPS.leerDatosGPS("datos_simulados/lineaB4.csv"));

        // Crear paneles
        panelSeleccion = new PanelSeleccionLinea(this);
        panelTiemposParada = new PanelTiemposParada();
        panelResumenLinea = new PanelResumenLinea();
        panelAdmin = new PanelAdmin(this, datosAdmin);

        // Botones volver
        panelTiemposParada.getBotonVolver().addActionListener(e -> mostrarPanel("seleccionLinea"));
        panelResumenLinea.getBotonVolver().addActionListener(e -> mostrarPanel("seleccionLinea"));

        // Agregar paneles al contenedor
        panelContenedor.add(panelSeleccion, "seleccionLinea");
        panelContenedor.add(panelTiemposParada, "tiempos");
        panelContenedor.add(panelResumenLinea, "resumenLinea");
        panelContenedor.add(panelAdmin, "admin");

        add(panelContenedor);

        // Mostrar panel de administración por defecto (menú principal)
        layout.show(panelContenedor, "admin");
    }

    public void mostrarPanel(String nombre) {
        layout.show(panelContenedor, nombre);
    }

    public PanelTiemposParada getPanelTiemposParada() {
        return panelTiemposParada;
    }

    public PanelResumenLinea getPanelResumenLinea() {
        return panelResumenLinea;
    }

    public PanelAdmin getPanelAdmin() {
        return panelAdmin;
    }
}
