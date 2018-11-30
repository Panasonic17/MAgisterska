package test

import org.apache.spark.sql.SparkSession
import parcers.flightRadarParsers.MLParcer
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

object testing {
  def main(args: Array[String]): Unit = {

    //    val spark = SparkSession
    //      .builder
    //      .master("local[*]")
    //      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
    //      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
    //      .appName("StructuredNetworkWordCount")
    //      .getOrCreate()
    //
    //    spark
    //
    //    import com.mongodb.spark.MongoSpark
    //
    //
    //    val a: Int  = 1542820961
    //    val implicitDS = MongoSpark.load(spark).toDF
    //    //    implicitDS.printSchema()
    //    //        implicitDS.show()
    //    println(implicitDS.count())
    //    //    implicitDS.select("*").where("cityDest <> ''").show()

    val d = "MLHistoracal(A320,DTW,LGA,[Lentity.MLTraectory;@3b2553d9,1543180320,1543186680,1543180416,1543185642)  MLTraectory(40.769485,-73.860916,14,1543185932)MLTraectory(40.631561,-73.998291,199,1543185315)MLTraectory(40.421028,-74.24498,240,1543185046)MLTraectory(40.37252,-74.636742,273,1543184735)MLTraectory(40.594807,-75.047729,340,1543184464)MLTraectory(41.001163,-76.572525,448,1543183756)MLTraectory(41.099667,-79.228905,359,1543182815)MLTraectory(41.213253,-78.703804,430,1543182392)MLTraectory(42.026917,-81.751823,450,1543181275)MLTraectory(42.363605,-83.011993,296,1543180731)MLTraectory(42.233551,-83.332359,161,1543180489)"

    println(MLParcer.goggleCloudStrToMLHistoracal(d))
  }
}
