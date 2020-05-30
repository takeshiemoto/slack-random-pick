package slack_apis

import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class SlackChannelMemberGetter(val slackApiToken: String, val ws: WSClient)
    extends SlackGettable
    with SlackResponseJsonParsable {
  def getMemberIdsBy(channelId: String): Future[MembersResponse] = {
    val requestParamMap = Map("channel" -> channelId)
    get("conversations.members", slackApiToken, requestParamMap, ws).flatMap {
      response =>
        Future.fromTry(parse[MembersResponse](response.json))
    }
  }

  def getProfile(memberId: String): Future[MemberProfileResponse] = {
    val requestParamMap = Map("user" -> memberId)
    get("users.profile.get", slackApiToken, requestParamMap, ws).flatMap {
      response =>
        Future.fromTry(parse[MemberProfileResponse](response.json))
    }
  }
}
