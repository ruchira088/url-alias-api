package controllers

import controllers.exceptions.UrlAliasAlreadyExistException
import dao.UrlAliasDAO
import utils.GeneralUtils

import scala.concurrent.{ExecutionContext, Future}

object ControllerUtils
{
  private def assignAlias(urlAliasDAO: UrlAliasDAO, aliasLength: Int)(implicit executionContext: ExecutionContext): Future[String] =
  {
    val urlAlias = GeneralUtils.randomString(aliasLength)

    urlAliasDAO.exists(urlAlias)
      .flatMap {
        case true => assignAlias(urlAliasDAO, aliasLength)
        case _ => Future.successful(urlAlias)
      }
  }

  def validateOrAssignAlias(urlAliasDAO: UrlAliasDAO, aliasOption: Option[String], autoUrlAliasLength: Int)
                           (implicit executionContext: ExecutionContext): Future[String] =
    aliasOption match {
      case Some(alias) =>
        urlAliasDAO.exists(alias)
          .flatMap {
            case true => Future.failed(UrlAliasAlreadyExistException(alias))
            case _ => Future.successful(alias)
          }
      case None => assignAlias(urlAliasDAO, autoUrlAliasLength)
    }

}
