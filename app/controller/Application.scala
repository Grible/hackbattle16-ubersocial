package controller

import javax.inject.Inject

import dao.{DriverInfoDao, UberDriverInfoDao}
import model.UberDriverInfo
import play.api._
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc._

class Application @Inject() (uberDriverInfoDao: UberDriverInfoDao, driverInfoDao: DriverInfoDao) extends Controller {

  def index = Action { request =>
    Ok(view.html.index("Your new application is ready."))
  }

  def driver = Action {
    Ok(view.html.driver(driverInfoDao.fetch().head))
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

  def sendSMS = Action {
    Ok()
  }
}