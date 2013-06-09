package uk.co.zutty.glarena;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public static final float SPEED = .4f;

    private long timer = 10L;
    private float yawRadians = 0;
    private BillboardList billboardList;
    private Vector4f emitPointL;
    private Vector4f emitPointR;
    private boolean emitAlt;
    private Gamepad gamepad;

    public Gunship(Model model, ShaderProgram shader) {
        super(model, shader);
        emitPointL = new Vector4f(0.7f,0,2.9f,1);
        emitPointR = new Vector4f(-0.7f,0,2.9f,1);
        emitAlt = true;
    }

    public void setGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void setBillboardList(BillboardList billboardList) {
        this.billboardList = billboardList;
    }

    @Override
    public void update() {
        float dz = 0f;
        float dx = 0f;
        boolean setYaw = false;

        /*
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z += SPEED;
            dz = 1;
            setYaw = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z -= SPEED;
            dz = -1;
            setYaw = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x += SPEED;
            dx = 1;
            setYaw = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x -= SPEED;
            dx = -1;
            setYaw = true;
        }
        */

        if(gamepad.getLeftStick().lengthSquared() > 0.04f) {
            position.x -= gamepad.getLeftStick().x * SPEED;
            position.z -= gamepad.getLeftStick().y * SPEED;
        }

        //position.x += Math.sin(yawRadians) * gamepad.getRightTrigger() * SPEED;
        //position.z += Math.cos(yawRadians) * gamepad.getRightTrigger() * SPEED;

        Vector2f direction = (gamepad.getRightStick().lengthSquared() > 0.04f) ? gamepad.getRightStick() : gamepad.getLeftStick();

        yawRadians = (float)Math.atan2(-direction.x, -direction.y);
        yaw = yawRadians * (180f/(float)Math.PI);

        super.update();

        ++timer;
        if (gamepad.getRightTrigger() > 0f) {
            if(timer >= 3L) {
                timer = 0;
                Vector4f emitPosition = new Vector4f((emitAlt = !emitAlt) ? emitPointL : emitPointR);
                Matrix4f.transform(matrix, emitPosition, emitPosition);

                billboardList.emitFrom(xyz(emitPosition), new Vector3f((float)Math.sin(yawRadians), 0, (float)Math.cos(yawRadians)), 1.2f);
            }
        }
    }

    public static Vector3f xyz(Vector4f v) {
        return new Vector3f(v.x, v.y, v.z);
    }
}
