package uk.co.zutty.glarena;

import uk.co.zutty.glarena.gl.Shader;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.vertex.VertexFormat;

/**
 * A collection of billboarded particles.
 */
public class BulletEmitter extends Emitter {

    public BulletEmitter(String texFilename) {
        glTexture = TextureLoader.loadTexture(texFilename);

        shader = new ShaderProgram();

        Shader vertexShader = new Shader(Shader.Type.VERTEX);
        vertexShader.loadSource("/shaders/bullet/vertex.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader geometryShader = new Shader(Shader.Type.GEOMETRY);
        geometryShader.loadSource("/shaders/bullet/geometry.glsl");
        geometryShader.compile();
        shader.attachShader(geometryShader);

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT);
        fragmentShader.loadSource("/shaders/bullet/fragment.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "in_velocity");

        shader.link();
        shader.validate();

        shader.initUniform("gVP");

        init(VertexFormat.Builder.format()
                .withAttribute(3)
                .withAttribute(3)
                .build());
    }

    @Override
    protected Particle newParticle() {
        return new BulletParticle();
    }
}