package models

import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}

case class CatFactsModel(fact: String, length: Int)

object CatFactsModel {
  implicit val schema: Schema[CatFactsModel]   = DeriveSchema.gen
  implicit val codec: JsonCodec[CatFactsModel] = DeriveJsonCodec.gen
}
