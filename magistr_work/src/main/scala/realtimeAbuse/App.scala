package realtimeAbuse

import java.io.{BufferedWriter, File, FileWriter}

import entity.{Historical, MLHistoracal}
import parcers.flightRadarParsers.{HistoricalDataParcer, MLParcer}
import recivers.GetHistoricalData
import util.Utils

import scala.io.Source
//java -cp magistr-1.0-SNAPSHOT-jar-with-dependencies.jar realtimeAbuse.App /tmp/start /tmp/outF

object App {
  def main(args: Array[String]): Unit = {

    val globalIDPAth = args(0)
    val outputFolder = args(1)

    val lines = Source.fromFile(globalIDPAth).getLines.toArray
    var globalStartID = lines(0).toInt

    println(globalStartID)

    var filewriter = getBuferedWriter(outputFolder, globalStartID.toString)
    val keyWriter = getBuferedWriter(outputFolder, "KEYS")

    while (true) {
      globalStartID += 10
      Thread.sleep(5*1000)
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

        if (globalStartID % 1000 == 0) {
          keyWriter.write(globalStartID)
          keyWriter.flush()
          filewriter = getBuferedWriter(outputFolder, globalStartID.toString)
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

/*


try  { //            globalID = input.findOne().get("flightGlobalID").toString();




//            System.out.println(historicalData);
//            System.out.println(mlHistoracal);
val outputObject: BasicDBObject = MLParcer.historacalMLtoBasicDBObject(mlHistoracal)
//            outputObject.append("timestamp",System.currentTimeMillis());
//            System.out.println(outputObject);
//save data f8cd6
System.out.println(mlHistoracal)
System.out.println("OK")
output.insert(outputObject)
// remove flight from tmp storage
} catch {
case er: MatchError =>

//            System.out.println("invalid data" + data);
case e: Exception =>
val document: BasicDBObject = new BasicDBObject
//            System.out.println("send to REJECTED ");
document.put("flightGlobalID", globalID)
document.put("time", System.currentTimeMillis)
rejected.insert(document)
System.out.println(e.fillInStackTrace)
} finally {
val document: BasicDBObject = new BasicDBObject
document.put("flightGlobalID", globalID)
input.remove(document)
}
//        System.out.println(historicalData);
//        System.out.println("class" + historicalData);
}
*/