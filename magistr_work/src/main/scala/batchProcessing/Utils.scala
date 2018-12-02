package batchProcessing

import org.apache.spark.sql.{DataFrame, SparkSession}
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

object Utils {

  def getAiroportsInfo(spark: SparkSession): DataFrame = {
    spark.read.csv("/home/sawa/MAGISTRA_WORK/Data/airports.dat")
      .withColumnRenamed("_c2", "City")
      .withColumnRenamed("_c3", "Country")
      .withColumnRenamed("_c4", "iata")
      .withColumnRenamed("_c6", "lat or lon")
      .withColumnRenamed("_c7", "lat or lon")
  }

  def getSparkSeesion() = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()
    spark
  }

  def dehipherIATA(spark: SparkSession, df: DataFrame): DataFrame = {
    val airoportsInfo = Utils.getAiroportsInfo(spark)
    import spark.implicits._

    df.join(airoportsInfo, $"iataOrigin" === $"iata")
      .withColumnRenamed("City", "originCity")
      .withColumnRenamed("Country", "originCountry")

      .drop("iata")
      .drop("_c0")
      .drop("_c1")
      .drop("_c5")
      .drop("lat or lon")
      .drop("_c8")
      .drop("_c9")
      .drop("_c10")
      .drop("_c11")
      .drop("_c12")
      .drop("_c13")
      .join(airoportsInfo, $"iataDest" === $"iata")
      .withColumnRenamed("City", "destCity")

      .withColumnRenamed("Country", "destCountry")
  }

  def visualiseDFToBAR(count: Int, dataFrame: DataFrame, xColumn: Int, yColumn: Int,name:String) = {
    val data = dataFrame.limit(10).collect().map(row => (row.getAs[Long](xColumn), row.getAs[String](yColumn)))
    util.Utils.createBarChart(data,name)
  }
}
