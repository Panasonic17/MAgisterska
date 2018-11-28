package batchProcessing

import org.apache.spark.sql._

import scala.reflect.internal.util.TableDef
object Regression {

  def getValidHistoricalReggersionRecords(df: DataFrame): DataFrame = {
    df.select("iataOrigin", "iataDest", "aircraftModelCode", "traectory", "scheduledDeparture", "scheduledArrival", "realDeparture", "realArrival")
      .where(new Column("scheduledDeparture")>10)
      .where(new Column("scheduledArrival")>10)
      .where(new Column("realDeparture")>10)
      .where(new Column("realArrival")>10)

      .na.drop()
  }

}
