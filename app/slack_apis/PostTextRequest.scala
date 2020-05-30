package slack_apis

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, Writes}

case class PostTextRequest(
    channel: String,
    text: String,
    unfurlMedia: Boolean = true
)

object PostTextRequest {
  implicit val config = JsonConfiguration(SnakeCase)
  implicit val writes: Writes[PostTextRequest] = Json.writes[PostTextRequest]
}
