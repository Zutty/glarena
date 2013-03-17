#version 150

in vec4 in_Position;
in vec3 in_velocity;

out vec3 pass_velocity;

void main()
{
    gl_Position = in_Position;
    pass_velocity = in_velocity;
}