#version 150

uniform sampler2D gColorMap;

in vec2 TexCoord;
out vec4 FragColor;

void main()
{
    FragColor = texture(gColorMap, TexCoord);

    if (FragColor.r == 0 && FragColor.g == 0 && FragColor.b == 0) {
        discard;
    }
}