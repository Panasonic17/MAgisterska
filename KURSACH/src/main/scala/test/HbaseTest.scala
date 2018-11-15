package test

import model.Traffic
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Durations, Seconds, StreamingContext}
import spark.streaming.reciver.SatoriReciver

object HbaseTest {
//  val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[4]")
//  //.set("es.nodes", "35.230.25.238").set("es.nodes.discovery","false") //.set("es.index.auto.create", "true").set("es.port", "9200").
//  val ssc = new StreamingContext(sparkConf, Durations.seconds(5))
//  val sc = ssc.sparkContext


  val sparkSess = SparkSession.builder().master("local[*]").appName("My App").getOrCreate()
  val sc = sparkSess.sparkContext
  val sql=sparkSess.sqlContext
  val ssc = new StreamingContext(sc, Seconds(1))

  def main(args: Array[String]): Unit = {

    val endpoint = "wss://open-data.api.satori.com"
    val appkey = "9fbd1c4BEa889C66cFf83B042B0fDCed"
    val channel = "air-traffic"
    val lines = ssc.receiverStream(new SatoriReciver(endpoint, appkey, channel))
    val pourTraffic: DStream[Traffic] = lines.map(row => Traffic.parceTraffic(row))


    import sparkSess.implicits._
    pourTraffic.foreachRDD(rdd=> sql.createDataFrame(rdd).as[Traffic].show(1))

    pourTraffic.print()
    ssc.start()
    try
      ssc.awaitTermination()
    catch {
      case e: InterruptedException =>
        e.printStackTrace()
    }

  }
//  private def storeNotArrestedToHbase(spark: SparkSession,dataStream DStream[Traffic]): Unit = {
//    import spark.implicits._
//
//    val notArrestedDataset =
//    HBaseWriter.createTable()
//    HBaseWriter.insert(notArrestedDataset)
//  }
}
