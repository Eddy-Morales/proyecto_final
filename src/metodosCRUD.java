import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.bson.types.ObjectId;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq; // Importar el método eq

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

    // Método para insertar un nuevo platillo
    public void insertarPlatillo(String nombreDeComida, String detalles, double precio) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
        Document platillo = new Document("nombreDeComida", nombreDeComida)
                .append("detalles", detalles)
                .append("precio", precio);
        collection.insertOne(platillo);
        System.out.println("Platillo insertado correctamente.");
    }

    // Método para obtener el menú como DefaultTableModel
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

    // Método para actualizar un platillo existente
    public void actualizarPlatillo(String id, String nuevoNombre, String nuevosDetalles, double nuevoPrecio) {
        try {
            // Conectar a la base de datos y colección
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Convertir el ID ingresado a ObjectId
            ObjectId objectId = new ObjectId(id);

            // Crear el documento con los nuevos valores
            Document nuevosValores = new Document("nombreDeComida", nuevoNombre)
                    .append("detalles", nuevosDetalles)
                    .append("precio", nuevoPrecio);

            // Actualizar el documento en la colección
            collection.updateOne(eq("_id", objectId), new Document("$set", nuevosValores));

            System.out.println("Platillo actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el platillo: " + e.getMessage());
        }
    }


    // Método para eliminar un platillo
    public void eliminarPlatillo(String id) {
        try {
            // Conectar a la base de datos y colección
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Convertir el ID ingresado a ObjectId
            ObjectId objectId = new ObjectId(id);

            // Utilizar Filters.eq para filtrar por ID y eliminar el documento
            collection.deleteOne(eq("_id", objectId));
            System.out.println("Platillo eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el platillo: " + e.getMessage());
        }
    }


    public boolean existePlatillo(String id) {
        try {
            // Conectar a la base de datos y colección
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Convertir el ID ingresado a ObjectId
            ObjectId objectId = new ObjectId(id);

            // Buscar el documento en la colección
            Document found = collection.find(eq("_id", objectId)).first();

            // Retornar true si se encuentra el platillo, false si no
            return found != null;
        } catch (Exception e) {
            System.err.println("Error al verificar existencia del platillo: " + e.getMessage());
            return false; // Retornar false en caso de error
        }
    }

    public void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
