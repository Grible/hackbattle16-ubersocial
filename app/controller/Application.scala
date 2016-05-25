package controller

import javax.inject.Inject

import dao.DriverDao
import model.UberDriverInfo
import play.api._
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc._
import service.SmsService

class Application @Inject() (driverDao: DriverDao, sms: SmsService) extends Controller {

  def index = Action { request =>
    println(request.host)

    Ok(view.html.index("Your new application is ready."))
  }

  def drivers = Action {
    Ok(Json.toJson(driverDao.fetch()))
  }

  def addDriver = Action(BodyParsers.parse.json) { request =>
    val driverResult: JsResult[UberDriverInfo] = request.body.validate[UberDriverInfo]

    driverResult.fold(
      errors => {
        BadRequest(JsError.toJson(errors))
      },
      driver => {
        driverDao.add(driver)
        Ok("")
      }
    )
  }

  def sendSMS = Action {
    Ok(sms.sendTestMessageTo("0031628302534"))
  }
}