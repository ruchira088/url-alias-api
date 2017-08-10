package controllers.exceptions

import play.api.libs.json.{JsObject, Json}

case class UrlAliasNotFoundException(alias: String) extends Exception
{
  def toJson: JsObject = Json.obj("errorMessage" -> s"Alias NOT found. ${alias}")
}