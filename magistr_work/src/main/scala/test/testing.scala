package test

import org.apache.spark.sql.SparkSession
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

object testing {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    spark

    import com.mongodb.spark.MongoSpark
    import org.apache.spark.sql.Dataset

    val implicitDS = MongoSpark.load(spark).toDF
    //    implicitDS.printSchema()
//        implicitDS.show()
        println(implicitDS.count())
//    implicitDS.select("*").where("cityDest <> ''").show()
  }
}
