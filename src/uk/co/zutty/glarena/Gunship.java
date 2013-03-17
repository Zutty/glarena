package uk.co.zutty.glarena;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public static final float SPEED = 0.1f;

    private long timer = 10L;
    private float yawRadians = 0;
    private BillboardList billboardList;
    private Vector4f emitPoint;

    public Gunship(Model model, ShaderProgram shader) {
        super(model, shader);
        emitPoint = new Vector4f(0,0,3.5f,1);
    }

    public void setBillboardList(BillboardList billboardList) {
        this.billboardList = billboardList;
    }

    @Override
    public void update() {
        float dz = 0f;
        float dx = 0f;
        boolean setYaw = false;

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

        if(setYaw) {
            yawRadians = (float)Math.atan2(dx, dz);
            yaw = yawRadians * (180f/(float)Math.PI);
        }

        super.update();

        ++timer;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if(timer >= 6L) {
                timer = 0;
                Vector4f emitPosition = new Vector4f(emitPoint);
                Matrix4f.transform(matrix, emitPosition, emitPosition);

                billboardList.emitFrom(xyz(emitPosition), new Vector3f((float)Math.sin(yawRadians),0,(float)Math.cos(yawRadians)), 0.4f);
            }
        }
    }

    public static Vector3f xyz(Vector4f v) {
        return new Vector3f(v.x, v.y, v.z);
    }
}
