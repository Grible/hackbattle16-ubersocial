package model

import play.api.libs.json._

case class UberDriverInfo(name: String, phoneNumber: String, rating: Int, pictureUrl: String)

object UberDriverInfo {
  implicit val driverModelFormat = Json.format[UberDriverInfo]
}

case class DriverInfo(phoneNumber: String, driverBio: DriverBio)

object DriverInfo {
  implicit val driverModelFormat = Json.format[DriverInfo]
}

case class DriverBio(bio: String)

object DriverBio {
  implicit val driverBioModelFormat = Json.format[DriverBio]
}