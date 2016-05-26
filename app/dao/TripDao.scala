package dao

import javax.inject.Inject
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import model.{AccessToken, Trip}
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@ImplementedBy(classOf[TripDaoImpl])
trait TripDao {
  def currentTrip(accessToken: AccessToken): Future[Trip]

  def requestTrip(accessToken: AccessToken): Future[String]

  def startTrip(accessToken: AccessToken, requestId: String): Future[Unit]
}

@Singleton
class TripDaoImpl @Inject() (ws: WSClient) extends TripDao {
  val url = "https://sandbox-api.uber.com"

  def currentTrip(accessToken: AccessToken) = {
    val eventualResponse: Future[WSResponse] = retrieveCurrentTrip(accessToken.accessToken)

    eventualResponse.map(res => res.json.as[Trip])
  }

  private def retrieveCurrentTrip(accessToken: String) = {
    val endpoint = url + "/v1/requests/current"

    ws.url(endpoint)
      .withHeaders(("Authorization", s"Bearer $accessToken"))
      .get()
  }

  def requestTrip(accessToken: AccessToken) = {
    val eventualResponse: Future[WSResponse] = sendRequest(accessToken.accessToken)

    eventualResponse
      .map(res => (res.json \ "request_id").get.toString())
  }

  private def sendRequest(accessToken: String) = {
    val endpoint = url + "/v1/requests"

    val data = Json.obj(
      "start_latitude" -> 52.3863808,
      "start_longitude" -> 4.8619476,
      "end_latitude" -> 52.3779753,
      "end_longitude" -> 4.8951756
    )

    ws.url(endpoint)
      .withHeaders(("Authorization", s"Bearer $accessToken"))
      .post(data)
  }

  def startTrip(accessToken: AccessToken, requestId: String) = {
    val endpoint = url + "/v1/sandbox/requests/" + requestId

    for {
      acceptTrip <- updateStatus(accessToken.accessToken, "accepted", requestId)
      startTrip <- updateStatus(accessToken.accessToken, "in_progress", requestId)
    } yield Future.successful()
  }

  def updateStatus(accessToken: String, status: String, requestId: String) = {
    val endpoint = url + "/v1/sandbox/requests/" + requestId

    val data = Json.obj(
      "status" -> status
    )

    ws.url(endpoint)
      .withHeaders(("Authorization", s"Bearer $accessToken"))
      .put(data)
  }
}