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

public class CodigoBarraBeanInfo extends SimpleBeanInfo {
@Override
public PropertyDescriptor[] getPropertyDescriptors() {
    try {
        PropertyDescriptor urlWhatsApp = new PropertyDescriptor("urlWhatsApp", CodigoBarra.class);
        PropertyDescriptor urlCalculadora = new PropertyDescriptor("urlCalculadora", CodigoBarra.class);
        PropertyDescriptor btnNuevo = new PropertyDescriptor("btnNuevo", CodigoBarra.class);
        PropertyDescriptor color = new PropertyDescriptor("color", CodigoBarra.class);
        color.setPropertyEditorClass(java.beans.PropertyEditorManager.findEditor(java.awt.Color.class).getClass()); // Usa el editor de color por defecto

        PropertyDescriptor orientacion = new PropertyDescriptor("orientacion", CodigoBarra.class);
        // Puedes usar un editor personalizado si quieres restringir a valores como "Horizontal", "Vertical Izquierda", etc.

        PropertyDescriptor posicion = createPosicionProperty(); // Ya tienes esto definido

        return new PropertyDescriptor[] {urlWhatsApp,urlCalculadora,color,orientacion,posicion};
    } catch (IntrospectionException ex) {
        ex.printStackTrace();
        return null;
    }
}

    
    private PropertyDescriptor createPosicionProperty() throws IntrospectionException {
        PropertyDescriptor descriptor = new PropertyDescriptor("posicion", CodigoBarra.class, "getPosicion", "setPosicion");
        
        // Creamos un editor de propiedades personalizado para la posición
        descriptor.setPropertyEditorClass(PosicionEditor.class);
        
        return descriptor;
    }
    
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor descriptor = new BeanDescriptor(CodigoBarra.class);
        descriptor.setDisplayName("Barra de Tareas Personalizada");
        descriptor.setShortDescription("Una barra de tareas configurable con iconos de sistema");
        return descriptor;
    }
}
