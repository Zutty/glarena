#version 330

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec2 position;
in vec2 texCoord;

out hud_vertex {
    vec2 texCoord;
} vertex;

void main() {
    gl_Position = vec4(position.xy, 0, 1);
    vertex.texCoord = texCoord;
}