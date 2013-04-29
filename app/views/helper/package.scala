package views

import play.api.i18n.{Messages, Lang}
import play.api.templates.Html
import play.mvc.Http

/**
 * <p>
 * Represents helper methods for {@link views.html} views.
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */

package object helper {

  val CollectedJSContextKey = "collectedJS"

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
}
