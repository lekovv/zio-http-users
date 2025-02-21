package models

import zio.schema.{DeriveSchema, Schema}

case class StatusModel(id: String, description: Option[String], active: Boolean)

object StatusModel {
  implicit val schema: Schema[StatusModel] = DeriveSchema.gen
}
