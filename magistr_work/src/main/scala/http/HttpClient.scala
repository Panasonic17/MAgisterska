package http

import scalaj.http.Http

object HttpClient {
  def getBody(url: String): String = {
    Http(url).asString.body
  }
}
