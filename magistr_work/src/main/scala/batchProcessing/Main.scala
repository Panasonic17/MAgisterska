package batchProcessing

import org.apache.spark.sql.SparkSession
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.functions._
object Main {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .master("local[8]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    import spark.implicits._
    val data = MongoSpark.load(spark).toDF

    val mlNormal=Regression.getValidHistoricalReggersionRecords(data).withColumn("delay",$"realArrival"-$"scheduledArrival")

    println(mlNormal.count())



  }
}
