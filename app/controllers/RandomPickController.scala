package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents
}
import slack_apis.errors.InvalidParamException
import slack_apis.{SlackChannelMemberGetter, SlackMemberPoster}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.Random

class RandomPickController @Inject()(cc: ControllerComponents, ws: WSClient)
    extends AbstractController(cc) {
  def execute(
      slackApiToken: String,
      channelId: String,
      pickUpNum: Int
  ): Action[AnyContent] =
    Action.async { request =>
      if (pickUpNum <= 0) {
        Future.successful(
          BadRequest(Json.obj("error" -> "pickUpNum must be greater than 0"))
        )
      } else {
        val channelMemberGetter =
          new SlackChannelMemberGetter(slackApiToken, ws)
        val postedMessage = for {
          memberResults <- channelMemberGetter.getMemberIdsBy(channelId)
          pickedUpMemberIds = Random
            .shuffle(memberResults.members)
            .take(pickUpNum)
          pickedUpMembers <- Future.sequence(
            pickedUpMemberIds.map(channelMemberGetter.getProfile)
          ) // (注:1)
          memberPoster = new SlackMemberPoster(slackApiToken, ws)
          response <- memberPoster
            .postMemberToChannel(channelId, pickedUpMembers)
        } yield {
          response
        }
        postedMessage.map { response =>
          Ok(Json.obj("text" -> response.message.text))
        }.recover {
          case InvalidParamException(message) =>
            BadRequest(Json.obj("error" -> message))
          case e: Exception =>
            InternalServerError(Json.obj("error" -> e.getMessage))
        }
      }
    }
}
