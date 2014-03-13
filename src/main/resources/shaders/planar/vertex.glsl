#version 330

in vec4 position;
in float rotation;
in float scale;
in float fade;

out bb_vertex {
    vec4 position;
    float rotation;
    float scale;
    float fade;
} pass;

void main()
{
    pass.position = position;
    pass.rotation = rotation;
    pass.scale = scale;
    pass.fade = fade;
}