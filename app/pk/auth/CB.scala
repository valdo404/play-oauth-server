package pk.auth

import javax.inject.{Inject, Singleton}
import play.api.inject.ApplicationLifecycle
import com.sandinh.couchbase.CBCluster
import play.api.libs.json.{JsNumber, JsObject, JsValue}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CB @Inject() (lifecycle: ApplicationLifecycle, cluster: CBCluster) {
  import ExecutionContext.Implicits.global

  lifecycle.addStopHook(() => Future {
    cluster.disconnect()
  })

  lazy val authBucket = cluster.openBucket("auth")
}

object CBType {
  val TUser = 1
  val TApp = 2
  val TToken = 3
  val TCode = 4
  val TPermission = 4

  def addTpe(tpe: Int) = (js: JsValue) => js match {
    case js: JsObject => js + ("tpe" -> JsNumber(tpe))
    case _ => throw new RuntimeException("expect JsObject")
  }
}
