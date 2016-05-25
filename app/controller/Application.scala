package controller

import javax.inject.Inject

import dao.DriverDao
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

class Application @Inject() (driverDao: DriverDao) extends Controller {

  def index = Action {
    Ok(view.html.index("Your new application is ready."))
  }

  def drivers = Action {
    Ok(Json.toJson(driverDao.fetch()))
  }

}