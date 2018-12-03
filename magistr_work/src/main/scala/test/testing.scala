package test

import org.apache.spark.sql.SparkSession

object testing {
  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql.expressions.Window
    import org.apache.spark.sql.functions._

    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()
    import spark.implicits._
    val customers = spark.sparkContext.parallelize(List(("Alice", "2016-05-01", 50.00),
      ("Alice", "2016-05-03", 45.00),
      ("Alice", "2016-05-04", 55.00),
      ("Bob", "2016-05-01", 25.00),
      ("Bob", "2016-05-04", 29.00),
      ("Bob", "2016-05-06", 27.00))).
      toDF("name", "date", "amountSpent")

    val wSpec1 = Window.partitionBy("name").orderBy("date").rowsBetween(-1, 1)

    customers.withColumn( "movingAvg",
      avg(customers("amountSpent")).over(wSpec1)).show()

  }
}
