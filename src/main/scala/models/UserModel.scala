package models

import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}

import java.util.UUID

case class UserModel(
    id: UUID,
    first_name: Option[String],
    last_name: Option[String],
    is_active: Boolean
)

object UserModel {
  implicit val schema: Schema[UserModel]   = DeriveSchema.gen
  implicit val codec: JsonCodec[UserModel] = DeriveJsonCodec.gen
}
