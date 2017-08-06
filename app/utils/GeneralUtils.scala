package utils

import java.util.UUID

object GeneralUtils
{
  def randomUuid(): String = UUID.randomUUID().toString
}
