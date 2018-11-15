package spark.streaming.distance

import config.CONFIG
import model.{Coordinates, PlainPosition}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Duration, Seconds}
import spark.elasticsearch.ElasticSearchSender
import test.App

class DistanceToDestinationCalculator extends Serializable {

  def calculateDistanceToDestinationAiroports(traficStream: DStream[PlainPosition], airoportCoordinates: Broadcast[Map[String, Coordinates]]) = {
    val distances = traficStream.window(Seconds(30), Seconds(30)).foreachRDD(rdd => calculateAllDistancesForBatch(rdd, airoportCoordinates))
  }

  def calculateAllDistancesForBatch(plainsPositions: RDD[PlainPosition], airoportCoordinates: Broadcast[Map[String, Coordinates]]): RDD[((String, Long, String), Double)] = {
    val distinctPlains = plainsPositions.map(plainsPositions => (plainsPositions.flight, plainsPositions)).reduceByKey((a, b) => a)
    val disnances = distinctPlains
      .map(plainPosition => (
        (plainPosition._2.destination, plainPosition._2.time, plainPosition._2.flight),
        Utils.getDistance(plainPosition._2.plainLat, plainPosition._2.plainLon, airoportCoordinates.value.getOrElse(plainPosition._2.destination, Coordinates(0, 0)))
      ))
    //    disnances.foreach(println)
    ElasticSearchSender.saveDistancesToElasticsearch(disnances)
    disnances
  }

  def criticalDistance(data: Iterable[PlainPosition]): Unit = {
    val list = scala.collection.mutable.ListBuffer.empty[(Double, Double, Double)]

    for (position1 <- data) {
      for (position2 <- data) {
        if (position2 == position1) {} else {
          val dist = Utils.getDistance(position2, position1)
          if (dist < CONFIG.getMinDistance)
            list += Tuple3(dist, position2.plainLon, position2.plainLat)
          println("add ")
        }
      }
    }
    println(list.toList)
    val rdd=App.sc.parallelize(list.toList)
    rdd.foreach(d=> println("data +++++ "+ d))
        ElasticSearchSender.plainDistancesToElasticsearch(rdd)
    println("end sending ")
  }
}
