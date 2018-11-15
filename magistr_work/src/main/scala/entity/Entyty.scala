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

case class Number1(
                    default: String,
                    alternative: String
                  )

case class Identification_1(
                             id: String,
                             row: Double,
                             number: Number1,
                             callsign: String
                           )

case class Status_1(
                     text: String,
                     color: String,
                     `type`: String
                   )

case class EventTime(
                      utc: Double,
                      local: Double
                    )

case class Generic(
                    status: Status_1,
                    eventTime: EventTime
                  )

case class Status(
                   live: Boolean,
                   text: String,
                   icon: String,
                   estimated: String,
                   ambiguous: Boolean,
                   generic: Generic
                 )

case class Model(
                  code: String,
                  text: String
                )

case class Thumbnails(
                       src: String,
                       link: String,
                       copyright: String,
                       source: String
                     )

case class Images(
                   thumbnails: List[Thumbnails],
                   medium: List[Thumbnails],
                   large: List[Thumbnails]
                 )

case class Aircraft1(
                      model: Model,
                      registration: String,
                      hex: String,
                      age: String,
                      msn: String,
                      images: Images
                    )

case class Code(
                 iata: String,
                 icao: String
               )

case class Airline(
                    name: String,
                    short: String,
                    code: Code,
                    url: String
                  )

case class Owner(
                  name: String,
                  code: Code,
                  url: String
                )

case class Country(
                    name: String,
                    code: String
                  )

case class Region(
                   city: String
                 )

case class Position(
                     latitude: Double,
                     longitude: Double,
                     altitude: Double,
                     country: Country,
                     region: Region
                   )

case class Timezone(
                     name: String,
                     offset: Double,
                     offsetHours: String,
                     abbr: String,
                     abbrName: String,
                     isDst: Boolean
                   )

case class Info(
                 terminal: String,
                 baggage: String,
                 gate: String
               )

case class Origin1(
                    name: String,
                    code: Code,
                    position: Position,
                    timezone: Timezone,
                    visible: Boolean,
                    website: String,
                    info: Info
                  )

case class Airport1(
                     origin: Origin1,
                     destination: Origin1,
                     real: String
                   )

case class Number(
                   default: String
                 )

case class Identification(
                           id: String,
                           number: Number
                         )

case class Origin(
                   name: String,
                   code: Code,
                   position: Position,
                   timezone: Timezone,
                   visible: Boolean,
                   website: String
                 )

case class Airport(
                    origin: Origin,
                    destination: Origin
                  )

case class Real(
                 departure: Double
               )

case class Time1(
                  real: Real
                )

case class Aircraft(
                     identification: Identification,
                     airport: Airport,
                     time: Time1
                   )

case class FlightHistory(
                          aircraft: List[Aircraft]
                        )

case class Scheduled(
                      departure: Double,
                      arrival: Double
                    )

case class Other(
                  eta: Double,
                  updated: Double
                )

case class Historical(
                       flighttime: String,
                       delay: String
                     )

case class Time(
                 scheduled: Scheduled,
                 real: Scheduled,
                 estimated: Scheduled,
                 other: Other,
                 historical: Historical
               )

case class Trail(
                  lat: Double,
                  lng: Double,
                  alt: Double,
                  spd: Double,
                  ts: Double,
                  hd: Double
                )

case class FullHistoracalData(
                           identification: Identification_1,
                           status: Status,
                           level: String,
                           aircraft: Aircraft1,
                           airline: Airline,
                           owner: Owner,
                           airspace: String,
                           airport: Airport1,
                           flightHistory: FlightHistory,
                           ems: String,
                           availability: List[String],
                           time: Time,
                           trail: List[Trail],
                           firstTimestamp: Double,
                           s: String
                         )

/*
{
  "identification": {
    "id": "1e839b78",
    "row": 4834675697,
    "number": {
    "default": "UA5606",
    "alternative": "OO5606"
  },
    "callsign": "SKW5606"
  },
  "status": {
    "live": false,
    "text": "Landed 15:44",
    "icon": "green",
    "estimated": null,
    "ambiguous": false,
    "generic": {
    "status": {
    "text": "landed",
    "color": "green",
    "type": "arrival"
  },
    "eventTime": {
    "utc": 1541893492,
    "local": 1541864692
  }
  }
  },
  "level": "limited",
  "aircraft": {
    "model": {
    "code": "E75L",
    "text": "Embraer ERJ-175LR"
  },
    "registration": "N206SY",
    "hex": "a1a9f4",
    "age": null,
    "msn": null,
    "images": {
    "thumbnails": [{
    "src": "https:\/\/cdn.jetphotos.com\/200\/5\/67606_1491091947_tb.jpg?v=0",
    "link": "https:\/\/www.jetphotos.com\/photo\/8551906",
    "copyright": "Jeremy D. Dando",
    "source": "Jetphotos"
  }],
    "medium": [{
    "src": "https:\/\/cdn.jetphotos.com\/400\/5\/67606_1491091947.jpg?v=0",
    "link": "https:\/\/www.jetphotos.com\/photo\/8551906",
    "copyright": "Jeremy D. Dando",
    "source": "Jetphotos"
  }],
    "large": [{
    "src": "https:\/\/cdn.jetphotos.com\/640cb\/5\/67606_1491091947.jpg?v=0",
    "link": "https:\/\/www.jetphotos.com\/photo\/8551906",
    "copyright": "Jeremy D. Dando",
    "source": "Jetphotos"
  }]
  }
  },
  "airline": {
    "name": "United Express",
    "short": "United Airlines",
    "code": {
    "iata": "UA",
    "icao": "UAL"
  },
    "url": "united-airlines-ual"
  },
  "owner": {
    "name": "SkyWest Airlines",
    "code": {
    "iata": "OO",
    "icao": "SKW"
  },
    "url": "skywest-airlines-skw"
  },
  "airspace": null,
  "airport": {
    "origin": {
    "name": "Spokane International Airport",
    "code": {
    "iata": "GEG",
    "icao": "KGEG"
  },
    "position": {
    "latitude": 47.619862,
    "longitude": -117.532997,
    "altitude": 2385,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "Spokane"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": null,
    "info": {
    "terminal": null,
    "baggage": null,
    "gate": "B5"
  }
  },
    "destination": {
    "name": "San Francisco International Airport",
    "code": {
    "iata": "SFO",
    "icao": "KSFO"
  },
    "position": {
    "latitude": 37.618969,
    "longitude": -122.374001,
    "altitude": 13,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "San Francisco"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": "http:\/\/www.flysfo.com\/",
    "info": {
    "terminal": "3",
    "baggage": "4",
    "gate": "71A"
  }
  },
    "real": null
  },
  "flightHistory": {
    "aircraft": [{
    "identification": {
    "id": "1e834206",
    "number": {
    "default": "UA5243"
  }
  },
    "airport": {
    "origin": {
    "name": "San Francisco International Airport",
    "code": {
    "iata": "SFO",
    "icao": "KSFO"
  },
    "position": {
    "latitude": 37.618969,
    "longitude": -122.374001,
    "altitude": 13,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "San Francisco"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": "http:\/\/www.flysfo.com\/"
  },
    "destination": {
    "name": "Spokane International Airport",
    "code": {
    "iata": "GEG",
    "icao": "KGEG"
  },
    "position": {
    "latitude": 47.619862,
    "longitude": -117.532997,
    "altitude": 2385,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "Spokane"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": null
  }
  },
    "time": {
    "real": {
    "departure": 1541877677
  }
  }
  }, {
    "identification": {
    "id": "1e829807",
    "number": {
    "default": "UA5550"
  }
  },
    "airport": {
    "origin": {
    "name": "Bentonville Northwest Arkansas Regional Airport",
    "code": {
    "iata": "XNA",
    "icao": "KXNA"
  },
    "position": {
    "latitude": 36.28186,
    "longitude": -94.306801,
    "altitude": 1288,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "Bentonville"
  }
  },
    "timezone": {
    "name": "America\/Chicago",
    "offset": -21600,
    "offsetHours": "-6:00",
    "abbr": "CST",
    "abbrName": "Central Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": null
  },
    "destination": {
    "name": "San Francisco International Airport",
    "code": {
    "iata": "SFO",
    "icao": "KSFO"
  },
    "position": {
    "latitude": 37.618969,
    "longitude": -122.374001,
    "altitude": 13,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "San Francisco"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": "http:\/\/www.flysfo.com\/"
  }
  },
    "time": {
    "real": {
    "departure": 1541857657
  }
  }
  }, {
    "identification": {
    "id": "1e81a615",
    "number": {
    "default": "UA5289"
  }
  },
    "airport": {
    "origin": {
    "name": "San Francisco International Airport",
    "code": {
    "iata": "SFO",
    "icao": "KSFO"
  },
    "position": {
    "latitude": 37.618969,
    "longitude": -122.374001,
    "altitude": 13,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "San Francisco"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": "http:\/\/www.flysfo.com\/"
  },
    "destination": {
    "name": "Bentonville Northwest Arkansas Regional Airport",
    "code": {
    "iata": "XNA",
    "icao": "KXNA"
  },
    "position": {
    "latitude": 36.28186,
    "longitude": -94.306801,
    "altitude": 1288,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "Bentonville"
  }
  },
    "timezone": {
    "name": "America\/Chicago",
    "offset": -21600,
    "offsetHours": "-6:00",
    "abbr": "CST",
    "abbrName": "Central Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": null
  }
  },
    "time": {
    "real": {
    "departure": 1541819642
  }
  }
  }, {
    "identification": {
    "id": "1e814f83",
    "number": {
    "default": "UA5984"
  }
  },
    "airport": {
    "origin": {
    "name": "Monterey Regional Airport",
    "code": {
    "iata": "MRY",
    "icao": "KMRY"
  },
    "position": {
    "latitude": 36.587002,
    "longitude": -121.842003,
    "altitude": 257,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "Monterey"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": null
  },
    "destination": {
    "name": "San Francisco International Airport",
    "code": {
    "iata": "SFO",
    "icao": "KSFO"
  },
    "position": {
    "latitude": 37.618969,
    "longitude": -122.374001,
    "altitude": 13,
    "country": {
    "name": "United States",
    "code": "USA"
  },
    "region": {
    "city": "San Francisco"
  }
  },
    "timezone": {
    "name": "America\/Los_Angeles",
    "offset": -28800,
    "offsetHours": "-8:00",
    "abbr": "PST",
    "abbrName": "Pacific Standard Time",
    "isDst": false
  },
    "visible": true,
    "website": "http:\/\/www.flysfo.com\/"
  }
  },
    "time": {
    "real": {
    "departure": 1541808196
  }
  }
  }]
  },
  "ems": null,
  "availability": ["AGE", "MSN"],
  "time": {
    "scheduled": {
    "departure": 1541886600,
    "arrival": 1541895120
  },
    "real": {
    "departure": 1541887331,
    "arrival": 1541893492
  },
    "estimated": {
    "departure": null,
    "arrival": null
  },
    "other": {
    "eta": 1541893492,
    "updated": 1541893530
  },
    "historical": {
    "flighttime": "6538",
    "delay": "-611"
  }
  },
  "trail": [{
  "lat": 37.619072,
  "lng": -122.386246,
  "alt": 0,
  "spd": 2,
  "ts": 1541893664,
  "hd": 225
}, {
  "lat": 37.61927,
  "lng": -122.386009,
  "alt": 0,
  "spd": 19,
  "ts": 1541893652,
  "hd": 225
}, {
  "lat": 37.619469,
  "lng": -122.385788,
  "alt": 0,
  "spd": 13,
  "ts": 1541893642,
  "hd": 208
}, {
  "lat": 37.621994,
  "lng": -122.386116,
  "alt": 0,
  "spd": 33,
  "ts": 1541893565,
  "hd": 239
}, {
  "lat": 37.622124,
  "lng": -122.385696,
  "alt": 0,
  "spd": 18,
  "ts": 1541893559,
  "hd": 261
}, {
  "lat": 37.622177,
  "lng": -122.384697,
  "alt": 0,
  "spd": 25,
  "ts": 1541893552,
  "hd": 264
}, {
  "lat": 37.622154,
  "lng": -122.383514,
  "alt": 0,
  "spd": 34,
  "ts": 1541893545,
  "hd": 284
}, {
  "lat": 37.621502,
  "lng": -122.381721,
  "alt": 0,
  "spd": 48,
  "ts": 1541893535,
  "hd": 298
}, {
  "lat": 37.620712,
  "lng": -122.379868,
  "alt": 0,
  "spd": 65,
  "ts": 1541893528,
  "hd": 298
}, {
  "lat": 37.618652,
  "lng": -122.37484,
  "alt": 0,
  "spd": 102,
  "ts": 1541893516,
  "hd": 297
}, {
  "lat": 37.615791,
  "lng": -122.368111,
  "alt": 0,
  "spd": 128,
  "ts": 1541893505,
  "hd": 297
},
{"lat":47.624229,"lng":-117.534042,"alt":0,"spd":14,"ts":1541886989,"hd":180}],"firstTimestamp":1541886989,"s":"nacHajP1vaqyFcMFqX8e-AmxfTp2-XksrP5Q8PATGbBenyTdgGKVkaXvIQEOqm6j"}*/