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
    "naaa" + "na"  + " Katamari Damacy!"
  }

  future foreach {
    case text => log(text)
  }

}

object PromisesAndCustomOperations extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete { case x => p tryComplete x }
      that onComplete { case y => p tryComplete y }
      p.future
    }
  }

  val f = Future { "now" } or Future { "later" }

  f foreach {
    case when => log(s"The future is $when")
  }
  Thread.sleep(3000)
}

object PromisesAndTimers extends App{
  import java.util._
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import PromisesAndCustomOperations._

  private val timer = new Timer(true)
  def timeout(millis: Long): Future[Unit] = {
    val p = Promise[Unit]
    timer.schedule(new TimerTask {
      override def run() = p.success(())
    },millis)
    p.future
  }

  val f = timeout(1000).map(_=>"timeout!") or Future{
    Thread.sleep(999)
    "work completed!"
  }
  f foreach{
    case text => log(text)
  }
  Thread.sleep(3000)
}

object PromisesCancellation extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def cancellable[T](b:Future[Unit] => T):(Promise[Unit],Future[T]) ={
      val p = Promise[Unit]
      val f = Future{
        val r = b(p.future)
        if(!p.tryFailure(new Exception))
           throw new CancellationException
        r
      }
    (p,f)
  }

  val (cancel,value) = cancellable {
    cancel =>
      var i =0
      while (i< 5){
        if(cancel.isCompleted) throw new CancellationException
        Thread.sleep(5000)
        log(s"$i: working")
        i+=1
      }
      "resulting value"
  }
  Thread.sleep(10000)
  cancel.trySuccess(())
  log("computation cancelled!")
}