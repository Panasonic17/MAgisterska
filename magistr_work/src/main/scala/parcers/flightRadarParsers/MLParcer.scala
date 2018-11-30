package parcers.flightRadarParsers

import com.mongodb.{BasicDBList, BasicDBObject}
import entity.{Historical, MLHistoracal, MLTraectory, Traectory}
import settings.Constants.TRAECTORY_SLICING_COUNT
import util.Utils._

object MLParcer {
  def toMLHistorical(hisitracal: Historical): MLHistoracal = {

    //      case class MLHistoracal(aircraftModelCode: String, iataOrigin: String, iataDest: String, traectory: Array[MLTraectory],scheduledDeparture: Int, scheduledArrival: Int, realDeparture: Int, realArrival: Int)
    val traectory1 = getSlicedValues(hisitracal.traectory, TRAECTORY_SLICING_COUNT).map(traectory => MLTraectory(traectory.lat, traectory.lng, traectory.spd, traectory.ts.toInt)).toArray
    //    case class MLTraectory(lat: Double, lng: Double, spd: Int, ts: Int)
    MLHistoracal(hisitracal.aircraftModelCode, hisitracal.iataOrigin, hisitracal.iataDest, traectory1, hisitracal.timeData.scheduledDeparture.toInt, hisitracal.timeData.scheduledArrival.toInt, hisitracal.timeData.realDeparture.toInt, hisitracal.timeData.realArrival.toInt)
  }

  def historacalMLtoBasicDBObject(historical: MLHistoracal): BasicDBObject = {
    val document = new BasicDBObject

    //    Historical(id: String, callsign: String, aircraftModelCode: String, countruDest: String, countruOrigin: String, cityDest: String, cityOrigin: String, timeData: TimeData, traectory: Array[Traectory])
    document.append("aircraftModelCode", historical.aircraftModelCode)
    document.append("iataOrigin", historical.iataOrigin)
    document.append("iataDest", historical.iataDest)
    document.append("traectory", mLTraectoryArrayTOBasicDBObject(historical.traectory))
    document.append("scheduledArrival", historical.scheduledArrival)
    document.append("scheduledDeparture", historical.scheduledDeparture)
    document.append("realDeparture", historical.realDeparture)
    document.append("realArrival", historical.realArrival)

    document
  }

  def mLTraectoryArrayTOBasicDBObject(timedata: Array[MLTraectory]): BasicDBList = {
    val global = new BasicDBList
    timedata.map(traectoryTOBasicDBObject).foreach(t => global.add(t))
    global
  }

  def traectoryTOBasicDBObject(traectory: MLTraectory): BasicDBObject = {
    val t = new BasicDBObject
    t.append("lat", traectory.lat)
    t.append("lng", traectory.lng)
    t.append("spd", traectory.spd)
    t.append("ts", traectory.ts)
    t
  }

  def goggleCloudStrToMLHistoracal(string: String): MLHistoracal = {
    val data = string.replace("Lentity.MLTraectory;@", "bla bla bla") split ("MLHistoracal|MLTraectory")
    val rawMLHistoracal: Array[String] = data(1).replace("(", "").replace(")", "").split(",")
    val arrTreactory = data.tail.tail.map(getMLTraectoryFromStrGooleCloud)

    MLHistoracal(rawMLHistoracal(0), rawMLHistoracal(1), rawMLHistoracal(2), arrTreactory, rawMLHistoracal(4).toInt, rawMLHistoracal(5).toInt, rawMLHistoracal(6).toInt, rawMLHistoracal(7).trim.toInt)
  }

  def getMLTraectoryFromStrGooleCloud(string: String): MLTraectory = {
    val data = string.replace("(", "").replace(")", "").split(",")

    MLTraectory(data(0).toDouble, data(1).toDouble, data(2).toInt, data(3).toInt)
  }
}