package controller

import javax.inject.Inject

import dao.{TripDao, UberUserInfoDao, UserInfoDao}
import model.{AccessToken, UserInfo}
import model.UberUserInfo._
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UberAuthenticationController @Inject() (ws: WSClient, uberUserInfoDao: UberUserInfoDao, userInfoDao: UserInfoDao, tripDao: TripDao) extends Controller {

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
          for {
            accessToken <- retrieveUberAccessToken(code)
            uberUserInfo <- uberUserInfoDao.fetch(accessToken)
            requestId <- tripDao.requestTrip(accessToken)
            requestTrip <- tripDao.startTrip(accessToken, requestId)
            trip <- tripDao.currentTrip(accessToken)
          } yield {
            val userInfo: UserInfo = UserInfo("0612345678", uberUserInfo)
            userInfoDao.add(userInfo)
            Ok(Json.toJson(userInfo))
          }
        // TODO: store actual telephone number and redirect to form for telephone number
        case None => Future.successful(NotFound("Oops!"))
      }
    }
  }

  def retrieveUberAccessToken(authorizationCode: String): Future[AccessToken] = {
    val data = Map(
      "client_secret" -> Seq(clientSecret),
      "client_id" -> Seq(clientId),
      "grant_type" -> Seq("authorization_code"),
      "code" -> Seq(authorizationCode),
      "redirect_uri" -> Seq("http://localhost:9000/hookmeup") // TODO: change for live
    )

    ws.url(url).post(data).map(res => {
      res.json.as[AccessToken]
    })
  }
}
