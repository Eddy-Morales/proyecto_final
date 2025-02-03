import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class administrador {
    private JTextField textField_id;
    private JTextField textField_combo;
    private JTextField textField_det;
    private JTextField textField_precio;
    private JButton insertar_btn;
    private JButton actualizar_btn;
    private JButton eliminar_btn;
    private JLabel id_lbl;
    private JLabel combo_lbl;
    private JLabel detalles_lbl;
    private JLabel precio_lbl;
    private JButton menu_btn;
    private JLabel titulo_lbl;
    public JPanel administracionPanel;
    private JButton generar_pedidos_btn;
    private JTable tabla_admin;
    private JRadioButton insertar_Rbtn;
    private JRadioButton Actualizar_Rbtn;
    private JRadioButton eliminar_Rbtn;
    private JButton regresar_btn;

    private metodosCRUD metodosCRUD;
    public administrador() {
        metodosCRUD= new metodosCRUD();

        ButtonGroup grupoBotones = new ButtonGroup();
        grupoBotones.add(insertar_Rbtn);
        grupoBotones.add(Actualizar_Rbtn);
        grupoBotones.add(eliminar_Rbtn);
        // Inicializar el estado de los campos deshabilitados
        deshabilitarCampos();



        insertar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombreCombo = textField_combo.getText();
                String detalles=textField_det.getText();
                double precio=Double.parseDouble(textField_precio.getText());

                metodosCRUD.insertarPlatillo(nombreCombo, detalles, precio);
                textField_combo.setText("");
                textField_det.setText("");
            }
        });
        actualizar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField_id.getText();
                String nuevoCombo = textField_combo.getText();
                String nuevoDetalles = textField_det.getText();
                double nuevoPrecio;

                try {
                    nuevoPrecio = Double.parseDouble(textField_precio.getText());

                    // Verificar si el platillo existe antes de intentar actualizarlo
                    if (metodosCRUD.existePlatillo(id)) {
                        metodosCRUD.actualizarPlatillo(id, nuevoCombo, nuevoDetalles, nuevoPrecio);
                        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "El platillo con ID " + id + " no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el platillo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                textField_id.setText("");
                textField_combo.setText("");
                textField_det.setText("");
                textField_precio.setText("");
            }
        });
        eliminar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = textField_id.getText(); // Obtener ID del platillo a eliminar

                try {
                    // Verificar si el platillo existe
                    if (metodosCRUD.existePlatillo(id)) {
                        // Si existe, proceder a eliminarlo
                        metodosCRUD.eliminarPlatillo(id);
                        JOptionPane.showMessageDialog(null, "Menú eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Si no existe, mostrar un mensaje de error
                        JOptionPane.showMessageDialog(null, "El platillo con ID " + id + " no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Manejo de excepciones en caso de error durante la eliminación
                    JOptionPane.showMessageDialog(null, "Error al eliminar el platillo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Limpiar el campo de ID después de intentar eliminar
                textField_id.setText("");
            }
        });

        generar_pedidos_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pdf pdf = new Pdf();

                // Especificar la ruta completa con el nombre del archivo JSON
                String rutaArchivo = "C:\\Users\\User\\Desktop\\POO_pdfs\\pruebas.pdf";

                try {
                    // Verificar si el directorio existe, si no, crearlo
                    File directorio = new File("C:\\Users\\User\\Desktop\\POO_pdfs");
                    if (!directorio.exists()) {
                        directorio.mkdirs(); // Crear el directorio
                    }

                    // Llamar al método para generar el JSON
                    pdf.generarPdf(rutaArchivo); // Este es el método correcto para generar JSON
                    JOptionPane.showMessageDialog(administracionPanel, "PDF generado correctamente en: " + rutaArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(administracionPanel, "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        menu_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de la clase metodosCRUD
                metodosCRUD crud = new metodosCRUD();

                // Obtener el modelo con los datos desde MongoDB
                DefaultTableModel modelo = crud.obtenerMenu();

                // Asignar el modelo a la tabla para actualizar los datos
                tabla_admin.setModel(modelo);

            }
        });
        insertar_Rbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habilitarCampos(true, false, false);
            }
        });
        Actualizar_Rbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habilitarCampos(false, true, false);
            }
        });
        eliminar_Rbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habilitarCampos(false, false, true);
            }
        });

        regresar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(administracionPanel);
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

    private void deshabilitarCampos() {
        textField_id.setEditable(false);
        textField_combo.setEditable(false);
        textField_det.setEditable(false);
        textField_precio.setEditable(false);

        insertar_btn.setEnabled(false);
        actualizar_btn.setEnabled(false);
        eliminar_btn.setEnabled(false);
    }
    private void habilitarCampos(boolean insertar, boolean actualizar, boolean eliminar) {
        // Deshabilita todo primero
        deshabilitarCampos();

        if (insertar) {
            textField_combo.setEditable(true);
            textField_det.setEditable(true);
            textField_precio.setEditable(true);
            insertar_btn.setEnabled(true);
        } else if (actualizar) {
            textField_id.setEditable(true);
            textField_combo.setEditable(true);
            textField_det.setEditable(true);
            textField_precio.setEditable(true);
            actualizar_btn.setEnabled(true);
        } else if (eliminar) {
            textField_id.setEditable(true);
            eliminar_btn.setEnabled(true);
        }
    }
}


