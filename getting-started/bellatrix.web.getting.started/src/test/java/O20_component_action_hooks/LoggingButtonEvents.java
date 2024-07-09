package O20_component_action_hooks;

import solutions.bellatrix.web.components.Button;
import solutions.bellatrix.core.utilities.Log;

public class LoggingButtonEvents {
    // CLICKING - an event executed before button click
    // CLICKED - an event executed after the button is clicked

    // In the example, Logger is called
    // for each button event printing to the console the coordinates of the button. You can call external logging provider,
    // making screenshots before or after each action, the possibilities are limitless.

    public static void addEventListeners() {
        Button.CLICKING.addListener(arg -> Log.info(
                "before clicking button; coordinates: x=%d y=%d",
                arg.getComponent().getWrappedElement().getLocation().getX(),
                arg.getComponent().getWrappedElement().getLocation().getY()
        ));

        Button.CLICKED.addListener(arg -> Log.info(
                "after button clicked; coordinates: x=%d y=%d",
                arg.getComponent().getWrappedElement().getLocation().getX(),
                arg.getComponent().getWrappedElement().getLocation().getY()
        ));
    }
}