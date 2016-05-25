package dao

import com.google.inject.ImplementedBy
import model.UberDriverInfo

@ImplementedBy(classOf[UberDriverInfoDaoImpl])
trait UberDriverInfoDao {

  def fetch(): Seq[UberDriverInfo]

  def findByNumber(telephoneNumber: String): Option[UberDriverInfo]

  def add(driver: UberDriverInfo): Unit
}

class UberDriverInfoDaoImpl extends UberDriverInfoDao {

  var drivers: Seq[UberDriverInfo] = List(
    UberDriverInfo("Steven", "0612345678", 5, "http://grible.co/images/team/99ade73c.steven.jpg")
  )

  def fetch() = drivers

  def findByNumber(phoneNumber: String) = drivers.find(_.phoneNumber == phoneNumber)

  def add(driver: UberDriverInfo) = drivers = drivers :+ driver
}
