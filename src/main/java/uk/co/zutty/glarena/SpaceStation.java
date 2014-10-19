package uk.co.zutty.glarena;

import uk.co.zutty.glarena.engine.AbstractEntity;
import uk.co.zutty.glarena.engine.ModelInstance;

public class SpaceStation extends AbstractEntity {

    public SpaceStation(ModelInstance modelInstance) {
        setModelInstance(modelInstance);

        roll = pitch = yaw = -25;
    }

    @Override
    public void update() {
        //++yaw;
        super.update();
    }
}
