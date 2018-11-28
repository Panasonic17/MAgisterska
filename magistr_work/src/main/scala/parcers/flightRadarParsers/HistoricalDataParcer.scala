package parcers.flightRadarParsers

import com.mongodb.{BasicDBList, BasicDBObject}
import entity.{Historical, TimeData, Traectory}

object HistoricalDataParcer {

  def timePArcer(str: String): TimeData = {
    val inputdata = str.replace("\"historical\":null", "historical\":{\"departure\":null,\"arrival\":null}")
    val data = inputdata.split(":|,|}").map(d => {
      if (d == "null") "1" else d
    })

    TimeData(data(1).toLong, data(3).toLong, data(7).toLong, data(10 - 1).toLong, data(14 - 1).toLong, data(16 - 1).toLong, data(20 - 1).toLong, data(22 - 1).toLong, data(26 - 1).replaceAll("\"", "").toLong, data(28 - 1).replaceAll("\"", "").toLong)
  }

  def TRAECTORYPArcer(str: String): Array[Traectory] = {
    def createTraectoria(str: String): Traectory = {
      val data = str.split(":|,")
      Traectory(data(1).toDouble, data(3).toDouble, data(5), data(7).toInt, data(9).toLong, if (data.length > 11) {
        data(11).toLong
      } else 0)
    }

    val data = str.substring(4, str.length - 4).split("\\},\\{")
    data.map(createTraectoria)
  }


  def getHistoricalData(str: String): Historical = {
    val splitRegex = "\"level\"|\"airline\"|\"owner\"|\"airspace\"|\"flightHistory\"|\"ems\"|\"availability\"|\"scheduled\":|\"trail\"|\"firstTimestamp\""
    val parts = str.split(splitRegex)
    val firstdata = parts(0)

    val firstREGEX = ".*id\":\"(\\w*).*callsign\":\"?(\\w*).*" r
    val firstREGEX(identificationId, identificationCallsign) = firstdata
    val secondData = parts(1)
    //    println(secondData)
    val aircraftModelCode = secondData.split("\",\"text\"")(0).split("\"code\":\"")(1)
    val FIFTHDataTMP = parts(4).split("origin|destination")
      .filter(str => str.contains("country"))
      .flatMap(s => s.split("country|city"))
      .filter(str => !str.contains("latitude"))

    val FIFTHDataTMP_1 = FIFTHDataTMP.filter(str => str.contains("code")).map(str => str.split("\"")(4))
    val FIFTHDataTMP_2 = FIFTHDataTMP.filter(str => !str.contains("code")).map(str => str.split("\"")(2))
    //
    //        println("===============")
    //    println()
    //    println()
    //
    ////        FIFTHDataTMP.foreach(println)
    //    println()
    //    println()
    //
    //        println(parts(4))
    //    println()
    ////        FIFTHDataTMP_1.foreach(println)
    ////        FIFTHDataTMP_2.foreach(println)
//    println("===============")


    var iatas = parts(4).split("iata\":\"").filter(str => str.contains("\",\"icao\"")).map(s => s.substring(0, 3))
    var countruOrigin, countruDest, cityOrigin, cityDest = ""

    //    println(FIFTHDataTMP_1.length)
    if (FIFTHDataTMP_1.length == 2) {
      countruOrigin = FIFTHDataTMP_1(0)
      countruDest = FIFTHDataTMP_1(1)
    }

    if (FIFTHDataTMP_1.length == 2) {
      cityOrigin = FIFTHDataTMP_2(0)
      cityDest = FIFTHDataTMP_2(1)
    }


    val timedata = timePArcer(parts(8))
    val traectory = TRAECTORYPArcer(parts(9))

    if (iatas.length == 2) {

      Historical(identificationId, identificationCallsign, aircraftModelCode, countruDest, countruOrigin, cityDest, cityOrigin, timedata, traectory, iatas(0), iatas(1))
    }
    else {
      Historical(identificationId, identificationCallsign, aircraftModelCode, countruDest, countruOrigin, cityDest, cityOrigin, timedata, traectory,null,null)
    }
  }

  def HistoricalDataToBasicDBObject(historical: Historical): BasicDBObject = {
    val document = new BasicDBObject
    //    Historical(id: String, callsign: String, aircraftModelCode: String, countruDest: String, countruOrigin: String, cityDest: String, cityOrigin: String, timeData: TimeData, traectory: Array[Traectory])
    document.append("id", historical.id)
    document.append("callsign", historical.callsign)
    document.append("aircraftModelCode", historical.aircraftModelCode)
    document.append("countruDest", historical.countruDest)
    document.append("countruOrigin", historical.countruOrigin)
    document.append("cityDest", historical.cityDest)
    document.append("cityOrigin", historical.cityOrigin)
    PopulateDbObjectByTimeData(historical.timeData, document)
    document.append("traectory", TraectoryArrayTOBasicDBObject(historical.traectory))
    document.append("iataOrigin", historical.iataOrigin)
    document.append("iataDest", historical.iataDest)

    document
  }

  def PopulateDbObjectByTimeData(timedata: TimeData, document: BasicDBObject): Unit = {
    document.append("scheduledDeparture", timedata.scheduledDeparture)
    document.append("scheduledArrival", timedata.scheduledArrival)
    document.append("realDeparture", timedata.realDeparture)
    document.append("realArrival", timedata.realArrival)
    document.append("estimatedETA", timedata.estimatedETA)
    document.append("estimatedUpdated", timedata.estimatedUpdated)
    document.append("otherETA", timedata.otherETA)
    document.append("otherUpdated", timedata.otherUpdated)
    document.append("historicalFlighttime", timedata.historicalFlighttime)
    document.append("historicalDelay", timedata.historicalDelay)
  }

  def TraectoryArrayTOBasicDBObject(timedata: Array[Traectory]): BasicDBList = {
    val global = new BasicDBList
    timedata.map(TraectoryTOBasicDBObject).foreach(t => global.add(t))
    global
  }

  def TraectoryTOBasicDBObject(traectory: Traectory): BasicDBObject = {
    val t = new BasicDBObject
    t.append("lat", traectory.lat)
    t.append("lng", traectory.lng)
    t.append("alt", traectory.alt)
    t.append("spd", traectory.spd)
    t.append("ts", traectory.ts)
    t.append("hd", traectory.hd)
    t
  }

}
