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
              for {
                trip <- tripDao.currentTrip(token)
                smsResponse <- sendSms(trip, userInfo)
              } yield Ok(smsResponse)
            case None => Future.successful(NotFound(""))
          }
        case None => Future.successful(BadRequest(""))
      }
    } else {
      Future.successful(Ok(""))
    }
  }}

  private def sendSms(trip: Trip, userInfo: UserInfo) = {
    val driverName = trip.driverName
    val driverUrl = driverName.toLowerCase() + ".isdriving.me"
    val message = s"Hi ${userInfo.uberUserInfo.firstName}! I'm $driverName and I'm driving you today. Learn what drives me at $driverUrl"
    smsService.to(userInfo, message)
  }

  def dummyStatusUpdated() = Action.async {
    val trip = Trip("7fca1042-e2f8-42ab-87c1-89b84298265c", "in_progress", "(555)555-5555", "Bert")
    val user = userInfoDao.fetch().head
    sendSms(trip, user).map(Ok(_))
  }
}
