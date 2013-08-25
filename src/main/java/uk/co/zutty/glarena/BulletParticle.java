package uk.co.zutty.glarena;

import java.nio.FloatBuffer;

public class BulletParticle extends Particle {
    public void put(FloatBuffer buffer) {
        buffer.put(position.x);
        buffer.put(position.y);
        buffer.put(position.z);

        buffer.put(velocity.x);
        buffer.put(velocity.y);
        buffer.put(velocity.z);
    }
}
