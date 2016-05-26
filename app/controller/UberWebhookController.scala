package controller

import javax.inject.Inject

import dao.{TripDao, UberUserInfoDao, UserInfoDao}
import model.{AccessToken, Trip, UserInfo}
import play.api.libs.json.JsValue
import play.api.mvc.{Action, Controller}
import service.SmsService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UberWebhookController @Inject() (uberUserInfoDao: UberUserInfoDao, userInfoDao: UserInfoDao, tripDao: TripDao, smsService: SmsService) extends Controller {

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

  def sendSms(trip: Trip, userInfo: UserInfo) = {
    val driverName = trip.driverName
    val driverUrl = driverName + "isdriving.me"
    val message = s"Hi ${userInfo.uberUserInfo.firstName}! I'm $driverName, your driver for today. Learn more about me at $driverUrl"
//    smsService.to(userInfo, message)
  }

  def dummyStatusUpdated() = Action {
    val trip = Trip("7fca1042-e2f8-42ab-87c1-89b84298265c", "in_progress", "(555)555-5555", "John")
    val user = userInfoDao.fetch().head
    sendSms(trip, user)
    Ok("")
  }
}
