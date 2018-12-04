package batchProcessing

import org.apache.spark.ml._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification._
import org.apache.spark.ml.evaluation._
import org.apache.spark.ml.tuning._
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object Clasification {
  def main(args: Array[String]): Unit = {
    val spark = Utils.getSparkSeesion()
    import spark.implicits._

    val df = PrepareGovData.getDataGovData(spark, "C:\\WORK_DIR\\magistra\\MAgisterska\\Data\\usaGov\\")

    println(df.where($"depdelay" > 1).count())
    println(df.count())
    val test = df

    df.createOrReplaceTempView("flights")
    spark.catalog.cacheTable("flights")

    import org.apache.spark.mllib.stat.Statistics

    //    val depdelay = df.select("depdelay").map { row: Row => row.getAs[Double]("depdelay") }.rdd
    //    val arrdelay = df.select("arrdelay").map { row: Row => row.getAs[Double]("arrdelay") }.rdd
    //    val correlation = Statistics.corr(depdelay, arrdelay, "pearson")

    val delaybucketizer = new Bucketizer().setInputCol("depdelay")
      .setOutputCol("delayed").setSplits(Array(0.0, 40.0, Double.PositiveInfinity))
    val df4 = delaybucketizer.transform(df)

    //    df4.groupBy("delayed").count.show


    //    println(df4.count())
    //    println(df4.select($"depdelay" > 1).count())
    //    val dft = delaybucketizer.transform(test)
    //    dft.groupBy("delayed").count.show
    //    df4.createOrReplaceTempView("flight4")

    val fractions = Map(0.0 -> .29, 1.0 -> 1.0)
    val strain = df4.stat.sampleBy("delayed", fractions, 36L)
    strain.groupBy("delayed").count.show

    // categorical Column names
    val categoricalColumns = Array("carrier", "origin", "dest", "dofW")
    // String Indexers will encode string categorial columns
    // into a column numeric indices
    val stringIndexers: Array[StringIndexerModel] = categoricalColumns.map { colName =>
      new StringIndexer()
        .setInputCol(colName)
        .setOutputCol(colName + "Indexed")
        .fit(df)
    }

    //OneHotEncoders map number indices column to column of binary vectors
    val encoders: Array[OneHotEncoder] = categoricalColumns.map { colName =>
      new OneHotEncoder()
        .setInputCol(colName + "Indexed")
        .setOutputCol(colName + "Enc")
    }
    //    encoders.foreach(enc=>println(enc.inputCol+ " ",enc.outputCol))

    //bucket the dataset into delayed and not delayed flights with a label 0/1 column
    val labeler = new Bucketizer().setInputCol("depdelay")
      .setOutputCol("label")
      .setSplits(Array(0.0, 40.0, Double.PositiveInfinity))
    val featureCols = Array("carrierEnc", "destEnc", "originEnc",
      "dofWEnc", "crsdephour", "crselapsedtime", "crsarrtime", "crsdeptime", "dist")
    //put features into a feature vector column
    val assembler = new VectorAssembler()
      .setInputCols(featureCols)
      .setOutputCol("features")

    val dTree = new DecisionTreeClassifier().setLabelCol("label")
      .setFeaturesCol("features")
      .setMaxBins(7000)

    val steps: Array[PipelineStage with MLWritable] = stringIndexers ++ encoders ++ Array(labeler, assembler, dTree)


    val dummysteps = stringIndexers //++ encoders ++ Array(labeler, assembler)

    val dummyPipeline = new Pipeline().setStages(dummysteps)

    val dummysteps1 = stringIndexers ++ encoders ++ Array(labeler, assembler)

    val dummyPipeline1 = new Pipeline().setStages(dummysteps1)

    println("encoded")
    dummyPipeline.fit(df).transform(df).show(truncate =false)
    dummyPipeline1.fit(df).transform(df).show(truncate =false)
    //    val steps = stringIndexers ++ Array(labeler, assembler, dTree)
    val pipeline = new Pipeline().setStages(steps)


    // set param grid to Search through decision tree's maxDepth parameter for best model
    // Deeper trees are potentially more accurate, but are also more likely to overfit.
    val paramGrid = new ParamGridBuilder().addGrid(dTree.maxDepth, Array(4, 5, 6)).build()

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("label").setPredictionCol("prediction")
      .setMetricName("accuracy")

    // Set up 3-fold cross validation with paramGrid
    val crossval = new CrossValidator().setEstimator(pipeline)
      .setEvaluator(evaluator)
      .setEstimatorParamMaps(paramGrid).setNumFolds(3)

    val ntrain = strain.drop("delayed").drop("arrdelay")
    println(ntrain.count)
    ntrain.show

    val cvModel = crossval.fit(ntrain)
    val treeModel = cvModel.bestModel.asInstanceOf[org.apache.spark.ml.PipelineModel].stages.last.asInstanceOf[DecisionTreeClassificationModel]

    treeModel.explainParams()
    treeModel.extractParamMap
    treeModel.numNodes
    treeModel.depth
    println(treeModel.toDebugString) // note OneHotEncoding increased the number of features


    val featureImpotances = treeModel.featureImportances

    //    val fis= s"features import:\n ${featureCols.zip(featureImpotances).map(t=>s"\t${t._1}=${t._2}").mkString("\n")}\n"

    //    println(fis)
    //    val fis =featureCols.zip(featuressss).foreach(t=>println(t._1+"="+t._2))

    val predictions = cvModel.transform(test)

    val accuracy = evaluator.evaluate(predictions)


    val lp = predictions.select("label", "prediction")
    val counttotal = predictions.count()
    val label0count = lp.filter($"label" === 0.0).count()
    val pred0count = lp.filter($"prediction" === 0.0).count()
    val label1count = lp.filter($"label" === 1.0).count()
    val pred1count = lp.filter($"prediction" === 1.0).count()

    val correct = lp.filter($"label" === $"prediction").count()
    val wrong = lp.filter(not($"label" === $"prediction")).count()
    val ratioWrong = wrong.toDouble / counttotal.toDouble
    val ratioCorrect = correct.toDouble / counttotal.toDouble
    val truep = lp.filter($"prediction" === 0.0)
      .filter($"label" === $"prediction").count() / counttotal.toDouble
    val truen = lp.filter($"prediction" === 1.0)
      .filter($"label" === $"prediction").count() / counttotal.toDouble
    val falsep = lp.filter($"prediction" === 0.0)
      .filter(not($"label" === $"prediction")).count() / counttotal.toDouble
    val falsen = lp.filter($"prediction" === 1.0)
      .filter(not($"label" === $"prediction")).count() / counttotal.toDouble


    println(lp)
    println(counttotal)
    println(correct)
    println(wrong)
    cvModel.write.overwrite().save("/user/user01/data/cfModel")

    import org.apache.spark.ml.tuning.{CrossValidator, CrossValidatorModel, ParamGridBuilder}
    val sameCVModel = CrossValidatorModel.load("/user/user01/data/cfModel")

    val predictions2 = sameCVModel.transform(test)

    predictions2.show
    val accuracy2 = evaluator.evaluate(predictions2)
    evaluator.explainParams()

    test.show


  }
}
