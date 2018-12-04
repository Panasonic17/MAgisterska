package test

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.{udf, _}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}


object getDATAFRAME {


  val schema = StructType(Array(StructField("DAY_OF_MONTH", IntegerType, true),
    StructField("DAY_OF_WEEK", IntegerType, true),
    StructField("FL_DATE", DateType, true),
    StructField("OP_UNIQUE_CARRIER", StringType, true),
    StructField("OP_CARRIER_FL_NUM", StringType, true),
    StructField("ORIGIN", StringType, true),
    StructField("DEST", StringType, true),
    StructField("CRS_DEP_TIME", IntegerType, true),
    StructField("DEP_DELAY_NEW", DoubleType, true),
    StructField("CRS_ARR_TIME", IntegerType, true),
    StructField("ARR_DELAY_NEW", DoubleType, true),
    StructField("CRS_ELAPSED_TIME", DoubleType, true),
    StructField("DISTANCE", DoubleType, true)
  ))

  def getDataframe(spark: SparkSession, path: String): DataFrame = {

    val df1 = spark.read.format("com.databricks.spark.csv").schema(schema)
      .option("header", "true")
      .option("treatEmptyValuesAsNulls", "true").option("dateFormat", "yyyy-MM-dd").load(path)
    val df2 = df1.na.drop
    val toDouble: UserDefinedFunction = udf { s: Integer => (s / 100.0).round }
    val df = df2.withColumn("crsdephour", toDouble(df2("CRS_DEP_TIME")))
    import spark.implicits._
    df.createOrReplaceTempView("flights")
    val dfd = spark.sql("select DAY_OF_MONTH as dofM, DAY_OF_WEEK as dofW, OP_UNIQUE_CARRIER as carrier,FL_DATE as fldate , OP_CARRIER_FL_NUM as flnum, ORIGIN as origin, DEST as dest,crsdephour as crsdephour, CRS_DEP_TIME  as crsdeptime, DEP_DELAY_NEW as depdelay, CRS_ARR_TIME as crsarrtime ,ARR_DELAY_NEW as arrdelay, CRS_ELAPSED_TIME as crselapsedtime , DISTANCE as dist   from flights")


    dfd.createOrReplaceTempView("flights1")
    val df21 = dfd.withColumn("_id", concat($"carrier", $"fldate", $"origin", $"dest", $"flnum"))

    df21
  }
}
