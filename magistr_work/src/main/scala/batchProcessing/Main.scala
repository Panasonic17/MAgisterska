package batchProcessing

import org.apache.spark.sql.SparkSession
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}
import com.mongodb.spark.MongoSpark

object Main {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    import spark.implicits
    val data = MongoSpark.load(spark).toDF
    //    _id|aircraftModelCode|callsign|      cityDest|  cityOrigin|  countruDest| countruOrigin|estimatedETA|estimatedUpdated|historicalDelay|historicalFlighttime|      id|  otherETA|otherUpdated|realArrival|realDeparture|scheduledArrival|scheduledDeparture|           traectory|
    data.show()

    val cityPairs = data.select("cityDest", "cityOrigin").where("cityDest <> '' ").where("cityOrigin <> ''").show()

    val countryPairs = data.select("countruDest", "countruOrigin").where("countruDest <> '' ").where("countruOrigin <> ''").show()


    while (true) {
      val a = 1
    }
    //        implicitDS.show()
    //    println(data.count())
    //    implicitDS.select("*").where("cityDest <> ''").show()
  }
}
