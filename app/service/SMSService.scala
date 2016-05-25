package service

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[CMSMSService])
trait SMSService {
  def sendTestMessageTo(phone: String)

}

class CMSMSService extends SMSService {
  override def sendTestMessageTo(phone: String) = ???
}
