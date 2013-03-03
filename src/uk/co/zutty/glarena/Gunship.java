package uk.co.zutty.glarena;

/**
 * Player controlled entity.
 */
public class Gunship extends Entity {

    public Gunship(ShaderProgram shader) {
        super(Model.fromMesh(new ObjLoader().loadMesh("/models/gunship.obj"), TextureLoader.loadTexture("/textures/gunship_diffuse.png"), shader));
    }

    @Override
    public void update() {
        yaw += 1;
    }
}
