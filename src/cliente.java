import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class cliente {
    private JTable table1;
    public JPanel clientePanel;
    private JButton verMenu;
    private JTextField textField_ingresar;
    private JButton ingresar_btn;
    private JLabel ingresar_lbl;
    private JButton regresar_btn;

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
        ingresar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCombo = textField_ingresar.getText(); // Obtener el nombre ingresado

                try {
                    metodosPedidos pedidosDAO = new metodosPedidos(); // Instanciar metodosPedidos

                    // Llamar al método para registrar el pedido por nombre
                    pedidosDAO.registrarPedidoPorNombre(nombreCombo);

                    JOptionPane.showMessageDialog(clientePanel, "Pedido registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    textField_ingresar.setText(""); // Limpiar campo después de registrar

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(clientePanel, "Error al registrar el pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        regresar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(clientePanel);
                frame.dispose(); // Cierra la ventana actual

                // Abrir la ventana de Login
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setContentPane(new login().loginPanel);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(600, 480);
                loginFrame.setPreferredSize(new Dimension(600, 480));
                loginFrame.setLocationRelativeTo(null); // Centra la ventana
                loginFrame.pack();
                loginFrame.setVisible(true);
            }
        });
    }

    private void cargarDatos() {
        metodosCRUD tabla1 = new metodosCRUD();
        DefaultTableModel modelo = tabla1.obtenerMenu();
        table1.setModel(modelo);


    }
}

