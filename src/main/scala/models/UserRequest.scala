package models

import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}

case class UserRequest(
    first_name: Option[String],
    last_name: Option[String],
    is_active: Boolean
)

object UserRequest {
  implicit val schema: Schema[UserRequest] = DeriveSchema.gen
  implicit val codec: JsonCodec[UserRequest] = DeriveJsonCodec.gen
}
