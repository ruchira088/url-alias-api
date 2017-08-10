package controllers.exceptions

import play.api.data.FormError
import play.api.libs.json.{JsObject, Json}

case class FormValidationException(errors: List[FormError]) extends Exception
{
  private def formErrorToJson(formError: FormError): JsObject =
    Json.obj("field" -> formError.key, "description" -> formError.message)

  def toJson = Json.obj("errors" -> errors.map(formErrorToJson))
}
