package test

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import settings.Constants.{MONGO_DATABACE, MONGO_HISTORICAL_DATA_SCHEMA}

case class Flight(_id: String, dofW: Integer, carrier: String, origin: String,
                  dest: String, crsdephour: Long, crsdeptime: Integer, depdelay: Double, crsarrtime: Integer,
                  arrdelay: Double, crselapsedtime: Double, dist: Double) extends Serializable

object EducateML {

  val schema = StructType(Array(
    StructField("_id", StringType, true),
    StructField("dofW", IntegerType, true),
    StructField("carrier", StringType, true),
    StructField("origin", StringType, true),
    StructField("dest", StringType, true),
    StructField("crsdephour", IntegerType, true),
    StructField("crsdeptime", IntegerType, true),
    StructField("depdelay", DoubleType, true),
    StructField("crsarrtime", IntegerType, true),
    StructField("arrdelay", DoubleType, true),
    StructField("crselapsedtime", DoubleType, true),
    StructField("dist", DoubleType, true)
  ))

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .config("spark.mongodb.input.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .config("spark.mongodb.output.uri", s"mongodb://127.0.0.1/${MONGO_DATABACE}.${MONGO_HISTORICAL_DATA_SCHEMA}")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

import spark.implicits._
    val df = getDATAFRAME.getDataframe(spark, "/home/sawa/MAGISTRA WORK/Data/usaGov/MLeducation.csv").as[Flight]

    df.show()

  }
}
