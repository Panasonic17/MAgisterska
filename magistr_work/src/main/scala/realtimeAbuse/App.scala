package realtimeAbuse

import java.io.{BufferedWriter, File, FileWriter}

import entity.{Historical, MLHistoracal}
import parcers.flightRadarParsers.{HistoricalDataParcer, MLParcer}
import recivers.GetHistoricalData
import util.Utils

import scala.io.Source
//nohup  java -cp magistr-1.0-SNAPSHOT-jar-with-dependencies.jar realtimeAbuse.App start files 5
//514520126
object App {
  def main(args: Array[String]): Unit = {

    val globalIDPAth = args(0)
    val outputFolder = args(1)
    val step = args(2).toInt
    val lines = Source.fromFile(globalIDPAth).getLines.toArray
    var globalStartID = lines(0).toInt

    println(globalStartID)

    var filewriter = getBuferedWriter(outputFolder, globalStartID.toString)
    val keyWriter = getBuferedWriter(outputFolder, "KEYS")
    var iteration = 0
    while (true) {
      iteration += 1
      globalStartID += step
      Thread.sleep(5 * 1000)
      try {
        val data: String = GetHistoricalData.getHistoricalFlightDataByFlightID(Integer.toHexString(globalStartID))
        println(data)
        var historicalData: Historical = null
        historicalData = HistoricalDataParcer.getHistoricalData(data)
        if (Utils.isFullHistoracal(historicalData)) {
          println("FULL")
          val mlHistoracal: MLHistoracal = MLParcer.toMLHistorical(historicalData)

          filewriter.write(mlHistoracal.toString)
          filewriter.write("  ")
          mlHistoracal.traectory.foreach(tr => {
            filewriter.write(tr.toString)
          })

          filewriter.write("\n")
          filewriter.flush()
        }

        if (iteration > 1000) {
          keyWriter.write(globalStartID)
          keyWriter.flush()
          filewriter = getBuferedWriter(outputFolder, globalStartID.toString)
          iteration = 0
        }

      }
      catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }


  def getBuferedWriter(outputPath: String, globalID: String): BufferedWriter = {
    var file = new File(outputPath + "/" + globalID)
    file.createNewFile()
    var bw: BufferedWriter = new BufferedWriter(new FileWriter(file))
    bw
  }
}

