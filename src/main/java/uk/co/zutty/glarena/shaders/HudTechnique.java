/*
 * Copyright (c) 2014 George Weller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package uk.co.zutty.glarena.shaders;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.engine.Camera;
import uk.co.zutty.glarena.engine.ModelInstance;
import uk.co.zutty.glarena.engine.Technique;
import uk.co.zutty.glarena.gl.Shader;
import uk.co.zutty.glarena.gl.ShaderProgram;
import uk.co.zutty.glarena.gl.enums.ShaderType;
import uk.co.zutty.glarena.vertex.VertexFormat;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static uk.co.zutty.glarena.vertex.VertexFormat.Builder.format;

public class HudTechnique implements Technique {

    private ShaderProgram shader;
    private VertexFormat format;

    public HudTechnique() {
        shader = new ShaderProgram();

        Shader vertexShader = new Shader(ShaderType.VERTEX);
        vertexShader.loadSource("/shaders/hud/vertex.glsl");
        vertexShader.compile();
        shader.attachShader(vertexShader);

        Shader fragmentShader = new Shader(ShaderType.FRAGMENT);
        fragmentShader.loadSource("/shaders/hud/fragment.glsl");
        fragmentShader.compile();
        shader.attachShader(fragmentShader);

        shader.bindAttribLocation(0, "position");
        shader.bindAttribLocation(1, "texCoord");

        shader.link();
        shader.validate();

        format = format()
                .withAttribute(2)
                .withAttribute(2)
                .build();
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    @Override
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
    }

    @Override
    public void setCamera(Camera camera) {
    }

    @Override
    public void renderInstance(ModelInstance instance) {
        shader.use();

        instance.getTexture().bind();

        instance.getModel().draw(GL_TRIANGLE_STRIP);

        ShaderProgram.useNone();
    }
}
