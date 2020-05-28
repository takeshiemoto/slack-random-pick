package slack_apis

import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class SlackMemberPoster(val slackApiToken: String, val ws: WSClient)
    extends SlackPostable
    with SlackResponseJsonParsable {
  def postMemberToChannel(
      channelId: String,
      members: Seq[MemberProfileResponse]): Future[PostedMessageResponse] = {
    val text = members.map(_.toString).mkString("\n\n")
    val requestParamJson = Json.toJson(PostTextRequest(channelId, text))
    post("chat.postMessage", slackApiToken, requestParamJson, ws).flatMap {
      response =>
        Future.fromTry(parse[PostedMessageResponse](requestParamJson))
    }
  }
}
