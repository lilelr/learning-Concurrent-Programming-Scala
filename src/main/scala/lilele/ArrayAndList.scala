package lilele

/**
  * Created by yuxiao on 4/25/18.
  */
object ArrayAndList extends App{
  println(s"${Thread.currentThread.getName}: lele")
  val array = new Array[String](3)
  array(0) = "This "
  array(1) = "is"
  array(2) = "mutable"
  println(array)
  array.foreach(println)

  val myList = List("This", "is", "immutable")
  println(myList.getClass)
  val oldList = List(1,2)
  val newList = 3 :: oldList
  newList.foreach(println)

  println()
  val newList2 = oldList:+3
  newList2.foreach(println)
  println()

  val afterDelete = newList2.filterNot(_ == 3)
  afterDelete.foreach(println)

  println()
  println(s"${afterDelete}\n")

  for( item <- afterDelete){
    println(s"lele : ${item}")
  }

}
