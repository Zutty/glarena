package uk.co.zutty.glarena.util;

/**
 * Created with IntelliJ IDEA.
 */
public class MathUtils {

    public static final float DEGTORAD = (float) (Math.PI / 180d);

    public static float cot(float angle) {
        return (float) (1f / Math.tan(angle));
    }

    public static float degreesToRadians(float degrees) {
        return degrees * DEGTORAD;
    }
}
