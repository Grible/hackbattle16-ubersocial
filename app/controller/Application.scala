package controller

import javax.inject.Inject

import dao.DriverDao
import model.Driver
import play.api._
import play.api.libs.json.{JsError, JsResult, Json}
import play.api.mvc._

class Application @Inject() (driverDao: DriverDao) extends Controller {

  def index = Action {
    Ok(view.html.index("Your new application is ready."))
  }

  def drivers = Action {
    Ok(Json.toJson(driverDao.fetch()))
  }

  def addDriver = Action(BodyParsers.parse.json) { request =>
    val driverResult: JsResult[Driver] = request.body.validate[Driver]

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
    Ok()
  }
}