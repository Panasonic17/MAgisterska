package collectDataIntoMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import entity.Historical;
import entity.MLHistoracal;
import parcers.flightRadarParsers.HistoricalDataParcer;
import parcers.flightRadarParsers.MLParcer;
import recivers.GetHistoricalData;
import scala.MatchError;
import settings.Constants;
import util.Utils;

import java.io.IOException;

public class HistoricalPopulator {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB(Constants.MONGO_DATABACE());
        DBCollection input = database.createCollection(Constants.MONGO_REALTIME_DATA_SCHEMA(), null);

        DBCollection output = database.createCollection(Constants.MONGO_HISTORICAL_DATA_SCHEMA(), null);
        DBCollection rejected = database.createCollection(Constants.MONGO_HISTORICAL_REJECTED_ID(), null);

        Integer start = 514455931;

        while (true) {

            populateOneReccord(input, output, rejected, start);
            String A;
            Thread.sleep(2 * 1000);
            start++;
            BasicDBObject document = new BasicDBObject();
            document.put("curentKEy", start);
            rejected.insert(document);
        }

    }

    public static void populateOneReccord(DBCollection input, DBCollection output, DBCollection rejected, Integer globalID) {
// load data

        try {
//            globalID = input.findOne().get("flightGlobalID").toString();
            String data = GetHistoricalData.getHistoricalFlightDataByFlightID(Integer.toHexString(globalID));
//            System.out.println(data);
//        System.out.println("data " + data);
//        System.out.println("id" + globalID);
            Historical historicalData = null;
            historicalData = HistoricalDataParcer.getHistoricalData(data);
            if (!Utils.isFullHistoracal(historicalData)) {
                throw new Exception("hist not full");
            }
            MLHistoracal mlHistoracal = MLParcer.toMLHistorical(historicalData);
//            System.out.println(historicalData);
//            System.out.println(mlHistoracal);
            BasicDBObject outputObject = MLParcer.historacalMLtoBasicDBObject(mlHistoracal);
//            outputObject.append("timestamp",System.currentTimeMillis());
//            System.out.println(outputObject);
//save data f8cd6
            System.out.println(mlHistoracal);
            System.out.println("OK");
            output.insert(outputObject);
            // remove flight from tmp storage

        } catch (MatchError er) {
//            System.out.println("invalid data" + data);
        } catch (Exception e) {
            BasicDBObject document = new BasicDBObject();
//            System.out.println("send to REJECTED ");
            document.put("flightGlobalID", globalID);
            document.put("time", System.currentTimeMillis());
            rejected.insert(document);
            System.out.println(e.fillInStackTrace());
        } finally {
            BasicDBObject document = new BasicDBObject();
            document.put("flightGlobalID", globalID);
            input.remove(document);
//            System.out.println("deleted");

        }
//        System.out.println(historicalData);

//        System.out.println("class" + historicalData);

    }


    // tmp test

}
