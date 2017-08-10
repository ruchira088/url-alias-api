package controllers.exceptions

import play.api.data.FormError
import play.api.libs.json.{JsValue, Json}

case class FormValidationException(errors: List[FormError]) extends Exception

case object FormValidationException
{
  private def formErrorToJson(formError: FormError): JsValue =
    Json.obj("field" -> formError.key, "description" -> formError.message)

  def errorJson(formValidationException: FormValidationException): JsValue =
    Json.obj("errors" -> formValidationException.errors.map(formErrorToJson))
}