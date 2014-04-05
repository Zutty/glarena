#version 330

uniform sampler2D uTexture;

in hud_vertex {
    vec2 texCoord;
} vertex;

out vec4 fragmentColor;

void main() {
    fragmentColor = texture(uTexture, vertex.texCoord);
}