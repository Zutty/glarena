#version 330
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec2 position;

smooth out vec3 eyeDirection;

void main() {
    vec4 vertex = vec4(position.xy, 0, 1);

    mat4 inverseProjection = inverse(projectionMatrix);

    mat4 r = viewMatrix;
    r[3][0] = 0.0;
    r[3][1] = 0.0;
    r[3][2] = 0.0;

    mat4 inverseModelview = inverse(r);
    vec4 unprojected = inverseProjection * vertex;
    eyeDirection = (inverseModelview * unprojected).xyz;



    gl_Position = vertex;
} 