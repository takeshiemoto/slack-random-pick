package slack_apis

import play.api.libs.json.{Json, Reads}

case class MembersResponse(members: Seq[String])

object MembersResponse {
  implicit val reads: Reads[MembersResponse] = Json.reads[MembersResponse]
}
