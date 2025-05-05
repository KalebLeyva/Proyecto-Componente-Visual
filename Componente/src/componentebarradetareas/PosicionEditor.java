/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package componentebarradetareas;

/**
 *
 * @author MSI
 */
import java.beans.*;
import java.awt.*;
import javax.swing.*;

public class PosicionEditor extends PropertyEditorSupport {
    
    private static final String[] POSICIONES = {"SOUTH", "WEST", "EAST"};
    private static final String[] DESCRIPCIONES = {"Abajo", "Izquierda", "Derecha"};
    
    @Override
    public String[] getTags() {
        return POSICIONES;
    }
    
    @Override
    public String getAsText() {
        int index = java.util.Arrays.asList(POSICIONES).indexOf(getValue());
        return (index >= 0) ? DESCRIPCIONES[index] : "Desconocido";
    }
    
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        for (int i = 0; i < DESCRIPCIONES.length; i++) {
            if (DESCRIPCIONES[i].equals(text)) {
                setValue(POSICIONES[i]);
                return;
            }
        }
        throw new IllegalArgumentException("Posición no válida: " + text);
    }
    
    @Override
    public boolean isPaintable() {
        return false;
    }
    
    @Override
    public Component getCustomEditor() {
        JComboBox<String> combo = new JComboBox<>(DESCRIPCIONES);
        combo.setSelectedIndex(java.util.Arrays.asList(POSICIONES).indexOf(getValue()));
        
        combo.addActionListener(e -> {
            setAsText((String)combo.getSelectedItem());
            firePropertyChange();
        });
        
        return combo;
    }
    
    @Override
    public boolean supportsCustomEditor() {
        return true;
    }
}
