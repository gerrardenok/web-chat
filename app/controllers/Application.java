package controllers;

import play.*;
import play.mvc.*;

import views.html.*;


/**
 * <p>
 * Main controller
 * </p>
 *
 * @author Mikhail Vatalev(m.vatalev@euroats.com)
 */


public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
}
