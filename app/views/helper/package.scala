package views

import play.api.i18n.{Messages, Lang}
import play.api.templates.Html
import play.mvc.Http
import models.Room
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}

/**
 * <p>
 * Represents helper methods for {@link views.html} views.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

package object helper {

  val CollectedJSContextKey = "collectedJS"
  val dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY hh:mm")

  /**
   * class to generate js from scala views in the end of body tag
   */

  def collectJS(script:Html) {
    val collected = Option(Http.Context.current().args.get(CollectedJSContextKey).asInstanceOf[Html]) getOrElse Html("")
    play.mvc.Http.Context.current().args.put(CollectedJSContextKey, script += collected)
  }

  def collectedJS(): Html = {
    Http.Context.current().args.get(CollectedJSContextKey).asInstanceOf[Html]
  }

  def getRoomIdForJS(room: Room):String = {
    return "room-"+room.getId;
  }
}
