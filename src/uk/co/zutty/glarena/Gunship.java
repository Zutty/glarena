package uk.co.zutty.glarena;

import org.lwjgl.input.Keyboard;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public static final float SPEED = 0.1f;

    public Gunship(Model model, ShaderProgram shader) {
        super(model, shader);
    }

    @Override
    public void update() {
        float dz = 0f;
        float dx = 0f;
        boolean setYaw = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            z += SPEED;
            dz = 1;
            setYaw = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            z -= SPEED;
            dz = -1;
            setYaw = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            x += SPEED;
            dx = 1;
            setYaw = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            x -= SPEED;
            dx = -1;
            setYaw = true;
        }

        if(setYaw) {
            yaw = (float)(Math.atan2(dx, dz) * (180/Math.PI));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            y -= SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            y += SPEED;
        }

        super.update();
    }
}
