package vista;

import lectura.LectorDatosGPS;
import modelo.DatoGPS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Ventana extends JFrame {
    private CardLayout layout;
    private JPanel panelContenedor;

    // Paneles únicos y reutilizables
    private PanelSeleccionLinea panelSeleccion;
    private PanelTiemposParada panelTiemposParada;
    private PanelResumenLinea panelResumenLinea;
    private PanelAdmin panelAdmin;

    public Ventana() {
        setTitle("Sistema de Autobuses de Sevilla");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        panelContenedor = new JPanel(layout);

        // Leer datos GPS para el panel admin (por ahora usamos línea 27)
        ArrayList<DatoGPS> datosAdmin = LectorDatosGPS.leerDatosGPS("datos_simulados/linea27.csv");

        // Crear paneles
        panelSeleccion = new PanelSeleccionLinea(this);
        panelTiemposParada = new PanelTiemposParada();
        panelResumenLinea = new PanelResumenLinea();
        panelAdmin = new PanelAdmin(this, datosAdmin);

        // Acción del botón Volver en panelTiempos
        panelTiemposParada.getBotonVolver().addActionListener(e -> mostrarPanel("seleccionLinea"));

        // Acción del botón Volver en resumen
        panelResumenLinea.getBotonVolver().addActionListener(e -> mostrarPanel("seleccionLinea"));

        // Acción del botón Volver en panel admin ya está dentro de PanelAdmin

        // Agregar paneles al contenedor
        panelContenedor.add(panelSeleccion, "seleccionLinea");
        panelContenedor.add(panelTiemposParada, "tiempos");
        panelContenedor.add(panelResumenLinea, "resumenLinea");
        panelContenedor.add(panelAdmin, "admin");

        add(panelContenedor);
        layout.show(panelContenedor, "admin");
    }

    // Métodos para cambiar de panel
    public void mostrarPanel(String nombre) {
        layout.show(panelContenedor, nombre);
    }

    // Getters para acceder desde otros paneles
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
