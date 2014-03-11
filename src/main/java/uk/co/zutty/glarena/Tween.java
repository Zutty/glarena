package uk.co.zutty.glarena;

public class Tween {

    private float offset;
    private float range;

    public Tween(float from, float to) {
        this.offset = from;
        this.range = to - from;
    }

    public float getValue(float factor) {
        return offset + (factor * range);
    }
}
