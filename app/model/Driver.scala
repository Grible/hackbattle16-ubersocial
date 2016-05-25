package model

import play.api.libs.json._

case class Driver(name: String, phoneNumber: String, rating: Int, pictureUrl: String)

object Driver {

  implicit val driverModelFormat = Json.format[Driver]
}
