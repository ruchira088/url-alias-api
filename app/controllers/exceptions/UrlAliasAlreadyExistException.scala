package controllers.exceptions

import play.api.libs.json.Json

case class UrlAliasAlreadyExistException(alias: String) extends Exception
{
  def toJson = Json.obj("errorMessage" -> s"${alias} already exists.")
}