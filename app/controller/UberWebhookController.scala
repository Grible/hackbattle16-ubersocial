package controller

import javax.inject.Inject

import dao.{TripDao, UberUserInfoDao, UserInfoDao}
import model.{AccessToken, Trip, UserInfo}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UberWebhookController @Inject() (uberUserInfoDao: UberUserInfoDao, userInfoDao: UserInfoDao, tripDao: TripDao) extends Controller {

  def statusUpdated() = Action.async(parse.json) { implicit request => {
    val json: JsValue = request.body

    val status = (json \ "meta" \ "status").as[String]
    val userId = (json \ "meta" \ "user_id").as[String]

    if (status == "in_progress") {
      val maybeUserInfo: Option[UserInfo] = userInfoDao.findById(userId)

      maybeUserInfo match {
        case Some(userInfo) =>
          val maybeToken: Option[AccessToken] = uberUserInfoDao.getAccessToken(userInfo.uberUserInfo)
          maybeToken match {
            case Some(token) =>
              val eventualTrip: Future[Trip] = tripDao.currentTrip(token)
              eventualTrip.map(trip => {
                val driverName = trip.driverName
                println(s"sending SMS to ${userInfo.phoneNumber}, with driver name $driverName") // TODO: actually send SMS here
                Ok("")
              })
            case None => Future.successful(NotFound(""))
          }
        case None => Future.successful(BadRequest(""))
      }
    } else {
      Future.successful(Ok(""))
    }
  }}
}
