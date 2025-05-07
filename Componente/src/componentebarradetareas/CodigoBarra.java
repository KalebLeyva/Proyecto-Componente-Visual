/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package componentebarradetareas;

import java.awt.*;
import java.awt.event.*;
import java.beans.JavaBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Desktop;

/**
 * Barra de tareas personalizada con múltiples funcionalidades.
 * 
 * <p>Este componente proporciona una barra de tareas similar a la de sistemas operativos modernos,
 * con iconos de sistema, acceso rápido a aplicaciones y capacidad de ocultación automática.</p>
 * 
 * <p><strong>Características principales:</strong></p>
 * <ul>
 *   <li>Visualización de estado de WiFi</li>
 *   <li>Monitor de batería</li>
 *   <li>Reloj en tiempo real</li>
 *   <li>Cronómetro integrado</li>
 *   <li>Accesos directos configurables</li>
 *   <li>Posicionamiento en diferentes bordes de la pantalla</li>
 *   <li>Modo auto-ocultable</li>
 * </ul>
 * 
 * @author Eduardo Yael Mendoza Martinez/Kaleb Daniel Leyva Solis
 * @version 1.0
 */

@JavaBean(defaultProperty = "barraAnclada", description = "Barra de tareas personalizada")
public class CodigoBarra extends JPanel implements Serializable {
    private JLabel IconoWifi;
    private JLabel IconoBateria;
    private JLabel IconoHora;
    private JButton BotonClima, BotonWhatsapp, BotonArchivos;
    private boolean MostrarIconoWifi = true;
    private boolean MostrarIconoBateria = true;
    private boolean MostrarIconoHora = true;
    private boolean barraAnclada = true;
    private String posicion = "SOUTH";
    private Container contenedorPadre;
    private JPopupMenu menuContextual;
    private boolean cronometroActivo = false;
private long tiempoInicio = 0;
private Timer timerCronometro;
private JLabel IconoCronometro;

/**
     * Constructor que inicializa la barra de tareas con sus componentes básicos.
     */

public CodigoBarra() {
    setBackground(new Color(0, 128, 128));
    setMinimumSize(new Dimension(60, 40));
    setPreferredSize(new Dimension(400, 40));
    setLayout(new BorderLayout());
    
    initComponents();
    configurarMenuContextual();
    setVisible(true);
}

/**
     * Inicializa todos los componentes visuales de la barra.
     */
    private void initComponents() {
    // Iconos
    IconoWifi = new JLabel(escalarIcono("/Imagenes/wifi.png", 16, 16));
    IconoBateria = new JLabel(escalarIcono("/Imagenes/battery_100.png", 16, 16));
    IconoHora = new JLabel();
    IconoHora.setForeground(Color.WHITE);
    
    // Icono del cronómetro
    IconoCronometro = new JLabel("00:00:00");
    IconoCronometro.setForeground(Color.WHITE);
    IconoCronometro.setVisible(false);
    
    // Botones con imágenes
    BotonClima = crearBotonConImagen("/Imagenes/cronometro.png", "Cronómetro");
    BotonWhatsapp = crearBotonConImagen("/Imagenes/whatsapp.png", "WhatsApp Web");
    BotonArchivos = crearBotonConImagen("/Imagenes/calculadora.png", "Calculadora");
    
    // Configurar acciones
    BotonClima.addActionListener(e -> controlarCronometro());
    BotonWhatsapp.addActionListener(e -> abrirWhatsAppWeb());
    BotonArchivos.addActionListener(e -> abrirCalculadora());
    
    actualizarReloj();
    actualizarBateria();
    actualizarWifi();

    // Configuración inicial
    actualizarComponentes();
    agregarEventosMouse();
    }

    /**
     * Configura el menú contextual que aparece al hacer clic derecho en la barra.
     */
    private void configurarMenuContextual() {
        menuContextual = new JPopupMenu();
        
        // Items del menú
        JCheckBoxMenuItem wifiItem = new JCheckBoxMenuItem("Mostrar WiFi", MostrarIconoWifi);
        JCheckBoxMenuItem bateriaItem = new JCheckBoxMenuItem("Mostrar Batería", MostrarIconoBateria);
        JCheckBoxMenuItem horaItem = new JCheckBoxMenuItem("Mostrar Hora", MostrarIconoHora);
        JCheckBoxMenuItem anclarItem = new JCheckBoxMenuItem("Anclar barra", barraAnclada);
        
        // Menú de posición
        JMenu posicionMenu = new JMenu("Posición");
        ButtonGroup grupoPosiciones = new ButtonGroup();
        
        JRadioButtonMenuItem abajoItem = new JRadioButtonMenuItem("Abajo", posicion.equals("SOUTH"));
        JRadioButtonMenuItem izquierdaItem = new JRadioButtonMenuItem("Izquierda", posicion.equals("WEST"));
        JRadioButtonMenuItem derechaItem = new JRadioButtonMenuItem("Derecha", posicion.equals("EAST"));
        
        grupoPosiciones.add(abajoItem);
        grupoPosiciones.add(izquierdaItem);
        grupoPosiciones.add(derechaItem);
        
        posicionMenu.add(abajoItem);
        posicionMenu.add(izquierdaItem);
        posicionMenu.add(derechaItem);

        // Añadir items al menú
        menuContextual.add(wifiItem);
        menuContextual.add(bateriaItem);
        menuContextual.add(horaItem);
        menuContextual.addSeparator();
        menuContextual.add(anclarItem);
        menuContextual.addSeparator();
        menuContextual.add(posicionMenu);

        // Listeners
        wifiItem.addActionListener(e -> {
            MostrarIconoWifi = wifiItem.isSelected();
            actualizarVisibilidad();
        });
        
        bateriaItem.addActionListener(e -> {
            MostrarIconoBateria = bateriaItem.isSelected();
            actualizarVisibilidad();
        });
        
        horaItem.addActionListener(e -> {
            MostrarIconoHora = horaItem.isSelected();
            actualizarVisibilidad();
        });
        
        anclarItem.addActionListener(e -> barraAnclada = anclarItem.isSelected());
        
        abajoItem.addActionListener(e -> {
            posicion = "SOUTH";
            reconfigurarBarra();
        });
        
        izquierdaItem.addActionListener(e -> {
            posicion = "WEST";
            reconfigurarBarra();
        });
        
        derechaItem.addActionListener(e -> {
            posicion = "EAST";
            reconfigurarBarra();
        });

        setComponentPopupMenu(menuContextual);
    }

     /**
     * Actualiza la disposición de los componentes según la posición de la barra.
     */   
private void actualizarComponentes() {
removeAll();
    
    if (posicion.equals("SOUTH")) {
        // Configuración HORIZONTAL (abajo)
        setLayout(new BorderLayout());
        
        JPanel panelIconos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIconos.setOpaque(false);
        if (MostrarIconoWifi) panelIconos.add(IconoWifi);
        if (MostrarIconoBateria) panelIconos.add(IconoBateria);
        panelIconos.add(IconoCronometro);
        if (MostrarIconoHora) panelIconos.add(IconoHora);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(BotonClima);
        panelBotones.add(BotonWhatsapp);
        panelBotones.add(BotonArchivos);
        
        add(panelIconos, BorderLayout.EAST);
        add(panelBotones, BorderLayout.CENTER);
    } else {
        // Configuración VERTICAL (izquierda/derecha)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Añadir espacio flexible arriba
        add(Box.createVerticalGlue());
        
        // Panel para los iconos
        JPanel panelIconos = new JPanel();
        panelIconos.setLayout(new BoxLayout(panelIconos, BoxLayout.Y_AXIS));
        panelIconos.setOpaque(false);
        
        if (MostrarIconoWifi) addComponenteCentrado(panelIconos, IconoWifi);
        if (MostrarIconoBateria) addComponenteCentrado(panelIconos, IconoBateria);
        addComponenteCentrado(panelIconos, IconoCronometro);
        if (MostrarIconoHora) addComponenteCentrado(panelIconos, IconoHora);
        
        add(panelIconos);
        
        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        
        addComponenteCentrado(panelBotones, BotonClima);
        addComponenteCentrado(panelBotones, BotonWhatsapp);
        addComponenteCentrado(panelBotones, BotonArchivos);
        
        add(panelBotones);
        
        // Añadir espacio flexible abajo
        add(Box.createVerticalGlue());
    }
    
    // Añadir área de activación para cuando la barra no está anclada
    if (!barraAnclada) {
        agregarAreaActivacion();
    }
    
    revalidate();
    repaint();
}
/**
     * Centra un componente dentro de un panel wrapper.
     * @param panel Panel contenedor
     * @param comp Componente a centrar
     */
private void addComponenteCentrado(JPanel panel, Component comp) {
    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
    wrapper.setOpaque(false);
    wrapper.add(comp);
    panel.add(wrapper);
}
/**
     * Agrega el área de activación para el modo auto-ocultable.
     */
private void agregarAreaActivacion() {
    // Primero eliminar cualquier área de activación existente
    Component[] components = getComponents();
    for (Component component : components) {
        if (component instanceof JPanel && ((JPanel)component).getMouseListeners().length > 0) {
            remove(component);
        }
    }
    
    if (!barraAnclada) {
        JPanel areaActivacion = new JPanel();
        areaActivacion.setOpaque(false);
        
        if (posicion.equals("SOUTH")) {
            areaActivacion.setPreferredSize(new Dimension(getWidth(), 10));
            add(areaActivacion, BorderLayout.NORTH);
        } else if (posicion.equals("EAST")) {
            areaActivacion.setPreferredSize(new Dimension(10, getHeight()));
            add(areaActivacion, BorderLayout.WEST);
        } else if (posicion.equals("WEST")) {
            areaActivacion.setPreferredSize(new Dimension(10, getHeight()));
            add(areaActivacion, BorderLayout.EAST);
        }
        
        areaActivacion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!barraAnclada) {
                    setVisible(true);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!barraAnclada) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(() -> {
                                if (!barraAnclada && !isMouseOverComponent()) {
                                    setVisible(false);
                                }
                            });
                        }
                    }, 500);
                }
            }
        });
    }
}

private boolean isMouseOverAreaActivacion(Point point) {
// Convertir las coordenadas del mouse a coordenadas relativas al panel
    Point relativePoint = SwingUtilities.convertPoint(this, point, this);
    
    // Verificar si el mouse está sobre el área de activación
    Component[] components = getComponents();
    for (Component component : components) {
        if (component instanceof JPanel && ((JPanel)component).getMouseListeners().length > 0) {
            // Aquí falta un paréntesis de cierre
            if (component.contains(SwingUtilities.convertPoint(this, relativePoint, component))) {
                return true;
            }
        }
    }
    return false;
}
/**
     * Reconfigura la posición y disposición de la barra en su contenedor padre.
     */
private void reconfigurarBarra() {
if (contenedorPadre != null) {
        contenedorPadre.remove(this);
        
        if (!(contenedorPadre.getLayout() instanceof BorderLayout)) {
            contenedorPadre.setLayout(new BorderLayout());
        }
        
        String constraint = switch (posicion) {
            case "WEST" -> BorderLayout.WEST;
            case "EAST" -> BorderLayout.EAST;
            default -> BorderLayout.SOUTH;
        };
        
        // Establecer tamaño preferido basado en la orientación
        if (posicion.equals("SOUTH")) {
            setPreferredSize(new Dimension(contenedorPadre.getWidth(), 40));
        } else {
            // Para posición vertical (izquierda/derecha)
            setPreferredSize(new Dimension(60, contenedorPadre.getHeight()));
        }
        
        contenedorPadre.add(this, constraint);
        contenedorPadre.revalidate();
        contenedorPadre.repaint();
    }
    actualizarComponentes();
}

public void setPosicion(String pos) {
    this.posicion = pos;
    // Recrear los botones manteniendo sus acciones
    JButton tempClima = BotonClima;
    JButton tempWhatsapp = BotonWhatsapp;
    JButton tempArchivos = BotonArchivos;
    
    BotonClima = crearBotonConImagen("/Imagenes/cronometro.png", "Cronómetro");
    BotonWhatsapp = crearBotonConImagen("/Imagenes/whatsapp.png", "WhatsApp Web");
    BotonArchivos = crearBotonConImagen("/Imagenes/calculadora.png", "Calculadora");
    
    // Restaurar los listeners
    BotonClima.addActionListener(e -> controlarCronometro());
    BotonWhatsapp.addActionListener(e -> abrirWhatsAppWeb());
    BotonArchivos.addActionListener(e -> abrirCalculadora());
    
    // Restaurar estado del cronómetro si estaba activo
    if (cronometroActivo) {
        BotonClima.setIcon(escalarIcono("/Imagenes/cronometro_activo.png", 24, 24));
    }
    
    reconfigurarBarra();
}

public void setBarraAnclada(boolean val) {
    this.barraAnclada = val;
    if (val) {
        setVisible(true);
    } else {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (!barraAnclada && !isMouseOverComponent()) {
                        setVisible(false);
                    }
                });
            }
        }, 1000);
    }
    actualizarComponentes(); // Esto asegura que el área de activación se actualice
}

private boolean isMouseOverComponent() {
    Point mousePos = MouseInfo.getPointerInfo().getLocation();
    SwingUtilities.convertPointFromScreen(mousePos, this);
    return contains(mousePos);
}

private boolean isMouseOver() {
    Point mousePos = MouseInfo.getPointerInfo().getLocation();
    SwingUtilities.convertPointFromScreen(mousePos, this);
    return contains(mousePos);
}
/**
     * Crea un botón con una imagen escalada.
     * @param rutaImagen Ruta de la imagen dentro del proyecto
     * @param textoAlternativo Texto para el tooltip
     * @return JButton configurado
     */
private JButton crearBotonConImagen(String rutaImagen, String textoAlternativo) {
    JButton btn = new JButton();
    btn.setIcon(escalarIcono(rutaImagen, 24, 24)); // Tamaño de la imagen
    btn.setToolTipText(textoAlternativo); // Texto que aparece al pasar el mouse
    btn.setMargin(new Insets(2, 2, 2, 2));
    btn.setFocusable(false);
    btn.setPreferredSize(new Dimension(40, 40)); // Tamaño del botón
    btn.setContentAreaFilled(false); // Fondo transparente
    btn.setBorderPainted(false); // Sin borde
    return btn;
}
/**
     * Escala una imagen a las dimensiones especificadas.
     * @param ruta Ruta del recurso de imagen
     * @param ancho Ancho deseado
     * @param alto Alto deseado
     * @return ImageIcon escalado
     */
    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(ruta));
            Image img = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + ruta);
            return new ImageIcon();
        }
    }
 /**
     * Inicia la actualización periódica del reloj.
     */
    private void actualizarReloj() {
        Timer timer = new Timer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    IconoHora.setText(LocalTime.now().format(formatter));
                });
            }
        }, 0, 1000);
    }
    /**
     * Obtiene el nivel actual de batería del sistema.
     * @return Porcentaje de batería (0-100) o -1 si no se pudo obtener
     */
private int obtenerNivelBateriaReal() {
    String os = System.getProperty("os.name").toLowerCase();
    
    try {
        if (os.contains("win")) {
            return obtenerNivelBateriaWindows();
        }
    } catch (Exception e) {
        System.err.println("Error al obtener nivel de batería: " + e.getMessage());
    }
    
    return -1;
}

/**
     * Implementación específica para Windows del monitor de batería.
     * @return Porcentaje de carga restante
     * @throws Exception Si falla la ejecución del comando WMIC
     */
private int obtenerNivelBateriaWindows() throws Exception {
    Process process = Runtime.getRuntime().exec("WMIC PATH Win32_Battery Get EstimatedChargeRemaining");
    process.waitFor();
    
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().matches("\\d+")) {
                return Integer.parseInt(line.trim());
            }
        }
    }
    return -1;
}
/**
     * Verifica si la batería está cargando.
     * @return true si está cargando, false en caso contrario
     */
private boolean estaCargandoBateria() {
    try {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Process process = Runtime.getRuntime().exec("WMIC PATH Win32_Battery Get BatteryStatus");
            process.waitFor();
            
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().matches("\\d+")) {
                        int status = Integer.parseInt(line.trim());
                        // 2 = Cargando, 1 = Descargando en Windows
                        return status == 2; 
                    }
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Error al verificar estado de carga: " + e.getMessage());
    }
    return false;
}
 /**
     * Actualiza periódicamente la información de la batería.
     */
    private void actualizarBateria() {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                try {
                    int nivel = obtenerNivelBateriaReal();
                    boolean cargando = estaCargandoBateria();
                    
                    System.out.println("Nivel batería: " + nivel + "% - Cargando: " + cargando);
                    
                    if (nivel < 0) {
                        System.err.println("No se pudo obtener nivel de batería");
                        IconoBateria.setVisible(false);
                        return;
                    }
                    
                    String iconPath = determinarIconoBateria(nivel, cargando);
                    System.out.println("Intentando cargar icono: " + iconPath);
                    
                    ImageIcon icono = escalarIcono(iconPath, 16, 16);
                    if (icono.getImageLoadStatus() != MediaTracker.COMPLETE) {
                        System.err.println("Error al cargar el icono: " + iconPath);
                    }
                    
                    IconoBateria.setIcon(icono);
                    IconoBateria.setToolTipText("Batería: " + nivel + "%" + (cargando ? " (Cargando)" : ""));
                    IconoBateria.setVisible(true);
                    
                } catch (Exception e) {
                    System.err.println("Error en actualizarBateria: " + e.getMessage());
                    IconoBateria.setIcon(escalarIcono("/Imagenes/battery_error.png", 16, 16));
                }
            });
        }
    }, 0, 60000);
}
/**
     * Determina qué icono de batería mostrar según el nivel y estado de carga.
     * @param nivel Porcentaje de carga (0-100)
     * @param cargando true si está cargando
     * @return Ruta del icono correspondiente
     */
private String determinarIconoBateria(int nivel, boolean cargando) {
      if (cargando) {
        return "/Imagenes/battery_charging.png";
    }
    
    // Redondea al múltiplo de 25 más cercano para tus imágenes
    int nivelRedondeado = ((nivel + 12) / 25) * 25;
    nivelRedondeado = Math.min(100, Math.max(0, nivelRedondeado));
    
    return "/Imagenes/battery_" + nivelRedondeado + ".png";
}
/**
     * Actualiza periódicamente la información de la conexión WiFi.
     */
    private void actualizarWifi() {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> {
                try {
                    int intensidad = obtenerIntensidadWifiReal();
                    boolean conectado = intensidad > 0;
                    
                    String icono = determinarIconoWifi(conectado, intensidad);
                    IconoWifi.setIcon(escalarIcono(icono, 16, 16));
                    IconoWifi.setToolTipText("WiFi: " + (conectado ? intensidad + "% señal" : "Desconectado"));
                    
                } catch (Exception e) {
                    System.err.println("Error actualizando WiFi: " + e.getMessage());
                    IconoWifi.setIcon(escalarIcono("/Imagenes/wifi_error.png", 16, 16));
                }
            });
        }
    }, 0, 30000); // Actualiza cada 30 segundos
}
/**
     * Determina qué icono de WiFi mostrar según la intensidad de señal.
     * @param conectado true si hay conexión WiFi
     * @param intensidad Porcentaje de intensidad de señal
     * @return Ruta del icono correspondiente
     */
private String determinarIconoWifi(boolean conectado, int intensidad) {
    if (!conectado) {
        return "/Imagenes/wifi_off.png";
    }
    
    if (intensidad > 75) return "/Imagenes/wifi_high.png";
    if (intensidad > 50) return "/Imagenes/wifi_medium.png";
    if (intensidad > 25) return "/Imagenes/wifi_low.png";
    return "/Imagenes/wifi_weak.png";
}
/**
     * Obtiene la intensidad de la señal WiFi en Windows.
     * @return Porcentaje de intensidad o 0 si no hay conexión
     */
private int obtenerIntensidadWifiReal() {
    try {
        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            return -1; // Solo Windows soportado
        }

        Process process = Runtime.getRuntime().exec(
            new String[] {"cmd.exe", "/c", "netsh wlan show interfaces"});
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Señal")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String percent = parts[1].trim().replace("%", "");
                        return Integer.parseInt(percent);
                    }
                }
            }
        }
        
        // Alternativa para versiones en inglés de Windows
        process = Runtime.getRuntime().exec(
            new String[] {"cmd.exe", "/c", "netsh wlan show interfaces"});
        
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("Signal")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String percent = parts[1].trim().replace("%", "");
                        return Integer.parseInt(percent);
                    }
                }
            }
        }
        
    } catch (Exception e) {
        System.err.println("Error al obtener intensidad WiFi: " + e.getMessage());
    }
    return 0; // Desconectado o error
}
/**
     * Actualiza la visibilidad de los componentes según las preferencias.
     */
    private void actualizarVisibilidad() {
        IconoWifi.setVisible(MostrarIconoWifi);
        IconoBateria.setVisible(MostrarIconoBateria);
        IconoHora.setVisible(MostrarIconoHora);
    }


    private Rectangle getLocationOnScreenBounds() {
        try {
            Point p = getLocationOnScreen();
            return new Rectangle(p.x, p.y, getWidth(), getHeight());
        } catch (IllegalComponentStateException e) {
            return null;
        }
    }



@Override
public Dimension getPreferredSize() {
    if (posicion.equals("SOUTH")) {
        return new Dimension(
            (getParent() != null) ? getParent().getWidth() : 400, 
            40
        );
    } else {
        return new Dimension(
            60, 
            (getParent() != null) ? getParent().getHeight() : 600
        );
    }
}
/**
     * Controla el inicio/detención del cronómetro.
     */
private void controlarCronometro() {
    if (!cronometroActivo) {
        iniciarCronometro();
        BotonClima.setText("⏹"); // Cambiar a icono de stop
    } else {
        detenerCronometro();
        BotonClima.setText("⏱"); // Volver a icono de reloj
    }
}
/**
     * Inicia el cronómetro.
     */
private void iniciarCronometro() {
    cronometroActivo = true;
    tiempoInicio = System.currentTimeMillis();
    IconoCronometro.setVisible(true);
    
    timerCronometro = new Timer();
    timerCronometro.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            SwingUtilities.invokeLater(() -> actualizarTiempoCronometro());
        }
    }, 0, 1000);
}
/**
     * Detiene el cronómetro.
     */
private void detenerCronometro() {
    cronometroActivo = false;
    if (timerCronometro != null) {
        timerCronometro.cancel();
    }
}
/**
     * Actualiza el display del cronómetro con el tiempo transcurrido.
     */
private void actualizarTiempoCronometro() {
    if (!cronometroActivo) return;
    
    long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
    
    long segundos = (tiempoTranscurrido / 1000) % 60;
    long minutos = (tiempoTranscurrido / (1000 * 60)) % 60;
    long horas = (tiempoTranscurrido / (1000 * 60 * 60));
    
    String tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos);
    IconoCronometro.setText(tiempoFormateado);
}

public void resetearCronometro() {
    detenerCronometro();
    IconoCronometro.setText("00:00:00");
    IconoCronometro.setVisible(false);
    BotonClima.setText("⏱");
}
/**
     * Abre una URL en el navegador predeterminado del sistema.
     * @param url URL a abrir
     */
private void abrirEnNavegador(String url) {
    try {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se puede abrir el navegador automáticamente en este sistema", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error al abrir el enlace: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
/**
     * Abre la calculadora web configurada.
     */
private void abrirCalculadora() {
    abrirEnNavegador(urlCalculadora);
}
/**
     * Abre WhatsApp Web en el navegador.
     */
private void abrirWhatsAppWeb() {
    abrirEnNavegador(urlWhatsApp);
}

private String urlWhatsApp = "https://web.whatsapp.com/";
private String urlCalculadora = "https://www.calculadora-online.xyz/";
// Métodos getter y setter con documentación

    /**
     * Obtiene la URL configurada para WhatsApp Web.
     * @return URL actual de WhatsApp Web
     */
public String getUrlWhatsApp() {
    return urlWhatsApp;
}

private void agregarEventosMouse() {
    addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!barraAnclada) {
                setVisible(true);
                revalidate();
            repaint(); 
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            if (!barraAnclada && !isMouseOverAreaActivacion(e.getPoint())) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (!barraAnclada && !isMouseOverComponent() && !isMouseOverAreaActivacion(MouseInfo.getPointerInfo().getLocation())) {
                                setVisible(false);
                            }
                        });
                    }
                }, 500);
            }
        }
    });
    
    // Añadir área de activación
    agregarAreaActivacion();
}
/**
     * Establece la URL para WhatsApp Web.
     * @param url Nueva URL para WhatsApp Web
     */
public void setUrlWhatsApp(String url) {
    this.urlWhatsApp = url;
}
/**
     * Obtiene la URL configurada para la calculadora web.
     * @return URL actual de la calculadora
     */
public String getUrlCalculadora() {
    return urlCalculadora;
}
 /**
     * Establece la URL para la calculadora web.
     * @param url Nueva URL para la calculadora
     */
public void setUrlCalculadora(String url) {
    this.urlCalculadora = url;
}
 /**
     * Verifica si el icono de WiFi está visible.
     * @return true si el icono de WiFi está visible
     */
    public boolean isMostrarWifi() {
        return MostrarIconoWifi;
    }
/**
     * Establece la visibilidad del icono de WiFi.
     * @param val true para mostrar, false para ocultar
     */
    public void setMostrarWifi(boolean val) {
        this.MostrarIconoWifi = val;
        actualizarVisibilidad();
    }
/**
     * Verifica si el icono de batería está visible.
     * @return true si el icono de batería está visible
     */
    public boolean isMostrarBateria() {
        return MostrarIconoBateria;
    }
/**
     * Establece la visibilidad del icono de batería.
     * @param val true para mostrar, false para ocultar
     */
    public void setMostrarBateria(boolean val) {
        this.MostrarIconoBateria = val;
        actualizarVisibilidad();
    }
/**
     * Verifica si el reloj está visible.
     * @return true si el reloj está visible
     */
    public boolean isMostrarReloj() {
        return MostrarIconoHora;
    }
/**
     * Establece la visibilidad del reloj.
     * @param val true para mostrar, false para ocultar
     */
    public void setMostrarReloj(boolean val) {
        this.MostrarIconoHora = val;
        actualizarVisibilidad();
    }
/**
     * Verifica si la barra está anclada (no auto-ocultable).
     * @return true si la barra está anclada
     */
    public boolean isBarraAnclada() {
        return barraAnclada;
    }

    //public void setBarraAnclada(boolean val) {
    //   this.barraAnclada = val;
    //}

    public String getPosicion() {
        return posicion;
    }

    //public void setPosicion(String pos) {
       // this.posicion = pos;
        //reconfigurarBarra();
    //}
/**
     * Obtiene el contenedor padre de la barra.
     * @return Contenedor padre
     */
    public Container getContenedorPadre() {
        return contenedorPadre;
    }
/**
     * Establece el contenedor padre de la barra.
     * @param contenedor Contenedor padre
     */
    public void setContenedorPadre(Container contenedor) {
        this.contenedorPadre = contenedor;
    }
/**
     * Obtiene el componente del icono de WiFi.
     * @return JLabel del icono de WiFi
     */
    public JLabel getIconoWifi() {
        return IconoWifi;
    }
/**
     * Obtiene el componente del icono de batería.
     * @return JLabel del icono de batería
     */
    public JLabel getIconoBateria() {
        return IconoBateria;
    }
/**
     * Obtiene el componente del reloj.
     * @return JLabel del reloj
     */
    public JLabel getIconoHora() {
        return IconoHora;
    }
/**
     * Obtiene el botón del cronómetro.
     * @return JButton del cronómetro
     */
    public JButton getBotonClima() {
        return BotonClima;
    }
/**
     * Obtiene el botón de WhatsApp.
     * @return JButton de WhatsApp
     */
    public JButton getBotonWhatsapp() {
        return BotonWhatsapp;
    }
/**
     * Obtiene el botón de la calculadora.
     * @return JButton de la calculadora
     */
    public JButton getBotonArchivos() {
        return BotonArchivos;
    }
}