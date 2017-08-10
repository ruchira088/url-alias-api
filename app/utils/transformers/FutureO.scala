package utils.transformers

import play.mvc.BodyParser.Empty

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

case class FutureO[+A](future: Future[Option[A]])
{
  def flatMap[B](f: A => FutureO[B])(implicit executionContext: ExecutionContext): FutureO[B] =
  {
    FutureO {
      future.flatMap {
        case Some(value) => f(value).future
        case None => Future.successful(None)
      }
    }
  }

  def map[B](f: A => B)(implicit executionContext: ExecutionContext): FutureO[B] =
    FutureO(future.map(option => option.map(f)))

  private def onSuccess(condition: Option[_] => Boolean)(f: => Unit)(implicit executionContext: ExecutionContext): Unit =
  {
    future.onComplete {
      case Success(option) if condition(option) => f
      case _ => Unit
    }
  }

  def onEmpty(f: Unit)(implicit executionContext: ExecutionContext): Unit = onSuccess(_.isEmpty)(f)

  def onSuccessfulValue(f: Unit)(implicit executionContext: ExecutionContext): Unit = onSuccess(_.nonEmpty)(f)

  def toFuture[B >: A](empty: => B)(implicit executionContext: ExecutionContext): Future[B] =
    future.map {
      case Some(value) => value
      case _ => empty
    }

}
