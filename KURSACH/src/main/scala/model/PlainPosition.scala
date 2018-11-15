package model

case class PlainPosition(flight: String, plainLon: Double, plainLat: Double, destination: String, time: Long) {
  def this(traffic: Traffic) = this(traffic.flight, traffic.lon, traffic.lat, traffic.destination, traffic.time)

  //lon 180 -> 180
  //  lat -90 ->  90
  def getCoorditationsKey: Integer = {
    var retKey = 0
    val lonKey = ((plainLon + 180) / 10).toInt // 0 - 36
    val latKey = ((plainLat + 90) / 10).toInt  // 0 -18
    retKey= lonKey*100+latKey
    retKey
  }
}

object PlainPosition {

}