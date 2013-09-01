package uk.co.zutty.glarena.gl;

import uk.co.zutty.glarena.Technique;

public interface Model {
    Technique getTechnique();

    void draw(int mode);

    void destroy();
}
