package config

import com.typesafe.config.ConfigFactory
import utils.GeneralUtils.optionString

import scala.util.control.NonFatal

case class ConfigValue[A](name: String, default: A)

object ConfigValue
{
  private val CONFIG_PREFIX = "UrlAliasApi"

  private def getEnvValue(name: String): Option[String] = optionString(System.getProperty(name))

  private def convertToEnvFormat(string: String): String =
    string.flatMap {
      case char if char.isUpper => s"_$char"
      case char => char.toUpper.toString
    }

  private def getPlayConfigValue[A](name: String): Option[String] =
    optionString(ConfigFactory.load().getString(s"$CONFIG_PREFIX.$name"))

  def getConfigValue[A](configValue: ConfigValue[A]): A =
    getEnvValue(convertToEnvFormat(configValue.name))
      .orElse(getPlayConfigValue(configValue.name))
      .flatMap {
        value => {
          val envValue = configValue.default match {
            case _: Int => value.toInt
            case _: Boolean => value.toBoolean
            case _ => value
          }

          try {
            Some(envValue.asInstanceOf[A])
          } catch {
            case NonFatal(_) => None
          }
        }
      }
      .getOrElse(configValue.default)
}

