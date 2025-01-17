import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.*;
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
                // Obtener usuario y contraseña ingresados
                String usuario = textField_usuario.getText();
                String contrasena = new String(passwordField1.getPassword());

                // Validar las credenciales en MongoDB
                if (validarUsuario(usuario, contrasena)) {
                    // Si las credenciales son correctas, abrir el formulario de cliente
                    abrirFormularioCliente();
                } else {
                    // Si las credenciales son incorrectas, mostrar un mensaje de error
                    JOptionPane.showMessageDialog(loginPanel, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Método para validar el usuario y la contraseña en MongoDB
    private boolean validarUsuario(String usuario, String contrasena) {
        boolean esValido = false;

        // Conexión a MongoDB (ajusta los datos según tu configuración)
        String uri = "mongodb://localhost:27017"; // Ajusta tu URI de conexión a MongoDB
        String baseDeDatos = "Proyecto"; // Nombre de tu base de datos
        String coleccionUsuarios = "usuarios"; // Nombre de la colección de usuarios

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(baseDeDatos);
            MongoCollection<Document> collection = database.getCollection(coleccionUsuarios);

            // Crear un filtro de búsqueda para encontrar el usuario y la contraseña
            Document filtro = new Document("usuario", usuario)
                    .append("password", contrasena);

            // Buscar el documento que coincida con el filtro
            Document usuarioEncontrado = collection.find(filtro).first();

            // Si el documento existe, las credenciales son correctas
            if (usuarioEncontrado != null) {
                esValido = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return esValido;
    }

    // Método para abrir el formulario de cliente
    private void abrirFormularioCliente() {
        // Asegúrate de que esta parte esté bien configurada
        JFrame frame = new JFrame("Cliente");

        // Asegurarse de que 'cliente' es una instancia válida de tu clase de formulario
        cliente clienteForm = new cliente(); // Instanciar la clase 'cliente' que contiene el panel

        frame.setContentPane(clienteForm.clientePanel); // Mostrar el panel del formulario
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 480);
        frame.setPreferredSize(new java.awt.Dimension(600, 480));
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        frame.pack();
        frame.setVisible(true);
    }
}
