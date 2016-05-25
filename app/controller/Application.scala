package controller

import javax.inject.Inject

import play.api.libs.ws.{WSClient, _}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject() (ws: WSClient) extends Controller {

  def index = Action { implicit request => {

      val possibleAuthorizationCode: Option[String] = request.getQueryString("code")

      possibleAuthorizationCode match {
        case Some(code: String) => retrieveUberAccessToken(code)
        case None =>
      }

      Ok(view.html.index("Your new application is ready."))
    }
  }

  def retrieveUberAccessToken(authorizationCode: String) = {

      println(authorizationCode)

      val url = "https://login.uber.com/oauth/v2/token"
      val clientId = "wrM46LuAXxFP4CqmeaOX4wlO66g0ZsMI"
      val clientSecret = "1w0-YUL5d4XMZAhY4yhJWX1G6dWg-YvWTAO3f5kX"
      val request: WSRequest = ws.url(url)

      val data = Map(
        "client_secret" -> Seq(clientSecret),
        "client_id" -> Seq(clientId),
        "grant_type" -> Seq("authorization_code"),
        "redirect_uri" -> Seq("http://localhost:9000"),
        "code" -> Seq(authorizationCode)
      )

      val futureResponse: Future[WSResponse] = request.post(data)

      futureResponse.onSuccess { case res =>
        println(res)
      }
    }
}