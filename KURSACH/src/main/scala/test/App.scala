package test

import java.util.Random

import config.CONFIG
import model.{Coordinates, PlainPosition, Traffic}
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.elasticsearch.spark.rdd.EsSpark
import plainDistances.DistanceBeetwinPlainsCalculator
import spark.BroadcastGenerator
import spark.elasticsearch.ElasticSearchSender
import spark.streaming.distance.{DistanceToDestinationCalculator, Utils}
import spark.streaming.reciver.SatoriReciver

object App {
  val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[4]")
  //.set("es.nodes", "35.230.25.238").set("es.nodes.discovery","false") //.set("es.index.auto.create", "true").set("es.port", "9200").
  val ssc = new StreamingContext(sparkConf, Durations.seconds(5))
  val sc = ssc.sparkContext

  def main(args: Array[String]): Unit = {


    val broadcastGenerator = new BroadcastGenerator
    val cityNames = sc.broadcast(broadcastGenerator.getIataCityName("C:\\WORK_DIR\\Projects\\KURSACH\\Data\\airport-codes.csv"))
    val countryNames = sc.broadcast(broadcastGenerator.getIataCountryName("C:\\WORK_DIR\\Projects\\KURSACH\\Data\\airport-codes.csv"))
    val airoportCoordinates: Broadcast[Map[String, Coordinates]] = sc.broadcast(broadcastGenerator.getIataCoorinates("C:\\WORK_DIR\\Projects\\KURSACH\\Data\\airport-codes.csv"))

    ssc.sparkContext.setLogLevel("ERROR")

    val endpoint = "wss://open-data.api.satori.com"
    val appkey = "9fbd1c4BEa889C66cFf83B042B0fDCed"
    val channel = "air-traffic"

    val lines = ssc.receiverStream(new SatoriReciver(endpoint, appkey, channel))
    val pourTraffic = lines.map(row => Traffic.parceTraffic(row))
    //    lines.print()
    //    calculate distance between plain and airoport and send to elk
    val plainPositions: DStream[PlainPosition] = pourTraffic.map(traffic => new PlainPosition(traffic))
    //    val distanceCalculator = new DistanceToDestinationCalculator
    //    distanceCalculator.calculateDistanceToDestinationAiroports(plainPositions, airoportCoordinates)
    //
    //    save updater to elk
    //    val updated: DStream[Traffic] = pourTraffic.map(trafic => trafic.updateCityAndCountry(cityNames.value, countryNames.value))
    //    updated.foreachRDD(rdd => ElasticSearchSender.saveUpdaterToElasticserach(_))


    //calculate distances beetwin plains
    val distanceToDestinationCalculator: DistanceToDestinationCalculator = new DistanceToDestinationCalculator
    //    val distanceBeetwinPlainsCalculator: DistanceBeetwinPlainsCalculator = new DistanceBeetwinPlainsCalculator
    val preparedDstreamForDistances: Unit = plainPositions.window(Durations.seconds(10), Durations.seconds(5))
      .map(data => (data.getCoorditationsKey, data))
      .groupByKey()
      .map(rdd => rdd._2).flatMap(data => {
      val list = scala.collection.mutable.ListBuffer.empty[(Double, Double, Double)]
              for (position1 <- data) {
                for (position2 <- data) {
                  if (position2 == position1) {} else {
                    val dist = Utils.getDistance(position2, position1)
                    if (dist < CONFIG.getMinDistance)
                      list += Tuple3(dist, position2.plainLon, position2.plainLat)
                  }
                }
              }
      list.toList
    }).foreachRDD(rdd=> ElasticSearchSender.plainDistancesToElasticsearch(rdd))

    ssc.start()
    try
      ssc.awaitTermination()
    catch {
      case e: InterruptedException =>
        e.printStackTrace()
    }

  }

}
