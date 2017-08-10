package controllers

import controllers.exceptions.UrlAliasAlreadyExistException
import dao.UrlAliasDAO
import utils.GeneralUtils

import scala.concurrent.{ExecutionContext, Future}

object ControllerUtils
{
  private def assignAlias(urlAliasDAO: UrlAliasDAO)(implicit executionContext: ExecutionContext): Future[String] =
  {
    val urlAlias = GeneralUtils.randomString(5)

    urlAliasDAO.exists(urlAlias)
      .flatMap {
        case true => assignAlias(urlAliasDAO)
        case _ => Future.successful(urlAlias)
      }
  }

  def validateOrAssignAlias(urlAliasDAO: UrlAliasDAO, aliasOption: Option[String])
                           (implicit executionContext: ExecutionContext): Future[String] =
    aliasOption match {
      case Some(alias) =>
        urlAliasDAO.exists(alias)
          .flatMap {
            case true => Future.failed(UrlAliasAlreadyExistException(alias))
            case _ => Future.successful(alias)
          }
      case None => assignAlias(urlAliasDAO)
    }

}
