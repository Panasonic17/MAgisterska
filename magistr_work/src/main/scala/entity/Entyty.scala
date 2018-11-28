package entity

case class ValuableRealtimeDataElasticsearch(
                                              val flightGlobalID: String,
                                              var timestamp: String,
                                              location: String
                                            )

case class ValuableRealtimeDataMongo(flightGlobalID: String,
                                     REGISTRATION: String,
                                     timestamp: String,
                                     from_City: String,
                                     to_City: String
                                    )

case class FlightRadarRecord(
                              flightGlobalID: String,
                              MODE_S_CODE: String,
                              lat: Float,
                              lon: Float,
                              TRACK_degres: String,
                              CALIBRATED_ALTITUDE: String,
                              GROUND_SPEED: String,
                              _8: String,
                              RADAR: String,
                              TYPE: String,
                              REGISTRATION: String,
                              timestamp: String,
                              from_City: String,
                              to_City: String,
                              MB_PLAIN_NAME: String,
                              _16: String,
                              _17: String,
                              _18: String,
                              _19: String,
                              _20: String,
                              location: String
                            )

case class TimeData(scheduledDeparture: Long, scheduledArrival: Long, realDeparture: Long, realArrival: Long, estimatedETA: Long, estimatedUpdated: Long, otherETA: Long, otherUpdated: Long, historicalFlighttime: Long, historicalDelay: Long)

case class Traectory(lat: Double, lng: Double, alt: String, spd: Int, ts: Long, hd: Long)

case class Historical(id: String, callsign: String, aircraftModelCode: String, countruDest: String, countruOrigin: String, cityDest: String, cityOrigin: String, timeData: TimeData, traectory: Array[Traectory], iataOrigin: String, iataDest: String)

case class MLHistoracal(aircraftModelCode: String, iataOrigin: String, iataDest: String, traectory: Array[MLTraectory],scheduledDeparture: Int, scheduledArrival: Int, realDeparture: Int, realArrival: Int)

case class MLTraectory(lat: Double, lng: Double, spd: Int, ts: Int)