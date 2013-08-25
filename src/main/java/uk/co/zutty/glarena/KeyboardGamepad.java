package uk.co.zutty.glarena;

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

        if(leftAxis.lengthSquared() != 0) leftAxis.normalise();
        if(rightAxis.lengthSquared() != 0) rightAxis.normalise();
    }
}
