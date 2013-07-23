package uk.co.zutty.glarena;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Zutty
 * Date: 21/07/2013
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
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
