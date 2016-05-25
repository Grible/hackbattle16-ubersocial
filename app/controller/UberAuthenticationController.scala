package controller

import javax.inject.Inject

import play.api._
import play.api.libs.ws.{WSResponse, WSRequest, WSClient}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UberAuthenticationController @Inject() (ws: WSClient) extends Controller {

  val clientSecret = "1w0-YUL5d4XMZAhY4yhJWX1G6dWg-YvWTAO3f5kX"
  val clientId = "wrM46LuAXxFP4CqmeaOX4wlO66g0ZsMI"
  val url = "https://login.uber.com/oauth/v2/token"

  def index = Action {
    Ok(view.html.connect(clientId))
  }

  def confirmAuth = Action.async { implicit request => {
      val possibleAuthorizationCode: Option[String] = request.getQueryString("code")
      possibleAuthorizationCode match {
        case Some(code: String) =>
          retrieveUberAccessToken(code).map(_ => Ok(""))
        case None => Future.successful(NotFound("Oops!"))
      }
    }
  }

  def retrieveUberAccessToken(authorizationCode: String): Future[WSResponse] = {
    val data = Map(
      "client_secret" -> Seq(clientSecret),
      "client_id" -> Seq(clientId),
      "grant_type" -> Seq("authorization_code"),
      "code" -> Seq(authorizationCode),
      "redirect_uri" -> Seq("http://localhost:9000/hookmeup")
    )

    ws.url(url).post(data)
  }

}
