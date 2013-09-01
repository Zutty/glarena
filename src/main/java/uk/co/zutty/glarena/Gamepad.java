package uk.co.zutty.glarena;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.lwjgl.util.vector.Vector2f;

/**
 * Wrapper for JInput controller.
 */
public class Gamepad {

    private Controller controller;

    private Vector2f leftStick;
    private Vector2f rightStick;

    private Component leftStickXAxis;
    private Component leftStickYAxis;
    private Component rightStickXAxis;
    private Component rightStickYAxis;
    private Component aButton;
    private Component rightTrigger;

    public Gamepad(Controller controller) {
        this.controller = controller;
        leftStick = new Vector2f();
        rightStick = new Vector2f();

        leftStickXAxis = controller.getComponent(Component.Identifier.Axis.X);
        leftStickYAxis = controller.getComponent(Component.Identifier.Axis.Y);

        rightStickXAxis = controller.getComponent(Component.Identifier.Axis.RX);
        rightStickYAxis = controller.getComponent(Component.Identifier.Axis.RY);

        aButton = controller.getComponent(Component.Identifier.Button._0);

        rightTrigger = controller.getComponent(Component.Identifier.Axis.RZ);
    }

    protected Gamepad() {
    }

    public boolean isButtonDown() {
        return aButton.getPollData() != 0;
    }

    public Vector2f getLeftStick() {
        return leftStick;
    }

    public Vector2f getRightStick() {
        return rightStick;
    }

    public float getRightTrigger() {
        return (rightTrigger.getPollData() + 1f) / 2f;
    }

    public void update() {
        controller.poll();

        leftStick.x = leftStickXAxis.getPollData();
        leftStick.y = leftStickYAxis.getPollData();

        rightStick.x = rightStickXAxis.getPollData();
        rightStick.y = rightStickYAxis.getPollData();
    }
}
