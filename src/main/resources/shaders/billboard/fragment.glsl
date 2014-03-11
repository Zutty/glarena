#version 150

uniform sampler2D gColorMap;

in bb_fragment {
    vec2 texCoord;
    float fade;
} frag;

out vec4 FragColor;

void main()
{
    FragColor = texture(gColorMap, frag.texCoord);

    if (FragColor.r == 0 && FragColor.g == 0 && FragColor.b == 0) {
        discard;
    }

    FragColor *= frag.fade;
}