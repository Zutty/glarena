#version 150

uniform sampler2D gColorMap;

in bullet_fragment {
    vec2 texCoord;
    float fade;
} frag;

out vec4 FragColor;

void main()
{
    vec4 color = texture(gColorMap, frag.texCoord);

    if (color.a == 0) {
        discard;
    }

    FragColor = vec4(color.rgb, color.a * frag.fade);
}