package model

import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._

case class AccessToken(accessToken: String, tokenType: String, expires: Long, refreshToken: String, scope: String)

object AccessToken {

  implicit val accessTokenReads: Reads[AccessToken] = (
    (JsPath \ "access_token").read[String] and
    (JsPath \ "token_type").read[String] and
    (JsPath \ "expires_in").read[Long] and
    (JsPath \ "refresh_token").read[String] and
    (JsPath \ "scope").read[String]
  )(AccessToken.apply _)
}

