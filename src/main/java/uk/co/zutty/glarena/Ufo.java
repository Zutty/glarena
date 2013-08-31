package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Vector3f;

/**
 * AI controlled enemy entity.
 */
public class Ufo extends AbstractEntity {

    private static final float HIT_RADIUS_SQUARED = 1.8f * 1.8f;
    protected Game game;

    private Emitter playerBulletEmitter;
    private int timer = 0;

    public Ufo(ModelInstance modelInstance, Emitter playerBulletEmitter) {
        setModelInstance(modelInstance);
        this.playerBulletEmitter = playerBulletEmitter;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void update() {
        yaw -= 3;

        ++timer;

        position.x += Math.sin((double)timer / (50.0 - ((double)timer/20.0))) * 0.5f;
        position.z += Math.cos((double)timer / (50.0 - ((double)timer/20.0))) * 0.5f;

        for (Particle p : playerBulletEmitter.particles()) {
            Vector3f.sub(p.getPosition(), position, Arena.V);
            if (Arena.V.lengthSquared() < HIT_RADIUS_SQUARED) {
                game.remove(this);
                p.setLifetime((short)0);
            }
        }

        super.update();
    }
}
