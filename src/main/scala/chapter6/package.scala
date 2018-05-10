/**
  * Created by yuxiao on 5/9/18.
  */
package object chapter6 {
  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }
}
