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
    document.append("traectory", MLTraectoryArrayTOBasicDBObject(historical.traectory))
    document.append("scheduledArrival", historical.scheduledArrival)
    document.append("scheduledDeparture", historical.scheduledDeparture)
    document.append("realDeparture", historical.realDeparture)
    document.append("realArrival", historical.realArrival)

    document
  }

  def MLTraectoryArrayTOBasicDBObject(timedata: Array[MLTraectory]): BasicDBList = {
    val global = new BasicDBList
    timedata.map(TraectoryTOBasicDBObject).foreach(t => global.add(t))
    global
  }

  def TraectoryTOBasicDBObject(traectory: MLTraectory): BasicDBObject = {
    val t = new BasicDBObject
    t.append("lat", traectory.lat)
    t.append("lng", traectory.lng)
    t.append("spd", traectory.spd)
    t.append("ts", traectory.ts)
    t
  }
}