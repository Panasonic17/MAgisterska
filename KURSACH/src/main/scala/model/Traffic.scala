package model

import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, _}
import spark.BroadcastGenerator

case class Traffic(
                    origin: String,
                    flight: String,
                    course: Int,
                    aircraft: String,
                    callsign: String,
                    registration: String,
                    lat: Double,
                    speed: Int,
                    altitude: String,
                    destination: String,
                    lon: Double,
                    time: Long,
                    var originCity: String,
                    var destinationCity: String,
                    var originCountry: String,
                    var destinationCountry: String) {
  def this(origin: String, flight: String, course: Int, aircraft: String, callsign: String, registration: String, lat: Double, speed: Int, altitude: String, destination: String, lon: Double, time: Long) = this(origin: String, flight: String, course: Int, aircraft: String, callsign: String, registration: String, lat: Double, speed: Int, altitude: String, destination: String, lon: Double, time: Long, "", "", "", "")

  def updateCityAndCountry(cities: Map[String, String], countries: Map[String, String]): Traffic = {
    originCity = cities.getOrElse(origin, "")
    destinationCity = cities.getOrElse(destination, "")
    originCountry = countries.getOrElse(origin, "")
    destinationCountry = countries.getOrElse(destination, "")
    this
  }
  def fieldIndex(field: String): Int = {
    field match {
      case "origin" => 1
      case "flight" => 2
      case "course" => 3
      case "aircraft" => 4
      case "callsign" => 5
      case "registration" => 6
      case "lat" => 7
      case "speed" => 8
      case "altitude" => 9
      case "destination" => 10
      case "lon" => 11
      case "time" => 12
      case "originCity" => 13
      case "destinationCity" => 14
      case "originCountry" => 15
      case "destinationCountry" => 16
    }
  }
  def getVal(field: String): String = {
    field match {
      case "origin" => this.origin
      case "flight" => this.flight
      case "course" => this.course.toString
      case "aircraft" => this.aircraft
      case "callsign" => this.callsign
      case "registration" => this.registration
      case "lat" => this.lat.toString
      case "speed" => this.speed.toString
      case "altitude" => this.altitude
      case "destination" => this.destination
      case "lon" => this.lon.toString
      case "time" => this.time.toString
      case "originCity" => this.originCity
      case "destinationCity" => this.destinationCity
      case "originCountry" => this.originCountry
      case "destinationCountry" => this.destinationCountry
    }
  }
}

object Traffic {
  def parceTraffic(json: String): Traffic = {
    val parsed = parse(json)
    implicit val formats = DefaultFormats
    val traffic = parsed.extract[Traffic]
    Traffic(traffic.origin, traffic.flight, traffic.course, traffic.aircraft, traffic.callsign, traffic.registration, traffic.lat, traffic.speed, traffic.altitude, traffic.destination, traffic.lon, traffic.time * 1000L, "", "", "", "")
  }

  def main(args: Array[String]): Unit = {
    val broadcstTest = new BroadcastGenerator
    val m1 = broadcstTest.getIataCityName("/home/sawa/programing/Kursach/Data/airports.dat")
    val m2 = broadcstTest.getIataCountryName("/home/sawa/programing/Kursach/Data/airports.dat")
    val test = "{\n  \"origin\": \"ARN\",\n  \"flight\": \"SK637\",\n  \"course\": 216,\n  \"aircraft\": \"A20N\",\n  \"callsign\": \"SAS61Z\",\n  \"registration\": \"SE-ROB\",\n  \"lat\": 57.1295,\n  \"speed\": 437,\n  \"altitude\": 39025,\n  \"destination\": \"FRA\",\n  \"lon\": 14.4414,\n  \"time\": 1524748817\n}"
    val traffic = parceTraffic(test).updateCityAndCountry(m1, m2)
    print(traffic)
  }
}


