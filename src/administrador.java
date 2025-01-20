import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private metodosCRUD metodosCRUD;
    public administrador() {
        metodosCRUD= new metodosCRUD();
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
    }
}
