package controllers.requests

import controllers.requests.exceptions.FormValidationException
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{AnyContent, Request}

import scala.util.{Failure, Success, Try}

case class ClientUrlAlias(destinationUrl: String, alias: Option[String])

case object ClientUrlAlias
{
  def fromRequest(implicit request: Request[AnyContent]): Try[ClientUrlAlias] =
  {
    Form(
      mapping(
        "destinationUrl" -> text,
        "alias" -> optional(text)
      )(ClientUrlAlias.apply)(ClientUrlAlias.unapply)
    )
      .bindFromRequest()
      .fold(
        form => Failure(FormValidationException(form.errors.toList)),
        clientUrlAlias => Success(clientUrlAlias)
      )
  }
}