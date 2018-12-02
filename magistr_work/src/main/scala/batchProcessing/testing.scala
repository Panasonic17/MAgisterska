package batchProcessing

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}

object testing {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = Utils.getSparkSeesion()
    import spark.implicits._

    val data = MongoSpark.load(spark).toDF

    val mlNormal: DataFrame = Regression.getValidHistoricalReggersionRecords(data).withColumn("delay", $"realArrival" - $"scheduledArrival")

    val airoportsInfo = Utils.getAiroportsInfo(spark)

    dehipherIATA(spark,mlNormal).show()
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
}
