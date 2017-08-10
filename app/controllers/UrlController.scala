package controllers

import javax.inject.{Inject, Singleton}

import controllers.exceptions.{FormValidationException, UrlAliasAlreadyExistException, UrlAliasNotFoundException}
import controllers.requests.ClientUrlAlias
import dao.UrlAliasDAO
import models.UrlAlias
import play.api.libs.json.Json
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
        urlAlias <- ControllerUtils.validateOrAssignAlias(urlAliasDao, alias)
        _ <- urlAliasDao.insert(UrlAlias(destinationUrl, urlAlias, randomUuid()))
      } yield {
        Ok(Json.obj("destinationUrl" -> destinationUrl, "alias" -> urlAlias))
      }
    } recover {
      case formValidationException @ FormValidationException(_) =>
        UnprocessableEntity(formValidationException.toJson)

      case urlAliasAlreadyExistException @ UrlAliasAlreadyExistException(_) =>
        Conflict(urlAliasAlreadyExistException.toJson)
    }
  }

  def redirect(alias: String): Action[AnyContent] = Action.async
  {
    val futureO = for {
      urlAlias <- urlAliasDao.findByAlias(alias)
    } yield PermanentRedirect(urlAlias.destinationUrl)

    futureO.toFuture(NotFound(UrlAliasNotFoundException(alias).toJson))
  }

}
