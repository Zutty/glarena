package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector3f;

/**
 * AI controlled enemy entity.
 */
public class Ufo extends Entity {

    private static final float HIT_RADIUS_SQUARED = 1.8f * 1.8f;

    private BulletEmitter bulletEmitter;
    private int timer = 0;

    public Ufo(Model model, ShaderProgram shader, BulletEmitter bulletEmitter) {
        super(model, shader);
        this.bulletEmitter = bulletEmitter;
    }

    @Override
    public void update() {
        yaw -= 3;

        ++timer;

        position.x += Math.sin((double)timer / (50.0 - ((double)timer/20.0))) * 0.5f;
        position.z += Math.cos((double)timer / (50.0 - ((double)timer/20.0))) * 0.5f;

        for (Particle p : bulletEmitter.particles()) {
            Vector3f.sub(p.getPosition(), position, Arena.V);
            if (Arena.V.lengthSquared() < HIT_RADIUS_SQUARED) {
                game.remove(this);
                p.setLifetime((short)0);
            }
        }

        super.update();
    }
}
