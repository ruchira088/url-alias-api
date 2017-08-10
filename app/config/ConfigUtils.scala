package config

import scala.util.{Failure, Success, Try}

object ConfigUtils
{
  val CONFIG_PREFIX = "UrlAliasApi"

  def getEnvValue(name: String): Try[String] = System.getProperty(name) match
    {
      case value: String => Success(value)
      case _ => Failure(new Exception(s"Unable to fetch $name from environment variables"))
    }

  def convertToEnvFormat(string: String): String =
    string.flatMap {
      case char if char.isUpper => s"_$char"
      case char => char.toUpper.toString
    }

  def getPlayConfigValue[A](name: String): Option[String] =
    play.api.Configuration.reference.entrySet.toMap.get(name).map(_.render())

}
