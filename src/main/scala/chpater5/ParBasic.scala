package chpater5

/**
  * Created by yuxiao on 5/8/18.
  */

object ParBasic extends App {

  import scala.collection._



  val numbers = scala.util.Random.shuffle(Vector.tabulate(5000000)(i => i))

  val seqtime = timed {
    val n = numbers.max
    println(s"largest number $n")
  }

  log(s"Sequential time $seqtime ms")
  val partime = timed{
    val n = numbers.par.max
    println(s"largest number $n")
  }
  log(s"Parallel time $partime ms")
}

object ParUid extends App {
  import scala.collection._
  import java.util.concurrent.atomic._
  private val uid = new AtomicLong(0L)

  val seqtime = timed {
    for (i <- 0 until 10000000) uid.incrementAndGet()
  }
  log(s"Sequential time $seqtime ms")

  val partime = timed {
    for (i <- (0 until 10000000).par) uid.incrementAndGet()
  }
  log(s"Parallel time $partime ms")

}

object ParConfig extends App {
  import scala.collection._
  import scala.concurrent.forkjoin.ForkJoinPool

  val fjpool = new ForkJoinPool(2)
  val myTaskSupport = new parallel.ForkJoinTaskSupport(fjpool)
  val numbers = scala.util.Random.shuffle(Vector.tabulate(5000000)(i => i))
  val partime = timed {
    val parnumbers = numbers.par
    parnumbers.tasksupport = myTaskSupport
    val n = parnumbers.max
    println(s"largest number $n")
  }
  log(s"Parallel time $partime ms")
}

object ParHtmlSpecSearch extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.collection._
  import scala.io.Source

  def getHtmlSpec() = Future {
    val specSrc: Source = Source.fromURL("http://www.w3.org/MarkUp/html-spec/html-spec.txt")
    try specSrc.getLines.toArray finally specSrc.close()
  }

  getHtmlSpec() foreach { case specDoc =>
    log(s"Download complete!")

    def search(d: GenSeq[String]) = warmedTimed() {
      d.indexWhere(line => line.matches(".*TEXTAREA.*"))
    }

    val seqtime = search(specDoc)
    log(s"Sequential time $seqtime ms")

    val partime = search(specDoc.par)
    log(s"Parallel time $partime ms")
  }
    Thread.sleep(3000)
}