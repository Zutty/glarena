package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public static final float SPEED = .4f;
    public static final float DEAD_ZONE = 0.1f;

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
        if(gamepad.getLeftStick().lengthSquared() > DEAD_ZONE) {
            position.x -= gamepad.getLeftStick().x * SPEED;
            position.z -= gamepad.getLeftStick().y * SPEED;
        }

        Vector2f direction = (gamepad.getRightStick().lengthSquared() > DEAD_ZONE) ? gamepad.getRightStick() : ((gamepad.getLeftStick().lengthSquared() > DEAD_ZONE) ? gamepad.getLeftStick() : null);

        float prevYaw = yaw;

        if(direction != null) {
            yawRadians = (float)Math.atan2(-direction.x, -direction.y);
            yaw = yawRadians * (180f/(float)Math.PI);
        }

        roll = easeAngle(roll, (prevYaw - yaw) * 10f);

        super.update();

        ++timer;
        if (gamepad.getRightStick().lengthSquared() > DEAD_ZONE || gamepad.isButtonDown()) {
            if(timer >= 3L) {
                timer = 0;
                Vector4f emitPosition = new Vector4f((emitAlt = !emitAlt) ? emitPointL : emitPointR);
                Matrix4f.transform(matrix, emitPosition, emitPosition);

                billboardList.emitFrom(xyz(emitPosition), new Vector3f((float)Math.sin(yawRadians), 0, (float)Math.cos(yawRadians)), 1.2f);
            }
        }
    }

    private float easeAngle(float current, float target) {
        return current + delta(target, current) / 10f;
    }

    private float delta(float a, float b) {
        float diff = Math.abs(a - b) % 360f;
        diff = (diff > 180) ? 360f - diff : diff;
        return (a > b) ? diff : -diff;
    }

    public static Vector3f xyz(Vector4f v) {
        return new Vector3f(v.x, v.y, v.z);
    }
}
