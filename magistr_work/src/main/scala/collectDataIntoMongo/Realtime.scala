package collectDataIntoMongo

import com.mongodb.spark.MongoSpark
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.bson.Document
import org.elasticsearch.spark._
import parcers.flightRadarParsers.{Flightradar24Parcer, JsonParcers}
import recivers.PlainReciver
import settings.Constants._

//import org.elasticsearch.spark._

object MongoSimplePopulator {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[*]")
    sparkConf.set("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("es.index.auto.create", "true")

    val ssc = new StreamingContext(sparkConf, Durations.seconds(SPARK_REALTIME_HTTP_REQUEST_FREEQUENSY_SECONDS * 2))
    import org.bson.Document

    ssc.sparkContext.setLogLevel("ERROR")
    val lines = ssc.receiverStream(new PlainReciver()).window(Durations.minutes(30),Durations.minutes(1))


//    lines.map(Flightradar24Parcer.parceValuableRealtimeDataElasticsearch)
//      .foreachRDD(rdd => rdd.distinct().saveToEs(ELSATICSERACH_REALTIME_STORAGE))

    lines.map(Flightradar24Parcer.parceValuableRealtimeDataMongo)
      .map(JsonParcers.valuableRealtimeDataMongoToJson)
      .map(i => Document.parse(i))
      .foreachRDD(rdd => {
        val dis = rdd.distinct()
        println(dis.count())
        MongoSpark.save(dis)
      })

    //
    //    val outputData = lines.map(data => Flightradar24Parcer.parsePlainData(data))
    //
    //    outputData.map(data => JsonParcers.flightRadarRecordtoJson(data)).map(i => Document.parse(i))
    //      .foreachRDD(rdd => MongoSpark.save(rdd))
    //
    //    outputData.foreachRDD(rdd => rdd.saveToEs("test5/test5"))
    //    //    lines.foreachRDD(rd=>print(""))
    ssc.start()
    ssc.awaitTermination()
  }
}
