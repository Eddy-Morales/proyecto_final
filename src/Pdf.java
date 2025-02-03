import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Pdf {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "tienda";
    private static final String PEDIDOS_COLLECTION = "pedidos";
    private MongoClient mongoClient;

    public Pdf() {
        mongoClient = new MongoClient(new MongoClientURI(CONNECTION_STRING));
    }

    public void generarPdf(String rutaArchivo) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(rutaArchivo)) {
            // Crear un nuevo documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            // Añadir título al PDF
            document.add(new Paragraph("Pedidos:"));

            // Conectar a la base de datos y obtener la colección "pedidos"
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<org.bson.Document> pedidosCollection = database.getCollection(PEDIDOS_COLLECTION);

            // Iterar sobre los documentos de la colección "pedidos"
            for (org.bson.Document pedido : pedidosCollection.find()) {
                // Formatear la fecha
                Date fecha = pedido.getDate("fechaPedido");
                String fechaFormateada = fecha != null ? new SimpleDateFormat("dd/MM/yyyy").format(fecha) : "N/A";

                // Crear el texto que se escribirá en el PDF
                String texto = "ID: " + pedido.getObjectId("_id").toString() +
                        ", Nombre: " + pedido.getString("nombreDeComida") +
                        ", Detalles: " + pedido.getString("detalles") +
                        ", Precio: " + pedido.getDouble("precio") +
                        ", Fecha: " + fechaFormateada;

                // Añadir cada pedido al PDF
                document.add(new Paragraph(texto));
            }

            // Cerrar el documento PDF
            document.close();

            System.out.println("PDF generado correctamente en: " + rutaArchivo);
        } catch (IOException | DocumentException e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
        } finally {
            // Cerrar la conexión con MongoDB
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}
