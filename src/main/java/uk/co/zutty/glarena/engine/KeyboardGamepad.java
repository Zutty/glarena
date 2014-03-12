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

package uk.co.zutty.glarena.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

/**
 * Psuedo-gamepad that actually uses keyboard input
 * TODO abstract this out properly
 */
public class KeyboardGamepad extends Gamepad {

    private Vector2f leftAxis = new Vector2f();
    private Vector2f rightAxis = new Vector2f();

    @Override
    public boolean isButtonDown() {
        return false;
    }

    @Override
    public Vector2f getLeftStick() {
        return leftAxis;
    }

    @Override
    public Vector2f getRightStick() {
        return rightAxis;
    }

    @Override
    public float getRightTrigger() {
        return 0f;
    }

    @Override
    public void update() {
        leftAxis.set(0, 0);
        rightAxis.set(0, 0);

        // Left
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            leftAxis.y -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            leftAxis.y += 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            leftAxis.x -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            leftAxis.x += 1;
        }

        // Right
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            rightAxis.y -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            rightAxis.y += 1;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            rightAxis.x -= 1;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            rightAxis.x += 1;
        }

        if (leftAxis.lengthSquared() != 0) leftAxis.normalise();
        if (rightAxis.lengthSquared() != 0) rightAxis.normalise();
    }
}
