package lilele

/**
  * Created by yuxiao on 4/9/18.
  */


object HelloWorld extends App{
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  val buildFile: Future[String] = Future {
    val f = Source.fromFile("build.sbt")
    try f.getLines.mkString("\n") finally f.close()
  }

  log(s"started reading build file asynchronously")
  log(s"status: ${buildFile.isCompleted}")
  Thread.sleep(250)
  log(s"status: ${buildFile.isCompleted}")
  log(s"status: ${buildFile.value}")
}

object FuturesCallbacks extends App{
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  def getUrlSpec():Future[Seq[String]] = Future{
    val f = Source.fromURL("http://www.w3.org/Addressing/URL/url-spec.txt")
    try f.getLines().toList finally f.close()

  }
  val urlSpec: Future[Seq[String]] = getUrlSpec()

  def find(lines: Seq[String],word: String) = lines.zipWithIndex collect{
    case (line,n) if line.contains(word) => (n,line)

  } mkString("\n")

//  urlSpec foreach{
//    lines => log(s"Found occurences of 'telnet'\n${find(lines,"telnet")}")
//  }

  urlSpec foreach {
    lines => log(s"Found occurrences of 'password'\n${find(lines, "password")}\n")
  }

  log("callbacks installed, continuing with other work")

  Thread.sleep(2000)
}

object FutureFailure extends App{
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source


  val urlSpec: Future[String] = Future {
    Source.fromURL("http://www.w3.org/non-existent-url-spec.txt").mkString
  }

  urlSpec.failed foreach{
    case t => log(s"exception ocurred - $t")
  }
  Thread.sleep(1000)
}

object FutureTry extends App{
  import scala.util._

  val threadName: Try[String] = Try(Thread.currentThread.getName)
   val someText: Try[String] = Try("Try objects are created synchronously")
  val message: Try[String] = for{
    tn <- threadName
    st <- someText
  } yield s"$st, t=$tn"

  message match {
    case Success(msg) => log(msg)
    case Failure(error) => log(s"There should be no $error here.")
  }

}

object FuturesClumsyCallback extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import org.apache.commons.io.FileUtils._
  import java.io._
  import scala.io.Source
  import scala.collection.convert.decorateAsScala._

  def blacklistFile(filename: String) = Future {
    println(s"fe")

    val lines = Source.fromFile(filename).getLines
    lines.filter(!_.startsWith("#")).toList
  }

  def findFiles(patterns: List[String]): List[String] = {
    val root = new File(".")
    for {
      f <- iterateFiles(root, null, true).asScala.toList
      pat <- patterns
      abspat = root.getCanonicalPath + File.separator + pat
      if f.getCanonicalPath.contains(abspat)
    } yield f.getCanonicalPath
  }

  blacklistFile(".gitignore") foreach {
    case lines =>
      val files = findFiles(lines)
      log(s"matches: ${files.mkString("\n")}")
  }
}

object FuturesFlatMapRaw extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.Source

//  val netiquette = Future { Source.fromURL("http://www.ietf.org/rfc/rfc1855.txt").mkString }
//  val urlSpec = Future { Source.fromURL("http://www.w3.org/Addressing/URL/url-spec.txt").mkString }
////  val answer = netiquette.flatMap { nettext =>
////    urlSpec.map { urltext =>
////      "First, read this: " + nettext + ". Now, try this: " + urltext
////    }
////  }
//
//  val answer = for{
//    nettext <- netiquette
//    urltext <- urlSpec
//  } yield {
//    "First, read this:" + nettext + ".Now try this:"+urltext
//  }
//  answer foreach {
//    case contents => log(contents)
//  }

  val netiqueettelUrl =  "http://www.baidu.com"
  val netiquett = Future{ Source.fromURL(netiqueettelUrl).mkString}
  val answer2= netiquett recover {
    case e: java.io.FileNotFoundException =>{
      "Dear "
    }
  }
  answer2 foreach {
    case contents => log(contents)
  }
  Thread.sleep(3000)
}
