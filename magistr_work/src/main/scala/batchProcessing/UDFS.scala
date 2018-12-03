package batchProcessing

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

object UDFS {

  val getDistance: (Double, Double, Double, Double) => Double = Utils.getDistance(_, _, _, _)

  val udfGetDistance: UserDefinedFunction = udf(getDistance)

  val getTimeWithAcseleration: (Double, Double, Double) => Double = Utils.getTimeFromAceleration(_, _, _)

  val getTimeWithAcselerationUDF: UserDefinedFunction = udf(getTimeWithAcseleration)

}
