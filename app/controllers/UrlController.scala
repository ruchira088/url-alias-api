package controllers

import javax.inject.{Inject, Singleton}

import controllers.requests.ClientUrlAlias
import controllers.requests.exceptions.FormValidationException
import dao.UrlAliasDAO
import models.UrlAlias
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import utils.GeneralUtils._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UrlController @Inject()(controllerComponents: ControllerComponents, urlAliasDao: UrlAliasDAO)
                             (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def create() = Action.async {
    implicit request: Request[AnyContent] =>
    {
      for {
        ClientUrlAlias(destinationUrl, alias) <- Future.fromTry(ClientUrlAlias.fromRequest)
        _ <- urlAliasDao.insert(UrlAlias(destinationUrl, alias.getOrElse("test-alias"), randomUuid()))
      } yield {
        Ok
      }
    } recover {
      case formValidationException @ FormValidationException(_) =>
        UnprocessableEntity(FormValidationException.errorJson(formValidationException))
    }
  }

}
