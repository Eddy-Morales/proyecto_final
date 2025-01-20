import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class cliente {
    private JTable table1;
    public JPanel clientePanel;
    private JButton verMenu;

    public cliente() {
        // Configurar la tabla con un modelo vacío al inicio
        table1.setModel(new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Detalles", "Precio"}, 0
        ));

        verMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos(); // Llamar al método para cargar los datos
            }
        });
    }

    private void cargarDatos() {
        metodosCRUD tabla1 = new metodosCRUD(); // Instanciar la clase MenuDAO
        DefaultTableModel modelo = tabla1.obtenerMenu(); // Obtener los datos
        table1.setModel(modelo); // Asignar el modelo con los datos al JTable


    }
}

