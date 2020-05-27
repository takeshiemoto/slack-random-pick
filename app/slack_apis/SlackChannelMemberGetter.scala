package slack_apis

import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class SlackChannelMemberGetter(val slackApiToken: String, val ws: WSClient)
    extends SlackGettable
    with SlackResponseJsonParsable {

  def getMemberIdsBy(channelId: String): Future[MembersResponse] = {
    val requestParamsMap = Map("channel" -> channelId)
    get("conversations.members", slackApiToken, requestParamsMap, ws).flatMap {
      response =>
        Future.fromTry(
          parse[MembersResponse](response.json)
        )
    }
  }

  def getProfile(memberId: String): Future[MemberProfileResponse] = {
    val requestParamMap = Map("user" -> memberId)
    get("users.profile.get", slackApiToken, requestParamMap, ws).flatMap {
      response =>
        Future.fromTry(
          parse[MemberProfileResponse](response.json)
        )
    }
  }
}
