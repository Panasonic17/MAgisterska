package recivers

import scalaj.http.{Http, HttpResponse}

class GetSimplePlainsArray {
  def getPlainsArray(): Array[String] = {
    val url = "https://data-live.flightradar24.com/zones/fcgi/feed.js?bounds=69.65,-55.06,-101.88,81.82&faa=1&mlat=1&flarm=1&adsb=1&gnd=1&air=1&vehicles=1&estimated=1&maxage=14400&gliders=1&stats=1"
    //    //   val url_1=/
    val data: HttpResponse[String] = Http(url).asString
    //
//println(data)
    val arr = data.body.split("\n")
    return arr
  }
}
