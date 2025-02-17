package exception

import zio.schema.{DeriveSchema, Schema}

object Exceptions {

  case class StatusNotFound(message: String) extends Exception

  object StatusNotFound {
    implicit val schema: Schema[StatusNotFound] = DeriveSchema.gen
  }
}
