package dao

import com.google.inject.ImplementedBy
import model.Driver

@ImplementedBy(classOf[DriverDaoImpl])
trait DriverDao {

  def fetch(): Seq[Driver]

  def findByNumber(telephoneNumber: String): Option[Driver]

  def add(driver: Driver): Unit
}

class DriverDaoImpl extends DriverDao {

  var drivers: Seq[Driver] = List(
    Driver("Steven", "0612345678", 5, "http://grible.co/images/team/99ade73c.steven.jpg")
  )

  def fetch() = drivers

  def findByNumber(phoneNumber: String) = drivers.find(_.phoneNumber == phoneNumber)

  def add(driver: Driver) = drivers = drivers :+ driver
}
