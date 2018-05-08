package lilele

import scala.concurrent.Promise

/**
  * Created by yuxiao on 5/7/18.
  */
object Promise_learning extends App{
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }
   val p = Promise[String]
   val q = Promise[String]
  p.future foreach{ case x=> log(s"p succeeded with $x")}
  Thread.sleep(1000)
  p success("assigned")
  q failure new Exception("not kept")
  q.future.failed foreach{ case t => log(s"q failed with $t")}
  Thread.sleep(1000)


}

object PromisesCustomAsync extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.util.control.NonFatal

  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  def myFuture[T](body: =>T): Future[T] = {
    val p = Promise[T]

    global.execute(new Runnable {
      def run() = try {
        val result = body
        p success result
      } catch {
        case NonFatal(e) =>
          p failure e
      }
    })

    p.future
  }

  val future = myFuture {
    "naaa" + "na" * 8 + " Katamari Damacy!"
  }

  future foreach {
    case text => log(text)
  }

}
