import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;
import static com.mongodb.client.model.Filters.eq;

public class metodosPedidos {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // URI de MongoDB
    private static final String DATABASE_NAME = "tienda"; // Nombre de la base de datos
    private static final String MENU_COLLECTION = "menu"; // Nombre de la colección "menu"
    private static final String PEDIDOS_COLLECTION = "pedidos"; // Nombre de la colección "pedidos"
    private static MongoClient mongoClient; // Cliente MongoDB compartido

    static {
        try {
            mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrarPedidoPorNombre(String nombreDeComida) {
        try {
            // Conectar a la base de datos
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

            // Obtener las colecciones "menu" y "pedidos"
            MongoCollection<Document> menuCollection = database.getCollection(MENU_COLLECTION);
            MongoCollection<Document> pedidosCollection = database.getCollection(PEDIDOS_COLLECTION);

            // Buscar el producto por nombre en la colección "menu"
            Document producto = menuCollection.find(eq("nombreDeComida", nombreDeComida)).first();

            if (producto != null) {
                // Crear un nuevo documento para el pedido
                Document pedido = new Document("nombreDeComida", producto.getString("nombreDeComida"))
                        .append("detalles", producto.getString("detalles"))
                        .append("precio", producto.getDouble("precio"))
                        .append("fechaPedido", new Date()); // Fecha actual

                // Insertar el pedido en la colección "pedidos"
                pedidosCollection.insertOne(pedido);
                System.out.println("Pedido registrado correctamente.");
            } else {
                System.out.println("El producto con nombre '" + nombreDeComida + "' no existe en el menú.");
            }
        } catch (Exception e) {
            System.err.println("Error al registrar el pedido: " + e.getMessage());
        }
    }

    // Método para cerrar la conexión con MongoDB (opcional)
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
