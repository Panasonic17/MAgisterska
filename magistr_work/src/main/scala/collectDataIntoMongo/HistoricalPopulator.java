package collectDataIntoMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import entity.Historical;
import parcers.flightRadarParsers.HistoricalDataParcer;
import recivers.GetHistoricalData;
import settings.Constants;

import java.io.IOException;

public class HistoricalPopulator {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB(Constants.MONGO_DATABACE());
        DBCollection input = database.createCollection(Constants.MONGO_REALTIME_DATA_SCHEMA(), null);

        DBCollection output = database.createCollection(Constants.MONGO_HISTORICAL_DATA_SCHEMA(), null);

        while (true) {
            populateOneReccord(input, output);
            String A;
            System.in.read();
        }

    }

    public static void populateOneReccord(DBCollection input, DBCollection output) {
// load data
        String globalID = input.findOne().get("flightGlobalID").toString();

        String data = GetHistoricalData.getHistoricalFlightDataByFlightID(globalID);
        System.out.println(data);
//        System.out.println("data " + data);
//        System.out.println("id" + globalID);
        Historical historicalData = HistoricalDataParcer.getHistoricalData(data);
//        System.out.println(historicalData);

//        System.out.println("class" + historicalData);
        BasicDBObject outputObject = HistoricalDataParcer.HistoricalDataToBasicDBObject(historicalData);
//save data f8cd6
        output.insert(outputObject);
        // remove flight from tmp storage
        BasicDBObject document = new BasicDBObject();
        document.put("flightGlobalID", globalID);
        input.remove(document);
    }

    // tmp test

}
