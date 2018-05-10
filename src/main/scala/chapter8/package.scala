/**
  * Created by yuxiao on 5/10/18.
  */



import akka.actor.ActorSystem
import com.typesafe.config._

package object chapter8 {
  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }


  lazy val ourSystem = ActorSystem("OurExampleSystem")

  def remotingConfig(port: Int) = ConfigFactory.parseString(s"""
akka {
  actor.provider = "akka.remote.RemoteActorRefProvider"
  remote {
    enabled-transports = ["akka.remote.netty.tcp"]
    netty.tcp {
      hostname = "127.0.0.1"
      port = $port
    }
  }
}
  """)

  def remotingSystem(name: String, port: Int) = ActorSystem(name, remotingConfig(port))

}
