package collectDataIntoMongo

import com.mongodb.BasicDBObject
import com.mongodb.spark.MongoSpark
import entity.TimeData
import org.apache.spark.sql.SparkSession
import parcers.flightRadarParsers.HistoricalDataParcer.{PopulateDbObjectByTimeData, TraectoryArrayTOBasicDBObject}
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

object HistoracalUSAGOV {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    val input = spark.read.option("header", "true").option("inferSchema", "true").csv("/home/sawa/MAGISTRA WORK/Data/usaGov")

    val normalColumnNames = input
      .withColumnRenamed("DEST", "FAACodeDest")
      .withColumnRenamed("ORIGIN", "FAACodeDest")
      .withColumnRenamed("DEP_DELAY", "dep_delay_minutes")
      .withColumnRenamed("DISTANCE", "")
//    input.show()
    MongoSpark.save(normalColumnNames)

  }
}
