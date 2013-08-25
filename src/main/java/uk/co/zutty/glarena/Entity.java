package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static uk.co.zutty.glarena.util.MathUtils.degreesToRadians;

/**
 * A game object.
 */
public abstract class Entity {

    protected Vector3f position = new Vector3f();

    protected float roll;
    protected float pitch;
    protected float yaw;

    private ModelInstance modelInstance;

    protected Game game;

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void update() {
        Matrix4f matrix = modelInstance.getMatrix();
        matrix.setIdentity();
        matrix.translate(position);
        matrix.rotate(degreesToRadians(pitch), new Vector3f(1, 0, 0));
        matrix.rotate(degreesToRadians(yaw), new Vector3f(0, 1, 0));
        matrix.rotate(degreesToRadians(roll), new Vector3f(0, 0, 1));
    }

    public void destroy() {
    }
}
