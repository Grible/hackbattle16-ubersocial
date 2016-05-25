package controller

import play.api._
import play.api.mvc._

class UberAuthenticationController extends Controller {

  def index = Action {
    Ok(view.html.connect("Your new application is ready."))
  }

}
