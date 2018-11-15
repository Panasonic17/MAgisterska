package parcers.flightRadarParsers

import entity.{FlightRadarRecord, ValuableRealtimeDataMongo}

object JsonParcers {
  def flightRadarRecordtoJson(record: FlightRadarRecord): String = {
    var str =
      s"""{"flightGlobalID":"${record.flightGlobalID}","MODE_S_CODE": "${record.MODE_S_CODE}","LATITUDE":${record.lat},"LONGITUDE":"${record.lon}","TRACK_degres": "${record.TRACK_degres}","CALIBRATED_ALTITUDE":"${record.CALIBRATED_ALTITUDE}","GROUND_SPEED":"${record.GROUND_SPEED}","_8": "${record._8}",RADAR: "${record.RADAR}","TYPE":"${record.TYPE}","REGISTRATION":"${record.REGISTRATION}","timestamp":"${record.timestamp}","from_City":"${record.from_City}" ,"to_City": "${record.to_City}","MB_PLAIN_NAME":"${record.MB_PLAIN_NAME}","_16":"${record._16}", "_17":"${record._17}","_18":"${record._18}","_19":"${record._19}","_20":"${record._20}"}"""
    return str
  }

  def valuableRealtimeDataMongoToJson(record: ValuableRealtimeDataMongo): String = {
    var str =
      s"""{"flightGlobalID":"${record.flightGlobalID}","REGISTRATION":"${record.REGISTRATION}","timestamp":"${record.timestamp}","from_City":"${record.from_City}" ,"to_City": "${record.to_City}"}"""
    return str
  }

}
