package batchProcessing

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import org.apache.spark.sql.Dataset

object PrepareGovData {

  case class Flight1(dofM: Integer, dofW: Integer, fldate: String, carrier: String, flnum: String, origin: String, dest: String, crsdephour: Long, crsdeptime: Integer, depdelay: Double, crsarrtime: Integer, arrdelay: Double, crselapsedtime: Double, dist: Double)

  val schema1 = StructType(Array(
    StructField("DAY_OF_MONTH", IntegerType, true),
    StructField("DAY_OF_WEEK", IntegerType, true),
    StructField("FL_DATE", DateType, true),
    StructField("CARRIER", StringType, true),
    StructField("FL_NUM", StringType, true),
    StructField("ORIGIN", StringType, true),
    StructField("DEST", StringType, true),
    StructField("CRS_DEP_TIME", IntegerType, true),
    StructField("DEP_DELAY_NEW", DoubleType, true),
    StructField("CRS_ARR_TIME", IntegerType, true),
    StructField("ARR_DELAY_NEW", DoubleType, true),
    StructField("CRS_ELAPSED_TIME", DoubleType, true),
    StructField("DISTANCE", DoubleType, true)
  ))

  case class Flight2(dofM: Integer, dofW: Integer, fldate: String, carrier: String, flnum: String, origin: String, dest: String, crsdephour: Integer, crsdeptime: Integer, depdelay: Double, crsarrtime: Integer, arrdelay: Double, crselapsedtime: Double, dist: Double)

  val schema2 = StructType(Array(
    StructField("dofM", IntegerType, true),
    StructField("dofW", IntegerType, true),
    StructField("fldate", StringType, true),
    StructField("carrier", StringType, true),
    StructField("flnum", StringType, true),
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

  case class FlightwId(_id: String, dofW: Integer, carrier: String, origin: String, dest: String, crsdephour: Integer, crsdeptime: Integer, depdelay: Double, crsarrtime: Integer, arrdelay: Double, crselapsedtime: Double, dist: Double) extends Serializable

  def createFlightwId(f: Flight2): FlightwId = {
    val id = f.carrier + '_' + f.fldate + '_' + f.origin + '_' + f.dest + '_' + f.flnum
    FlightwId(id, f.dofW, f.carrier, f.origin, f.dest, f.crsdephour, f.crsdeptime, f.depdelay, f.crsarrtime, f.arrdelay, f.crselapsedtime, f.dist)
  }

  def getDataGovData(spark: SparkSession, path: String): DataFrame = {
    import spark.implicits._
    val df11 = spark.read.format("com.databricks.spark.csv").schema(schema1)
      .option("header", "true").option("treatEmptyValuesAsNulls", "true").option("dateFormat", "yyyy-MM-dd").load(path)

    val df21 = df11.na.drop
    val toDouble = udf { s: Integer => (s / 100.0).round }
    val df1 = df21.withColumn("crsdephour", toDouble(df21("CRS_DEP_TIME")))

    df1.createOrReplaceTempView("flights")


    val ds1: Dataset[Flight1] = spark.sql("select DAY_OF_MONTH as dofM, DAY_OF_WEEK as dofW, CARRIER as carrier,FL_DATE as fldate , FL_NUM as flnum, ORIGIN as origin, DEST as dest,crsdephour as crsdephour, CRS_DEP_TIME  as crsdeptime, DEP_DELAY_NEW as depdelay, CRS_ARR_TIME as crsarrtime ,ARR_DELAY_NEW as arrdelay, CRS_ELAPSED_TIME as crselapsedtime , DISTANCE as dist   from flights").as[Flight1]

    ds1.createOrReplaceTempView("flights1")

    val outdf = ds1.toDF()


    return outdf
  }
}
