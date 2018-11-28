package collectDataIntoMongo

import com.mongodb.spark.MongoSpark
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Durations, StreamingContext}
import org.bson.Document
import parcers.flightRadarParsers.{Flightradar24Parcer, JsonParcers}
import recivers.PlainReciver
import settings.Constants._



object MongoSimplePopulator {
  def main(args: Array[String]): Unit = {



    val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[*]")
    sparkConf.set("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_REALTIME_DATA_SCHEMA}")
    sparkConf.set("es.index.auto.create", "true")

    val ssc = new StreamingContext(sparkConf, Durations.seconds(SPARK_REALTIME_HTTP_REQUEST_FREEQUENSY_SECONDS + 10))
    import org.bson.Document

    ssc.sparkContext.setLogLevel("ERROR")
    val lines = ssc.receiverStream(new PlainReciver())


    //    lines.map(Flightradar24Parcer.parceValuableRealtimeDataElasticsearch)
    //      .foreachRDD(rdd => rdd.distinct().saveToEs(ELSATICSERACH_REALTIME_STORAGE))

    //    lines.map(row=> row.split(",\"|\":")(1)).window(Durations.minutes(60),Durations.minutes(1)).map(r=>{var new BasicDBObject })

    lines.map(Flightradar24Parcer.parceValuableRealtimeDataMongo)
      .map(JsonParcers.valuableRealtimeDataMongoToJson)
      .map(i => Document.parse(i)).map(doc => doc.append("timestamp", System.currentTimeMillis()))
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
