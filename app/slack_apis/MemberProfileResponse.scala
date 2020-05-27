package slack_apis
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{JsonConfiguration, Reads, Json}

case class MemberProfileResponse(profile: Profile) {
  override def toString: String = {
    s"${profile.displayName}(${profile.realName})\n${profile.image_48}"
  }
}

object MemberProfileResponse {
  implicit val reads: Reads[MemberProfileResponse] =
    Json.reads[MemberProfileResponse]
}

case class Profile(displayName: String, realName: String, image_48: String)

object Profile {
  implicit val config = JsonConfiguration(SnakeCase)
  implicit val reads: Reads[Profile] = Json.reads[Profile]
}
