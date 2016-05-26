package dao

import javax.inject.Singleton

import com.google.inject.ImplementedBy
import model.{UberUserInfo, UserInfo}

@ImplementedBy(classOf[UserInfoDaoImpl])
trait UserInfoDao {
  def fetch(): Seq[UserInfo]

  def findByNumber(phoneNumber: String): Option[UserInfo]

  def findById(uuid: String): Option[UserInfo]

  def add(userInfo: UserInfo): Unit
}

@Singleton
class UserInfoDaoImpl extends UserInfoDao {
  var userInfo = List(
    UserInfo(
      "31628302534",
      UberUserInfo("Frank", "Lagendijk", "frank@grible.co", "", "1")
    )
  )

  def fetch() = userInfo

  def findByNumber(phoneNumber: String) = userInfo.find(_.phoneNumber == phoneNumber)

  def findById(uuid: String) = userInfo.find(_.uberUserInfo.uuid == uuid)

  def add(newUserInfo: UserInfo) = userInfo = userInfo :+ newUserInfo
}
