package settings

object Constants {
  var MONGO_DATABACE = "MagistraTest"
  var MONGO_REALTIME_DATA_SCHEMA = "plain_dataTest"
  var MONGO_HISTORICAL_DATA_SCHEMA = "flight_info"
  var MONGO_HISTORICAL_REJECTED_ID = "rejected_id"
  var MONGO_HISTORICAL_KEYS = "keys"

  var ELSATICSERACH_REALTIME_STORAGE = "test5/test5"

  var SPARK_REALTIME_HTTP_REQUEST_FREEQUENSY_SECONDS: Int = 60*5

  var TRAECTORY_SLICING_COUNT=10
}
