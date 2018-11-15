package spark.elasticsearch

import config.CONFIG
import model.Traffic
import org.apache.spark.rdd.RDD
import org.elasticsearch.spark.rdd.EsSpark

case class elkDistance(destination:String,time:Long,flight:String,distance:Double)

case class plainDistance(disiance:Double,location:location,timestamp:Long)

case class location(lat:Double,lon:Double)

object ElasticSearchSender {
  def saveDistancesToElasticsearch(data:RDD[((String, Long,String), Double)])= {
    EsSpark.saveToEs(data.map(row => elkDistance(row._1._1, row._1._2, row._1._3, row._2)), "test_mapping3/test")
  }
  def saveUpdaterToElasticserach(data:RDD[Traffic])={
    EsSpark.saveToEs(data, "blablabla/te123" ,Map("es.nodes" -> CONFIG.getElasticIp,"es.nodes.discovery" -> "false","es.nodes.wan.only"->"true"))
  }
  def plainDistancesToElasticsearch(data:RDD[Tuple3[Double,Double,Double]])={
    println("send")
   val dataToELK= data.map(data=>plainDistance(data._1,location(data._2,data._3),System.currentTimeMillis()))
    EsSpark.saveToEs(dataToELK, "locations/te123",Map("es.nodes" -> CONFIG.getElasticIp,"es.nodes.discovery" -> "false","es.nodes.wan.only"->"true") )
  }
}
