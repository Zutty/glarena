#version 330

uniform sampler2D gColorMap;

in bb_fragment {
    vec2 texCoord;
    float fade;
} frag;

out vec4 FragColor;

void main()
{
    vec4 color = texture(gColorMap, frag.texCoord);

    if (color.r == 0 && color.g == 0 && color.b == 0) {
        discard;
    }

    FragColor = vec4(color.rgb, frag.fade);
}