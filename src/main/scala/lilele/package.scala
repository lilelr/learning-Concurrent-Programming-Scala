/**
  * Created by yuxiao on 4/9/18.
  */
package object lilele {
  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }
}
