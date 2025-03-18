package exception

import zio.schema.{DeriveSchema, Schema}

sealed trait AppError

object AppError {

  case class UserNotFoundException(message: String)     extends AppError
  case class InternalDatabaseException(message: String) extends AppError
  case class BodyParsingException(message: String)      extends AppError
  case class RequestExecutionException(message: String) extends AppError

  object UserNotFoundException {
    implicit val schema: Schema[UserNotFoundException] = DeriveSchema.gen
  }

  object InternalDatabaseException {
    implicit val schema: Schema[InternalDatabaseException] = DeriveSchema.gen
  }

  object BodyParsingException {
    implicit val schema: Schema[BodyParsingException] = DeriveSchema.gen
  }

  object RequestExecutionException {
    implicit val schema: Schema[RequestExecutionException] = DeriveSchema.gen
  }
}
