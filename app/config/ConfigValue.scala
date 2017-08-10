package config

import config.ConfigUtils._

import scala.util.control.NonFatal

case class ConfigValue[A](name: String, default: A)

object ConfigValue
{
  def getConfigValue[A](configValue: ConfigValue[A]): A =
    getEnvValue(convertToEnvFormat(configValue.name)).toOption
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

