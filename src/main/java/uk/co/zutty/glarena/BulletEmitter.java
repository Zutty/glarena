package uk.co.zutty.glarena;

import uk.co.zutty.glarena.gl.Shader;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.gl.ShaderType;
import uk.co.zutty.glarena.vertex.VertexFormat;

/**
 * A collection of billboarded particles.
 */
public class BulletEmitter extends Emitter {

    public BulletEmitter(int glTexture) {
        super(new BulletTechnique(), glTexture, BulletParticle.class);
    }

    private static ShaderProgram makeShader() {
        ShaderProgram shader = new ShaderProgram();

        Shader vertexShader = new Shader(ShaderType.VERTEX);
        vertexShader.loadSource("/shaders/bullet/vertex.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader geometryShader = new Shader(ShaderType.GEOMETRY);
        geometryShader.loadSource("/shaders/bullet/geometry.glsl");
        geometryShader.compile();
        shader.attachShader(geometryShader);

        Shader fragmentShader = new Shader(ShaderType.FRAGMENT);
        fragmentShader.loadSource("/shaders/bullet/fragment.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "in_velocity");

        shader.link();
        shader.validate();

        shader.initUniform("gVP");

        return shader;
    }
}