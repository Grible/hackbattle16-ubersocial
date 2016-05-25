package controller

import javax.inject.Inject

import play.api._
import play.api.libs.ws.{WSResponse, WSRequest, WSClient}
import play.api.mvc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UberAuthenticationController @Inject() (ws: WSClient) extends Controller {

  def index = Action {
    Ok(view.html.connect())
  }

  def confirmAuth = Action { implicit request => {
      val possibleAuthorizationCode: Option[String] = request.getQueryString("code")
      possibleAuthorizationCode match {
        case Some(code: String) =>
          retrieveUberAccessToken(code)
          Ok("")
        case None => NotFound("Oops!")
      }
    }
  }

  def retrieveUberAccessToken(authorizationCode: String) = {
    val url = "https://login.uber.com/oauth/v2/token"
    val clientId = "wrM46LuAXxFP4CqmeaOX4wlO66g0ZsMI"
    val clientSecret = "1w0-YUL5d4XMZAhY4yhJWX1G6dWg-YvWTAO3f5kX"
    val request: WSRequest = ws.url(url)

    val data = Map(
      "client_secret" -> Seq(clientSecret),
      "client_id" -> Seq(clientId),
      "grant_type" -> Seq("authorization_code"),
      "code" -> Seq(authorizationCode)
    )

    val futureResponse: Future[WSResponse] = request.post(data)

    futureResponse.onSuccess { case res =>
      println(res.json)
      println(res.statusText)
    }

    futureResponse.onFailure { case res =>
        println(res.getMessage)
    }
  }

}
