#version 150

in vec4 position;
in vec3 velocity;
in float scale;
in float fade;

out bullet_vertex {
    vec4 position;
    vec3 velocity;
    float scale;
    float fade;
} vertex;

void main()
{
    vertex.position = position;
    vertex.velocity = velocity;
    vertex.scale = scale;
    vertex.fade = fade;
}