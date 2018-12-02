package util

import java.io.File

import entity.Historical
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartUtilities}
import org.jfree.data.category.DefaultCategoryDataset

object Utils {

  def getSlicedValues[T](data: Seq[T], count: Int): Seq[T] = {
    val step = data.size / count
    for (i <- Range(0, data.size, step)) yield {
      data(i)
    }
  }

  def isFullHistoracal(historical: Historical): Boolean = {

    if (historical.traectory.length < settings.Constants.TRAECTORY_SLICING_COUNT) {
      print("empty Traectory ");
      return false
    }

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

    data.foreach(data => dataset.addValue(data._1, data._2, "name"))
    val barChart = ChartFactory.createBarChart("CAR USAGE STATIStICS", "Category", "Score", dataset, PlotOrientation.VERTICAL, true, true, false)

    val width = 640
    /* Width of the image */
    val height = 480
    /* Height of the image */
    val BarChart = new File(name + ".jpeg")
    ChartUtilities.saveChartAsJPEG(BarChart, barChart, width, height)
  }
}

