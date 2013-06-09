package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
* Representation of a single particle, both the logical game entity and VBO format for OpenGL.
*/
public class Particle {

    private static final int FLOAT_BYTES = 4;
    public static final int POSITION_ELEMENTS = 3;
    public static final int VELOCITY_ELEMENTS = 3;
    public static final int ELEMENTS = POSITION_ELEMENTS + VELOCITY_ELEMENTS;

    public static final int POSITION_BYTES = POSITION_ELEMENTS * FLOAT_BYTES;
    public static final int VELOCITY_BYTES = VELOCITY_ELEMENTS * FLOAT_BYTES;

    public static final int POSITION_OFFSET = 0;
    public static final int VELOCITY_OFFSET = POSITION_OFFSET + POSITION_BYTES;

    public static final int STRIDE = POSITION_BYTES + VELOCITY_BYTES;

    private Vector3f position;
    private Vector3f velocity;
    private short lifetime;

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public short getLifetime() {
        return lifetime;
    }

    public void setLifetime(short lifetime) {
        this.lifetime = lifetime;
    }

    public boolean isDead() {
        return lifetime <= 0;
    }

    public void update() {
        --lifetime;
        Vector3f.add(position, velocity, position);
    }

    public void put(FloatBuffer buffer) {
        buffer.put(position.x);
        buffer.put(position.y);
        buffer.put(position.z);

        buffer.put(velocity.x);
        buffer.put(velocity.y);
        buffer.put(velocity.z);
    }
}
