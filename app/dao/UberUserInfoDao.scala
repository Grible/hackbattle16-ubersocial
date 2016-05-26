package dao

import javax.inject.Inject
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import model.{AccessToken, UberUserInfo}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[UberUserInfoDaoImpl])
trait UberUserInfoDao {
  def fetch(accessToken: AccessToken): Future[UberUserInfo]

  def getAccessToken(uberUserInfo: UberUserInfo): Option[AccessToken]
}

@Singleton
class UberUserInfoDaoImpl @Inject() (ws: WSClient) extends UberUserInfoDao {
  val url = "https://api.uber.com/v1/me"

  var uberUserInfoMap: Map[AccessToken, UberUserInfo] = Map()

  def fetch(accessToken: AccessToken): Future[UberUserInfo] = {
    val maybeInfo: Option[UberUserInfo] = uberUserInfoMap.get(accessToken)

    maybeInfo match {
      case Some(info) =>
        Future.successful(info)
      case None =>
        val eventualInfo: Future[UberUserInfo] = retrieveUberUserInfo(accessToken.accessToken)
          .map(_.json.as[UberUserInfo])
        eventualInfo.map(info => {
          uberUserInfoMap = uberUserInfoMap + (accessToken -> info)
          info
        })
    }
  }

  def retrieveUberUserInfo(accessToken: String): Future[WSResponse] = {
    ws.url(url)
      .withHeaders(("Authorization", s"Bearer $accessToken"))
      .get()
  }

  def getAccessToken(uberUserInfo: UberUserInfo) = {
    uberUserInfoMap.find(entry => entry._2 == uberUserInfo).map(_._1)
  }
}
