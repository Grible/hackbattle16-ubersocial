package dao

import com.google.inject.ImplementedBy
import model.{UberUserInfo, UserInfo}

@ImplementedBy(classOf[UserInfoDaoImpl])
trait UserInfoDao {
  def fetch(): Seq[UserInfo]

  def findByNumber(phoneNumber: String): Option[UserInfo]

  def add(userInfo: UserInfo): Unit
}

class UserInfoDaoImpl extends UserInfoDao {
  var userInfo = List(
    UserInfo(
      "0612345678",
      UberUserInfo("Steven", "", "steven@isdriving.me", "http://grible.co/images/team/99ade73c.steven.jpg", "1")
    )
  )

  def fetch() = userInfo

  def findByNumber(phoneNumber: String) = userInfo.find(_.phoneNumber == phoneNumber)

  def add(newUserInfo: UserInfo) = userInfo = userInfo :+ newUserInfo
}
