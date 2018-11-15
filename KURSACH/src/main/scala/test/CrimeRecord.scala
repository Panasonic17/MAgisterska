package test

case class CrimeRecord(
                        id: Int,
                        caseNumber: String,
                        date: String,

                        block: String,
                        iucr: String,
                        primaryType: String,

                        description: String,
                        locationDescription: String,
                        arrest: Boolean,

                        domestic: Boolean,
                        beat: Int,
                        district: Int,

                        ward: Int,
                        communityArea: Int,
                        fbiCode: String,

                        xCoordinate: Int,
                        yCoordinate: Int,
                        year: Int,

                        updatedOn: String, //LocalDateTime,
                        latitude: Double,
                        longitude: Double,

                        location: String
                      ){
  def getVal(field: String): String = {
    field match {
      case "id" => this.id.toString
      case "caseNumber" => this.caseNumber
      case "date" => this.date
      case "block" => this.block
      case "iucr" => this.iucr
      case "primaryType" => this.primaryType
      case "description" => this.description
      case "locationDescription" => this.locationDescription
      case "arrest" => this.arrest.toString
      case "domestic" => this.domestic.toString
      case "beat" => this.beat.toString
      case "district" => this.district.toString
      case "ward" => this.ward.toString
      case "communityArea" => this.communityArea.toString
      case "fbiCode" => this.fbiCode
      case "xCoordinate" => this.xCoordinate.toString
      case "yCoordinate" => this.yCoordinate.toString
      case "year" => this.year.toString
      case "updatedOn" => this.updatedOn
      case "latitude" => this.latitude.toString
      case "longitude" => this.longitude.toString
      case "location" => this.location
    }
  }
  def fieldIndex(field: String): Int = {
    field match {
      case "id" => 1
      case "caseNumber" => 2
      case "date" => 3
      case "block" => 4
      case "iucr" => 5
      case "primaryType" => 6
      case "description" => 7
      case "locationDescription" => 8
      case "arrest" => 9
      case "domestic" => 10
      case "beat" => 11
      case "district" => 12
      case "ward" => 13
      case "communityArea" => 14
      case "fbiCode" => 15
      case "xCoordinate" => 16
      case "yCoordinate" => 17
      case "year" => 18
      case "updatedOn" => 19
      case "latitude" => 20
      case "longitude" => 21
      case "location" => 22
    }
  }
}
