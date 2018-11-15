package parcers.flightRadarParsers

import entity.{FlightRadarRecord, ValuableRealtimeDataElasticsearch, ValuableRealtimeDataMongo}

object Flightradar24Parcer {

  def parsePlainData(row: String): FlightRadarRecord = {
    val regex = ",\"(\\w*)\":\\[\"(\\w*)\",([-.\\d]*),([-.\\d]*),(\\d*),(\\d*),(\\d*),\"(\\d*)\",\"([\\w-]*)\",\"(\\w*)\",\"([\\w-]*)\".(\\d*),\"(\\w*)\",\"(\\w*)\",\"(\\w*)\",(\\d*),([-\\d]*),\"(\\w*)\",(\\d*),\"(\\w*)\"]".r
    row match {
      case regex(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20) => FlightRadarRecord(_1, _2, _3.toFloat, _4.toFloat, _5, _6, _7, _8, _9, _10, _11, _12 + "000", _13, _14, _15, _16, _17, _18, _19, _20, _3 + "," + _4)
      case _ =>
        println("failed data " + row); null
    }
  }



  def parceValuableRealtimeDataMongo(row: String): ValuableRealtimeDataMongo = {
    val regex = ",\"(\\w*)\":\\[\"(\\w*)\",([-.\\d]*),([-.\\d]*),(\\d*),(\\d*),(\\d*),\"(\\d*)\",\"([\\w-]*)\",\"(\\w*)\",\"([\\w-]*)\".(\\d*),\"(\\w*)\",\"(\\w*)\",\"(\\w*)\",(\\d*),([-\\d]*),\"(\\w*)\",(\\d*),\"(\\w*)\"]".r
    row match {
      case regex(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20) => ValuableRealtimeDataMongo(_1, _11, _12 + "1000", _13, _14)
      case _ =>
        println("failed data " + row); null
    }
  }

  def parceValuableRealtimeDataElasticsearch(row: String): ValuableRealtimeDataElasticsearch = {
    val regex = ",\"(\\w*)\":\\[\"(\\w*)\",([-.\\d]*),([-.\\d]*),(\\d*),(\\d*),(\\d*),\"(\\d*)\",\"([\\w-]*)\",\"(\\w*)\",\"([\\w-]*)\".(\\d*),\"(\\w*)\",\"(\\w*)\",\"(\\w*)\",(\\d*),([-\\d]*),\"(\\w*)\",(\\d*),\"(\\w*)\"]".r
    row match {
      case regex(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17, _18, _19, _20) => ValuableRealtimeDataElasticsearch(_1, _12 + "1000", _3.toFloat + "," + _4.toFloat)
      case _ =>
        println("failed data " + row); null
    }
  }
}
