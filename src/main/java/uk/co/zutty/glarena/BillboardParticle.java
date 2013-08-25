package uk.co.zutty.glarena;

import java.nio.FloatBuffer;

public class BillboardParticle extends Particle {

    private float rotation;

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void update() {
        super.update();
        rotation += 0.1f;
    }

    public void put(FloatBuffer buffer) {
        buffer.put(position.x);
        buffer.put(position.y);
        buffer.put(position.z);
        buffer.put(rotation);
    }
}
