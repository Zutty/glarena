/*
 * Copyright (c) 2013 George Weller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
