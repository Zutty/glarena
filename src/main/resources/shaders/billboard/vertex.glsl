#version 330


in vec4 position;
in float rotation;
in float scale;
in float fade;

out bb_vertex {
    float rotation;
    float scale;
    float fade;
} pass;

void main()
{
    gl_Position = position;
    pass.rotation = rotation;
    pass.scale = scale;
    pass.fade = fade;
}