package uk.co.zutty.glarena.engine;

public class Tween {

    public interface EasingFunction {
        float apply(float x);
    }

    public enum Easing {
        LINEAR(new EasingFunction() {
            @Override
            public float apply(float x) {
                return x;
            }
        }),
        EXPO_OUT(new EasingFunction() {
            @Override
            public float apply(float x) {
                return (float) Math.pow(2, 10f * (x - 1));
            }
        }),
        QUAD_INOUT(new EasingFunction() {
            @Override
            public float apply(float x) {
                return 1f - (float) Math.pow(1f - (2f * x), 2);
            }
        });

        private EasingFunction f;

        private Easing(EasingFunction f) {
            this.f = f;
        }

        public EasingFunction getFunction() {
            return f;
        }
    }

    private float offset;
    private float range;
    private EasingFunction f;

    public Tween(float from, float to, Easing easing) {
        this.offset = from;
        this.range = to - from;
        f = easing.getFunction();
    }

    public float getValue(float factor) {
        return offset + (f.apply(factor) * range);
    }
}
