import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class login {
    private JTextField textField_usuario;
    private JPasswordField passwordField1;
    private JLabel bienvenida_lbl;
    private JLabel usuario_lbl;
    private JLabel password_lbl;
    public JPanel loginPanel;
    private JButton ingresar_btn;

    public login() {
        ingresar_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"))) {
                    // Obtener la base de datos
                    MongoDatabase database = mongoClient.getDatabase("tienda");

                    // Obtener la colección
                    MongoCollection<Document> collection = database.getCollection("usuarios");

                    // Crear la consulta con los valores reales ingresados por el usuario
                    Document query = new Document("user", textField_usuario.getText())
                            .append("password", new String(passwordField1.getPassword())); // Se usa 'getPassword' para JPasswordField

                    // Buscar el usuario
                    Document user = collection.find(query).first();

                    System.out.println("Conexión exitosa a la base de datos.");

                    if (user != null) {
                        String username = user.getString("user"); // Obtener el nombre de usuario

                        if ("eddy_admin".equals(username)) {
                            // Obtener la ventana actual (Login) y cerrarla
                            JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);
                            if (loginFrame != null) {
                                loginFrame.dispose();
                            }
                            // Abrir la ventana del administrador
                            JFrame ventanaAdmin = new JFrame("Ventana Administrador");
                            ventanaAdmin.setContentPane(new administrador().administracionPanel); // Asegúrate de tener esta clase
                            ventanaAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ventanaAdmin.setSize(1024, 768);
                            ventanaAdmin.setPreferredSize(new Dimension(1024, 768));
                            ventanaAdmin.pack();
                            ventanaAdmin.setVisible(true);

                        } else {
                            // Obtener la ventana actual (Login) y cerrarla
                            JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(loginPanel);
                            if (loginFrame != null) {
                                loginFrame.dispose();
                            }
                            // Abrir la ventana del cliente
                            JFrame ventanaCliente = new JFrame("Ventana Cliente");
                            ventanaCliente.setContentPane(new cliente().clientePanel); // Asegúrate de tener esta clase
                            ventanaCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            ventanaCliente.setSize(1024, 768);
                            ventanaCliente.setPreferredSize(new Dimension(1024, 768));
                            ventanaCliente.pack();
                            ventanaCliente.setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Acceso denegado. Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
