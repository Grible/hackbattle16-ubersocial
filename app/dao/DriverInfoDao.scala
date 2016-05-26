package dao

import javax.inject.Inject
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import model.{DriverBio, DriverInfo, UberUserInfo, UserInfo}

@ImplementedBy(classOf[DriverInfoDaoImpl])
trait DriverInfoDao {
  def fetch(): Seq[DriverInfo]

  def findByNumber(telephoneNumber: String): Option[DriverInfo]

  def findByFirstName(firstName: String): Option[DriverInfo]
}

@Singleton
class DriverInfoDaoImpl @Inject() (userInfoDao: UserInfoDao) extends DriverInfoDao {
  var driverInfo: Seq[DriverInfo] = List(
    DriverInfo(
      DriverBio("I like Apple, photography, drones, Coca-Cola, and KFC."),
      UserInfo("(555)555-5555",
        UberUserInfo(
          "Bert", "", "", "https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/driver.jpg", ""
        )
      )
    )
  )

  def fetch() = driverInfo

  def findByNumber(phoneNumber: String) = driverInfo.find(_.userInfo.phoneNumber == phoneNumber)

  def findByFirstName(firstName: String) = driverInfo.find(_.userInfo.uberUserInfo.firstName equalsIgnoreCase firstName)
}
