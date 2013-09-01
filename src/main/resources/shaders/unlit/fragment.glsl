#version 150

uniform sampler2D gColorMap;

in vec2 pass_TextureCoord;
out vec4 out_FragColor;

void main()
{
    out_FragColor = texture(gColorMap, pass_TextureCoord);

    if (out_FragColor.r == 0 && out_FragColor.g == 0 && out_FragColor.b == 0) {
        discard;
    }
}