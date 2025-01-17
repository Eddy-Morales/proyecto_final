import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.table.DefaultTableModel;

public class metodosCRUD {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // URI de MongoDB
    private static final String DATABASE_NAME = "tienda"; // Nombre de la base de datos
    private static final String COLLECTION_NAME = "menu"; // Nombre de la colección
    private static MongoClient mongoClient; // Cliente MongoDB compartido

    // Inicializar el cliente MongoDB
    static {
        try {
            mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel obtenerMenu() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Detalles");
        modelo.addColumn("Precio");

        try {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Obtener los documentos de la colección
            for (Document doc : collection.find()) {
                Object[] fila = new Object[4];
                fila[0] = doc.getObjectId("_id");
                fila[1] = doc.getString("nombreDeComida");
                fila[2] = doc.getString("detalles");
                fila[3] = doc.getDouble("precio");
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }



    // Cierra el cliente MongoDB al finalizar la aplicación
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
