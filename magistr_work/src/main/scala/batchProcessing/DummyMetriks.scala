package batchProcessing

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.functions._

object DummyMetriks {

  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()
    import spark.implicits._

    val data = MongoSpark.load(spark).toDF

    val dechipherIATA = Utils.dehipherIATA(spark, data).cache()


    val a = dechipherIATA.groupBy("originCountry").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, a, 1, 0, "originCountry")

    val b = dechipherIATA.groupBy("originCity").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, b, 1, 0, "originCity")

    val c = dechipherIATA.groupBy("destCity").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, c, 1, 0, "destCity")

    val d = dechipherIATA.groupBy("destCountry").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, d, 1, 0, "destCountry")


    val e = dechipherIATA.withColumn("city->city",concat($"originCity",$"destCity")).groupBy("city->city").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, e, 1, 0, "city -> city")

    val f = dechipherIATA.withColumn("country->country",concat($"originCountry",$"destCountry")).groupBy("country->country").count().orderBy(desc("count")).limit(10)
    Utils.visualiseDFToBAR(10, f, 1, 0, "country -> country")

  }

}
