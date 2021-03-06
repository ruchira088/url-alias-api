package utils

import java.util.UUID

object GeneralUtils
{
  def randomUuid(): String = UUID.randomUUID().toString

  def randomString(length: Int): String = {
    val random = randomUuid().replaceAll("-", "")

    if(random.length < length)
      random + randomString(length - random.length)
    else
      random.substring(0, length)
  }

  def optionString(value: String): Option[String] =
    value match {
      case null => None
      case _ => Some(value)
    }

}
