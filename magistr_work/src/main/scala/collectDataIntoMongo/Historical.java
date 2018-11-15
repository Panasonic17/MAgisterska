package collectDataIntoMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import recivers.GetHistoricalData;
import settings.Constants;

public class Historical {

    public static void main(String[] args) throws InterruptedException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB(Constants.MONGO_DATABACE());
        DBCollection input = database.createCollection(Constants.MONGO_REALTIME_DATA_SCHEMA(), null);

        DBCollection output = database.createCollection(Constants.MONGO_HISTORICAL_DATA_SCHEMA(), null);

        String globalID = input.findOne().get("flightGlobalID").toString();
        String data = GetHistoricalData.getHistoricalFlightDataByFlightID(globalID);
        System.out.println(data);
        System.out.println(globalID);
        Thread.sleep(1000 * 60);
        BasicDBObject document = new BasicDBObject();
        document.put("flightGlobalID", globalID);

        input.remove(document);

//        System.out.println(input.findOne());
    }

}
