package dao

import javax.inject.Inject

import models.UrlAlias
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag

import scala.concurrent.{ExecutionContext, Future}

class UrlAliasDAO @Inject() (@NamedDatabase("url_alias_api") protected val dbConfigProvider: DatabaseConfigProvider)
        (implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]
{
  private val urlAliases = TableQuery[UrlAliasTable]

  def all(): Future[List[UrlAlias]] = db.run(urlAliases.result).map(_.toList)

  def insert(urlAlias: UrlAlias): Future[Int] = db.run(urlAliases += urlAlias)

  private class UrlAliasTable(tag: Tag) extends Table[UrlAlias](tag, "url_alias")
  {
    def destinationUrl = column[String]("destination_url")

    def alias = column[String]("alias")

    def id = column[String]("id")

    def * = (destinationUrl, alias, id) <> (UrlAlias.tupled, UrlAlias.unapply)
  }
}
