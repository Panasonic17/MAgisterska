package batchProcessing

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}

object testing {

  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()
    import org.apache.spark.ml.fpm.FPGrowth
import spark.implicits._
    val dataset = spark.createDataset(Seq(
      "a b c",
      "a b c d",
      "a b")
    ).map(t => t.split(" ")).toDF("items")

    dataset.show()
    val fpgrowth = new FPGrowth().setItemsCol("items").setMinSupport(0.5).setMinConfidence(0.6)
    val model = fpgrowth.fit(dataset)

    // Display frequent itemsets.
    model.freqItemsets.show()

    // Display generated association rules.
    model.associationRules.show()

    // transform examines the input items against all the association rules and summarize the
    // consequents as prediction
    model.transform(dataset).show()
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
