package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.gl.Shader;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.gl.ShaderType;
import uk.co.zutty.glarena.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.*;

public class BulletTechnique implements Technique {

    private ShaderProgram shader;
    private VertexFormat format;
    private Matrix4f projectionMatrix;
    private Camera camera;

    public BulletTechnique() {
        shader = new ShaderProgram();

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

        format = VertexFormat.Builder.format()
                .withAttribute(3)
                .withAttribute(3)
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
        glDisable(GL_CULL_FACE);

        Matrix4f viewProjectionMatrix = new Matrix4f();
        Matrix4f.mul(projectionMatrix, camera.getViewMatrix(), viewProjectionMatrix);

        shader.use();
        shader.setUniform("gVP", viewProjectionMatrix);

        instance.getTexture().bind();

        instance.getModel().draw(GL_POINTS);

        ShaderProgram.useNone();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
}
