package batchProcessing

import java.io.File

import entity.Historical
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartUtilities}
import org.jfree.data.category.DefaultCategoryDataset
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

object Utils {

  def getTimeFromAceleration(L: Double, U: Double, A: Double): Double = {
    val a = A / 2
    val b = U
    val c = -L

    if (a == 0) return L / U

    val de = b * b + 4 * a * c
    val x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a)
    val x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a)

    if ((x1 < 0 && x2 < 0) ||x1.isNaN||x2.isNaN) return L / U
    if (x1 > 0 && x2 < 0) return x1
    if (x1 < 0 && x2 > 0) return x2

    return Math.min(x1, x2)

  }

  def getAiroportsInfo(spark: SparkSession): DataFrame = {
    spark.read.csv("/home/sawa/MAGISTRA_WORK/Data/airports.dat")
      .withColumnRenamed("_c2", "City")
      .withColumnRenamed("_c3", "Country")
      .withColumnRenamed("_c4", "iata")
      .withColumnRenamed("_c6", "lat or lon1")
      .withColumnRenamed("_c7", "lat or lon2")
  }

  def getSparkSeesion() = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
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

  def dehipherIATA(df: DataFrame): DataFrame = {
    val spark = df.sparkSession
    val airoportsInfo = Utils.getAiroportsInfo(spark)
    import spark.implicits._

    df.join(airoportsInfo, $"iataOrigin" === $"iata")
      .withColumnRenamed("City", "originCity")
      .withColumnRenamed("Country", "originCountry")
      .withColumnRenamed("lat or lon1", "originlat")
      .withColumnRenamed("lat or lon2", "originlon")
      .drop("iata")
      .drop("_c0")
      .drop("_c1")
      .drop("_c5")
      .drop("_c8")
      .drop("_c9")
      .drop("_c10")
      .drop("_c11")
      .drop("_c12")
      .drop("_c13")
      .join(airoportsInfo, $"iataDest" === $"iata")
      .withColumnRenamed("City", "destCity")

      .withColumnRenamed("Country", "destCountry")
      .withColumnRenamed("lat or lon1", "destlat")
      .withColumnRenamed("lat or lon2", "destlon")
  }


  def visualiseDFToBAR(count: Int, dataFrame: DataFrame, xColumn: Int, yColumn: Int, name: String) = {
    val data = dataFrame.limit(count).collect().map(row => (row.getAs[Number](xColumn), row.getAs[String](yColumn)))
    util.Utils.createBarChart(data, name)
  }

  def getSlicedValues[T](data: Seq[T], count: Int): Seq[T] = {
    val step = data.size / count
    for (i <- Range(0, data.size, step)) yield {
      data(i)
    }
  }

  def isFullHistoracal(historical: Historical): Boolean = {


    if (historical.iataDest == null) {
      print("empty iata ");
      return false
    }
    if (historical.iataOrigin == null) {
      print("empty iata ");
      return false
    }
    if (historical.timeData.realArrival < 10) {
      print("empty ar time ");
      return false
    }
    if (historical.timeData.realDeparture < 10) {
      print("empty dep time ");
      return false
    }
    if (historical.timeData.scheduledDeparture < 10) {
      print("empty time  ");
      return false
    }
    if (historical.timeData.scheduledArrival < 10) {
      print("empty time  ");
      return false
    }
    return true

  }


  def getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double = {
    val earthRadius = 6371000 //meters
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    val dist = (earthRadius * c).toFloat
    dist
  }

  def createBarChart(data: Array[Tuple2[Long, String]], name: String) = {
    val dataset = new DefaultCategoryDataset

    data.foreach(data => dataset.addValue(data._1, data._2, data._2))
    val barChart = ChartFactory.createBarChart("CAR USAGE STATIStICS", "Category", "Score", dataset, PlotOrientation.VERTICAL, true, true, false)

    val width = 640
    /* Width of the image */
    val height = 480
    /* Height of the image */
    val BarChart = new File(name + ".jpeg")
    ChartUtilities.saveChartAsJPEG(BarChart, barChart, width, height)
  }
}
