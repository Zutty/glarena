#version 150

in vec4 in_Position;
in float in_rotation;

out float pass_rotation;

void main()
{
    gl_Position = in_Position;
    pass_rotation = in_rotation;
}