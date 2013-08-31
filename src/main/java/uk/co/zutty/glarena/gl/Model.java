package uk.co.zutty.glarena.gl;

import uk.co.zutty.glarena.Technique;

import java.nio.FloatBuffer;

public interface Model {
    Technique getTechnique();

    void draw(int mode);

    void destroy();
}
