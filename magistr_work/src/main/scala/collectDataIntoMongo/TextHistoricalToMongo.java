package collectDataIntoMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import entity.Historical;
import entity.MLHistoracal;
import parcers.flightRadarParsers.HistoricalDataParcer;
import parcers.flightRadarParsers.MLParcer;
import settings.Constants;

import java.io.*;

public class TextHistoricalToMongo {

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
                    try {

                        Historical  historicalData = HistoricalDataParcer.getHistoricalData(line);

                        BasicDBObject outputObject = HistoricalDataParcer.HistoricalDataToBasicDBObject(historicalData);
                        output.insert(outputObject);

                    }catch (Exception e){

                    }
                    }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
