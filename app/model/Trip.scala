package model

import play.api.libs.json.{JsPath, Reads, Writes}
import play.api.libs.functional.syntax._

case class Trip(requestId: String, status: String, driverPhone: String, driverName: String)

object Trip {
  implicit val tripReads: Reads[Trip] = (
    (JsPath \ "request_id").read[String] and
    (JsPath \ "status").read[String] and
    (JsPath \ "driver" \ "phone_number").read[String] and
    (JsPath \ "driver" \ "name").read[String]
  )(Trip.apply _)

  implicit val tripWrites: Writes[Trip] = (
    (JsPath \ "request_id").write[String] and
      (JsPath \ "status").write[String] and
      (JsPath \ "driver" \ "phone_number").write[String] and
      (JsPath \ "driver" \ "name").write[String]
    )(unlift(Trip.unapply))
}
