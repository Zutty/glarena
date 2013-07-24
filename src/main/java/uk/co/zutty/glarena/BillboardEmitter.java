package uk.co.zutty.glarena;

import uk.co.zutty.glarena.vertex.VertexFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Zutty
 * Date: 21/07/2013
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class BillboardEmitter extends Emitter {

    private Camera camera;

    public BillboardEmitter(String texFilename, Camera camera) {
        this.camera = camera;

        glTexture = TextureLoader.loadTexture(texFilename);

        shader = new ShaderProgram();

        Shader vertexShader = new Shader(Shader.Type.VERTEX);
        vertexShader.loadSource("/shaders/billboard/vertex.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader geometryShader = new Shader(Shader.Type.GEOMETRY);
        geometryShader.loadSource("/shaders/billboard/geometry.glsl");
        geometryShader.compile();
        shader.attachShader(geometryShader);

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT);
        fragmentShader.loadSource("/shaders/billboard/fragment.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "in_rotation");

        shader.link();
        shader.validate();

        shader.initUniform("gVP");
        shader.initUniform("gCameraPos");
        shader.initUniform("gCameraCenter");

        init(VertexFormat.Builder.format()
                .withAttribute(3)
                .withAttribute(1)
                .build());
    }

    @Override
    protected Particle newParticle() {
        return new BillboardParticle();
    }

    @Override
    protected void initUniforms(ShaderProgram shader) {
        shader.setUniform("gCameraPos", camera.getPosition());
        shader.setUniform("gCameraCenter", camera.getCenter());
    }
}
