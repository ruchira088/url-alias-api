package controllers

import javax.inject.{Inject, Singleton}

import controllers.exceptions.{FormValidationException, UrlAliasNotFoundException}
import controllers.requests.ClientUrlAlias
import dao.UrlAliasDAO
import models.UrlAlias
import play.api.mvc._
import utils.GeneralUtils._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UrlController @Inject()(controllerComponents: ControllerComponents, urlAliasDao: UrlAliasDAO)
                             (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def create(): Action[AnyContent] = Action.async {
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

  def redirect(alias: String): Action[AnyContent] = Action.async
  {
    val futureO = for {
      urlAlias <- urlAliasDao.findByAlias(alias)
    } yield PermanentRedirect(urlAlias.destinationUrl)

    futureO.toFuture(UrlAliasNotFoundException(alias).toResult)
  }

}
