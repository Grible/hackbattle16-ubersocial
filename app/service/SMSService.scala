package service

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[CmSmsService])
trait SmsService {
  def sendTestMessageTo(phone: String): String

}

class CmSmsService extends SmsService {
  private val token = "8b775e97-c99f-4a35-9971-f3865181ef60"
  "https://sgw01.cm.nl/gateway.ashx?producttoken=8b775e97-c99f-4a35-9971-f3865181ef60&body=Example+message+text&to=&from=TNW Hackba.&reference=your_reference"

  def encode(text: String) = java.net.URLEncoder.encode(text, "utf8")

  override def sendTestMessageTo(phone: String) = {
    val url =
      s"""https://sgw01.cm.nl/gateway.ashx?
         |producttoken=$token
         |&body=${encode("Awesome! This thing is working. Greetings from Grible at TNW!")}
         |&to=$phone
         |&from=whoisdrivingme
         |&reference=test
         |""".stripMargin

    val res: String = scala.io.Source.fromURL(url).mkString
    res
  }
}
