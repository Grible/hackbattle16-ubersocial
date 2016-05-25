package model

import play.api.libs.json._
import play.api.libs.functional.syntax._

// Info about the driver that is retrieved from /v1/requests/current once a trip starts
case class UberDriverInfo(name: String, phoneNumber: String, rating: Int, pictureUrl: String)

object UberDriverInfo {
  implicit val uberDriverInfoFormat = Json.format[UberDriverInfo]
}

// Info about the driver/user that we obtain once he logs in with his Uber-account in our application
case class UberUserInfo(firstName: String, lastName: String, email: String, picture: String, uuid: String)

object UberUserInfo {
  implicit val uberUserInfoWrites: Writes[UberUserInfo] = (
    (JsPath \ "firstName").write[String] and
    (JsPath \ "lastName").write[String] and
    (JsPath \ "email").write[String] and
    (JsPath \ "picture").write[String] and
    (JsPath \ "uuid").write[String]
    )(unlift(UberUserInfo.unapply))

  implicit val uberUserInfoReads: Reads[UberUserInfo] = (
    (JsPath \ "first_name").read[String] and
    (JsPath \ "last_name").read[String] and
    (JsPath \ "email").read[String] and
    (JsPath \ "picture").read[String] and
    (JsPath \ "uuid").read[String]
    )(UberUserInfo.apply _)
}

// Info about the user plus telephone number, obtained during registration in our application
case class UserInfo(phoneNumber: String, uberUserInfo: UberUserInfo)

object UserInfo {
  implicit val userInfoFormat = Json.format[UserInfo]
}

// Additional info that we collect about the driver once he registers in our application
case class DriverBio(bio: String)

object DriverBio {
  implicit val driverBioFormat = Json.format[DriverBio]
}

// Object containing all the information that we collected from the driver
case class DriverInfo(driverBio: DriverBio, userInfo: UserInfo)

object DriverInfo {
  implicit val driverInfoFormat = Json.format[DriverInfo]
}

