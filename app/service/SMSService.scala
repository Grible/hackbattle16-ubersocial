package service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[MessageBirdSmsService])
trait SmsService {
  def sendTestMessageTo(phone: String): Future[String]

}

class CmSmsService extends SmsService {
  private val token = "8b775e97-c99f-4a35-9971-f3865181ef60"
  "https://sgw01.cm.nl/gateway.ashx?producttoken=8b775e97-c99f-4a35-9971-f3865181ef60&body=Example+message+text&to=&from=TNW Hackba.&reference=your_reference"

  def encode(text: String) = java.net.URLEncoder.encode(text, "ascii")

  override def sendTestMessageTo(phone: String) = {
    val url =
      s"https://sgw01.cm.nl/gateway.ashx?" +
        s"producttoken=$token" +
        s"&body=${encode("Awesome. This thing is working. Greetings from Grible at TNW")}" +
        s"&to=${encode(phone)}" +
        s"&from=whoisdrivingme" +
        s"&reference=test"

    println(url)

    val res: String = scala.io.Source.fromURL(url).mkString
    Future.successful(res)
  }
}

class MessageBirdSmsService @Inject() (ws: WSClient) extends SmsService {
  private val token = "test_MfaLXX0SMR3CdJiQt1ybCgGQR"
  val apiUrl = "https://rest.messagebird.com"

  override def sendTestMessageTo(phone: String) = {
    val request = ws.url(apiUrl + "/messages")

    val eventualResponse: Future[WSResponse] = request
      .withHeaders(
        "Authorization" -> s"AccessKey $token",
        "Accept" -> "application/json")
      .post(Map(
        "originator" -> Seq("UBER"),
        "body" -> Seq("Awesome. This thing is working. Greetings from Grible at TNW"),
        "recipients" -> Seq(phone)))
    eventualResponse.map(_.json.toString)
  }
}