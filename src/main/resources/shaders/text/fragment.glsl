#version 330

uniform sampler2D uTexture;

in font_vertex {
    vec2 texCoord;
    vec3 colour;
} vertex;

out vec4 fragmentColor;

void main() {
    float alpha = texture(uTexture, vertex.texCoord).r;
    if(alpha == 0) {
        discard;
    }
    fragmentColor = vec4(vertex.colour, alpha);
}