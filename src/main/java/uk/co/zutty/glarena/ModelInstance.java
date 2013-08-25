package uk.co.zutty.glarena;

import org.lwjgl.util.vector.Matrix4f;
import uk.co.zutty.glarena.gl.Model;

public class ModelInstance {

    private Model model;
    private Matrix4f matrix = new Matrix4f();

    public ModelInstance(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }
}
