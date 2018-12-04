package batchProcessing

import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.mllib.recommendation.Rating

object ColaborativeFiltering {
  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()

//    val govData = PrepareGovData.getDataGovData(spark, "C:\\WORK_DIR\\magistra\\MAgisterska\\Data\\usaGov\\")
//    Rating r
//    val rank = 10
//    val numIterations = 10
//    val model = ALS.train(govData, rank, numIterations, 0.01)
  }
}
