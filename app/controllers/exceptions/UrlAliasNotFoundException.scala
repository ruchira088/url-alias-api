package controllers.exceptions

import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._

case class UrlAliasNotFoundException(alias: String) extends Exception
{
  def toResult: Result = NotFound(Json.obj("errorMessage" -> s"Alias NOT found. ${alias}"))
}