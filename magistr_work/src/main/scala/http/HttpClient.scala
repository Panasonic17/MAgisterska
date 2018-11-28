package http

import java.net.SocketTimeoutException

import scalaj.http._

object HttpClient {
  def getBody(url: String): String = {
    try

      new HttpRequest(url = url,
        method = "GET",
        connectFunc = DefaultConnectFunc,
        params = Nil,
        headers = Seq("User-Agent" -> s"scalaj-http/test}"),
        options = HttpConstants.defaultOptions,
        proxyConfig = None,
        charset = HttpConstants.utf8,
        sendBufferSize = 4096,
        urlBuilder = QueryStringUrlFunc,
        compress = true,
        digestCreds = None).asString.body

    //      Http(url).asString.body
    catch {
      case ioe: SocketTimeoutException => {
        println("abuse");
        val a =Math.random()
        new HttpRequest(url = url,
          method = "GET",
          connectFunc = DefaultConnectFunc,
          params = Nil,
          headers = Seq("User-Agent" -> s"scalaj-http/$a}"),
          options = HttpConstants.defaultOptions,
          proxyConfig = None,
          charset = HttpConstants.utf8,
          sendBufferSize = 4096,
          urlBuilder = QueryStringUrlFunc,
          compress = true,
          digestCreds = None).asString.body
      }
    }
  }
}
