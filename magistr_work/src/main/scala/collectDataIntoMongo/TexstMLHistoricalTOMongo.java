package collectDataIntoMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import entity.MLHistoracal;
import parcers.flightRadarParsers.MLParcer;
import settings.Constants;

import java.io.*;

public class TexstMLHistoricalTOMongo {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB(Constants.MONGO_DATABACE());

        DBCollection output = database.createCollection(Constants.MONGO_HISTORICAL_DATA_SCHEMA(), null);

        File folder = new File("C:\\WORK_DIR\\magistra\\MAgisterska\\Data\\googleCloud");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    MLHistoracal mlHistoracal = MLParcer.goggleCloudStrToMLHistoracal(line);
                    BasicDBObject outputObject = MLParcer.historacalMLtoBasicDBObject(mlHistoracal);
                    output.insert(outputObject);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
