package test

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.bson.Document
import recivers.PlainReciver
import settings.Constants._

//import org.elasticsearch.spark._

object MongoSimplePopulator {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[*]")
    sparkConf.set("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("es.index.auto.create", "true")

    val ssc = new StreamingContext(sparkConf, Durations.seconds(10))
    import org.bson.Document

    ssc.sparkContext.setLogLevel("ERROR")
    val lines = ssc.receiverStream(new PlainReciver(5)).window(Durations.seconds(30), Durations.seconds(10))



    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    // updete state by window
    lines.map(row => row.split(",\"|\":")(1)).foreachRDD(rdd => println(rdd.distinct().count()))

    lines.map(row => (row.split(",\"|\":")(1), row)).reduce((k1, k2) => k1) // possible to add reduce by key and window  (wery big window )
    //    lines.foreachRDD(rdd=>rdd.foreach(println))


    ssc.start()
    ssc.awaitTermination()
  }

}
