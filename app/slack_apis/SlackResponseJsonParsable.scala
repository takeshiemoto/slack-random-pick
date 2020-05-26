package slack_apis

import play.api.libs.json.{JsError, JsSuccess, JsValue, Json, Reads}
import scala.util.{Success, Failure, Try}
import slack_apis.errors.InvalidParamException

trait SlackResponseJsonParsable {
  def parse[A](responseJson: JsValue)(implicit reads: Reads[A]): Try[A] = {
    val ok = (responseJson \ "OK").as[Boolean]
    if (ok) {
      Json.fromJson[A](responseJson) match {
        case JsSuccess(result, _) =>
          Success(result)
        case _: JsError =>
          Failure(new RuntimeException("Failure to parse JSON"))
      }
    } else {
      Json.fromJson[SlackErrorResponse](responseJson) match {
        case JsSuccess(slackErrorResponse, _) =>
          Failure(
            InvalidParamException(slackErrorResponse.error)
          )
        case _: JsError =>
          Failure(
            new RuntimeException(
              "Slack API response is something wrong..."
            )
          )
      }
    }
  }
}
