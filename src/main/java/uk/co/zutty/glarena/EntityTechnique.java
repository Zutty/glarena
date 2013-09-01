package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.gl.Shader;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.gl.ShaderType;
import uk.co.zutty.glarena.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static uk.co.zutty.glarena.vertex.VertexFormat.Builder.format;

public class EntityTechnique implements Technique {

    private ShaderProgram shader;
    private VertexFormat format;
    private Matrix4f projectionMatrix;
    private Camera camera;

    public EntityTechnique() {
        shader = new ShaderProgram();

        Shader vertexShader = new Shader(ShaderType.VERTEX);
        vertexShader.loadSource("/shaders/vec.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader fragmentShader = new Shader(ShaderType.FRAGMENT);
        fragmentShader.loadSource("/shaders/frag.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.bindAttribLocation(0, "in_Position");
        shader.bindAttribLocation(1, "foobar");
        shader.bindAttribLocation(2, "in_TextureCoord");

        shader.link();
        shader.validate();

        // Get matrices uniform locations
        shader.initUniform("projectionMatrix");
        shader.initUniform("viewMatrix");
        shader.initUniform("modelMatrix");

        format = format()
                .withAttribute(3)
                .withAttribute(3)
                .withAttribute(2)
                .build();
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    @Override
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void renderInstance(ModelInstance instance) {
        shader.use();

        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", camera.getViewMatrix());
        shader.setUniform("modelMatrix", instance.getMatrix());

        instance.getTexture().bind();

        instance.getModel().draw(GL_TRIANGLES);

        ShaderProgram.useNone();
    }
}
