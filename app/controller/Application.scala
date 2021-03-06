package controller

import javax.inject.Inject

import dao.{DriverInfoDao, UberDriverInfoDao}
import model.{DriverInfo, UberDriverInfo}
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc._
import service.SmsService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject() (uberDriverInfoDao: UberDriverInfoDao, driverInfoDao: DriverInfoDao, sms: SmsService) extends Controller {

  def driver = Action { request =>
    val subDomain = request.domain.split('.').headOption

    subDomain match {
      case None => NotFound("No driver found")
      case Some(firstName) =>
        val maybeDriverInfo: Option[DriverInfo] = driverInfoDao.findByFirstName(firstName)
        maybeDriverInfo match {
          case None => NotFound("No driver found")
          case Some(driverInfo) => Ok(view.html.driver(driverInfo))
        }
    }
  }

  def drivers = Action {
    Ok(Json.toJson(uberDriverInfoDao.fetch()))
  }

  def addDriver = Action(BodyParsers.parse.json) { request =>
    val driverResult: JsResult[UberDriverInfo] = request.body.validate[UberDriverInfo]

    driverResult.fold(
      errors => {
        BadRequest(JsError.toJson(errors))
      },
      driver => {
        uberDriverInfoDao.add(driver)
        Ok("")
      }
    )
  }

  def sendSMS = Action.async { request =>
    val maybeEventualString: Option[Future[String]] = request.getQueryString("phone")
      .map(sms.testMessageTo)

    maybeEventualString match {
      case Some(s) => s.map(Ok(_))
      case None => Future.successful(NotFound("Could not sms, maybe number incorrect?"))
    }
  }

}