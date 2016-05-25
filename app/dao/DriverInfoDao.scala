package dao

import com.google.inject.ImplementedBy
import model.{DriverBio, DriverInfo, UberUserInfo, UserInfo}

@ImplementedBy(classOf[DriverInfoDaoImpl])
trait DriverInfoDao {
  def fetch(): Seq[DriverInfo]

  def findByNumber(telephoneNumber: String): Option[DriverInfo]

  def findByFirstName(firstName: String): Option[DriverInfo]
}

class DriverInfoDaoImpl extends DriverInfoDao {
  var driverInfo: Seq[DriverInfo] = List(
    DriverInfo(
      DriverBio("I like Apple, photography, drones, Coca-Cola, and KFC."),
      UserInfo(
        "0612345678",
        UberUserInfo("Steven", "", "steven@isdriving.me", "http://grible.co/images/team/99ade73c.steven.jpg", "1")
      )
    )
  )

  def fetch() = driverInfo

  def findByNumber(phoneNumber: String) = driverInfo.find(_.userInfo.phoneNumber == phoneNumber)

  def findByFirstName(firstName: String) = driverInfo.find(_.userInfo.uberUserInfo.firstName equalsIgnoreCase firstName)
}
