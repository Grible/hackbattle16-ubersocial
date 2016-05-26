package service

import javax.inject.Inject

import com.google.inject.ImplementedBy
import model.UserInfo
import play.api.Configuration
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[MessageBirdSmsService])
trait SmsService {
  def testMessageTo(phone: String): Future[String]
  def to(user: UserInfo, message: String): Future[String]
}

class MessageBirdSmsService @Inject() (ws: WSClient, conf: Configuration) extends SmsService {
  private val token = conf.getString("messagebird.api.token")
  lazy val authorizationHeader = "Authorization" -> s"AccessKey ${token.get}"
  val contentTypeHeader = "Accept" -> "application/json"
  val apiUrl = "https://rest.messagebird.com"

  override def testMessageTo(phone: String) = {
    val request = ws.url(apiUrl + "/messages")
    if (token.isEmpty) throw new IllegalArgumentException("API token not available for sending sms")

    val eventualResponse: Future[WSResponse] = request
      .withHeaders(authorizationHeader, contentTypeHeader)
      .post(Map(
        "originator" -> Seq("UBER"),
        "body" -> Seq("Awesome. This thing is working. Greetings from Grible at TNW"),
        "recipients" -> Seq(phone)))
    eventualResponse.map(_.json.toString)
  }

  override def to(user: UserInfo, message: String) = {
    val request = ws.url(apiUrl + "/messages")
    if (token.isEmpty) throw new IllegalArgumentException("API token not available for sending sms")

    val eventualResponse: Future[WSResponse] = request
      .withHeaders(authorizationHeader, contentTypeHeader)
      .post(Map(
        "originator" -> Seq("UBER SOCIAL"),
        "body" -> Seq(message),
        "recipients" -> Seq(user.phoneNumber)))
    eventualResponse.map(_.json.toString)
  }
}