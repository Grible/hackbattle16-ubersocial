package controller

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(view.html.index("Your new application is ready."))
  }

}