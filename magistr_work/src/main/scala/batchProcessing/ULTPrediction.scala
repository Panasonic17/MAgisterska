package batchProcessing

import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions.{lag, _}
import org.apache.spark.sql.{DataFrame, _}

import scala.collection.mutable.ListBuffer

object ULTPrediction {
  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()

    import spark.implicits._
    val data = MongoSpark.load(spark)
      .toDF
      .filter($"iataOrigin".isNotNull)
      .filter($"iataDest".isNotNull)
      .filter($"iataDest" =!= "")


    val raw_data = Utils.dehipherIATA(data)


    val preparedData: DataFrame = raw_data.
      withColumn("id", monotonically_increasing_id())
      .where(size($"traectory") > 12)
      .select($"id", $"realArrival", $"realDeparture", $"scheduledArrival", $"scheduledDeparture", explode($"traectory"), $"destlat", $"destlon", $"originlat", $"originlon")
      .na.drop()
      .withColumn("time in flight", $"col".getItem("ts") - $"realDeparture") // seconds
      .withColumn("realDistance", UDFS.udfGetDistance($"destlat", $"destlon", $"col.lat", $"col.lng")) // meter
      .withColumn("distance", UDFS.udfGetDistance($"destlat", $"destlon", $"originlat", $"originlon"))
      .withColumn("flightTime", $"realArrival" - $"realDeparture")
      .repartition($"id")
      .orderBy("col.ts")
          .cache()
    //
    //    visualizate(preparedData.withColumn("predictedFlightTime", $"realDistance" / ($"col.spd" * 1000 / 3600)), "noaceleration") //seconds



    aceleratedwithAVGLag(preparedData, 5)
    aceleratedwithAVGLag(preparedData, 7)
    aceleratedwithAVGLag(preparedData, 9)
    aceleratedwithAVGLag(preparedData, 10)
    aceleratedwithAVGLag(preparedData, 30)
    aceleratedwithAVGLag(preparedData, 100)
    //    aceleratedwithAVGLag(preparedData.limit(10), 1)
    // mp into another function and optimization


    // build charts by  percent of time pohybka in meters and vise versa
    // mb do not pmdul pohybky


    // apply function of speed acseleration

    // percent in way pohybka in minutes
    //    val graphData = dummyPredictions


    //    graphData.show(60)
    //    Utils.visualiseDFToBAR(50, graphData, 1, 0, "sawa")
  }

  def visualizate(df: DataFrame, name: String) = {
    import df.sparkSession.implicits._
    val data = df.withColumn("predictedDestinationTime", $"predictedFlightTime" + $"time in flight" + $"realDeparture")
      .withColumn("pohybka", ($"realArrival" - $"predictedDestinationTime") / ($"realArrival"))
      .withColumn("flightedDistance", UDFS.udfGetDistance($"originlat", $"originlon", $"col.lat", $"col.lng"))
      .withColumn("percentOFWay", ($"flightedDistance") / ($"distance") * 40) // 40 and 4
      .withColumn("rounded", round($"percentOFWay") / 4)
      .withColumn("percentOFTime", abs($"pohybka" / $"flightTime"))
      .groupBy($"rounded")
      .avg("percentOFTime")
      .orderBy("rounded")
      .na.drop().cache()

    Utils.visualiseDFToBAR(50, data.where($"avg(percentOFTime)" < 1), 1, 0, name)
  }

  def aceleratedwithLag(df: DataFrame, lagValue: Int) = {
    import df.sparkSession.implicits._

    val window = Window.partitionBy("id").orderBy(desc("col.ts"))

    val acelerated = df.withColumn("dU", lag($"col.spd", lagValue, null).over(window) - $"col.spd")
      .withColumn("dT", lag($"col.ts", lagValue, null).over(window) - $"col.ts")
      .withColumn("acseleration1", $"dU" / $"dT")
      .withColumn("predictedFlightTime", UDFS.getTimeWithAcselerationUDF($"realDistance", $"col.spd", $"acseleration1"))

    visualizate(acelerated, "acelerated" + lagValue)

  }

  def aceleratedwithAVGLag(df: DataFrame, range: Int) = {
    import df.sparkSession.implicits._
    val window = Window.partitionBy("id").orderBy("col.ts")

    var finalDF = df

    var exspr1 = new ListBuffer[Column]()
    var exspr2 = new ListBuffer[Column]()

    for (a <- 1 to range) {
      finalDF = finalDF.withColumn("lagtm" + a, lag($"col.ts", a, null).over(window))
      finalDF = finalDF.withColumn("lagspd" + a, lag($"col.spd", a, null).over(window))
      exspr1 += new Column("lagtm" + a)
      exspr2 += new Column("lagspd" + a)
    }


    val acelerated = finalDF.withColumn("acseleration", exspr1.reduce(_ + _) / exspr2.reduce(_ + _))
      .withColumn("predictedFlightTime", UDFS.getTimeWithAcselerationUDF($"realDistance", $"col.spd", $"acseleration"))

    visualizate(acelerated, "acelerated" + "vag" + range)

  }
}
