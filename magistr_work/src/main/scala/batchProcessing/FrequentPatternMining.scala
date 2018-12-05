package batchProcessing

import batchProcessing.PrepareGovData.schema1
import org.apache.spark.ml.fpm.FPGrowth
import org.apache.spark.ml._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification._
import org.apache.spark.ml.evaluation._
import org.apache.spark.ml.tuning._
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.sql._
import org.apache.spark.sql.functions.{concat, _}


object FrequentPatternMining {
  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()
    import spark.implicits._
    val df = spark.read.option("header", "true").option("treatEmptyValuesAsNulls", "true").csv("C:\\WORK_DIR\\magistra\\MAgisterska\\Data\\usaGov\\")

    //    df.show()
    //      [LCK, FLL]
    //
    val dataset = df.withColumn("items",
      array(concat(lit("dow"), $"DAY_OF_WEEK"),
        concat(lit("month"), $"MONTH"),
        concat($"ORIGIN", lit("->"), $"DEST"),
        concat($"DEST", lit("->"), $"ORIGIN"))
    )
    //    val dataset = df.withColumn("items", array(concat(lit("dom"),$"DAY_OF_MONTH"),concat(lit("dow"),$"DAY_OF_WEEK"),$"ORIGIN",$"DEST"))

    //    dataset.show(truncate = false)
    val fpgrowth = new FPGrowth().setItemsCol("items").setMinSupport(0).setMinConfidence(0.3)
    val model = fpgrowth.fit(dataset)

    // Display frequent itemsets.
    //    model.freqItemsets.show(truncate = false)

    // Display generated association rules.
    model.associationRules.show(truncate = false)
    model.associationRules.withColumn("like", $"consequent".getItem(0).rlike("dow|month")).filter($"like").show(1000, truncate = false)

    //
    //    // transform examines the input items against all the association rules and summarize the
    //    // consequents as prediction
    //    model.transform(dataset).show(truncate = false)

  }
}
