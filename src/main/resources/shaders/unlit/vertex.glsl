#version 150

uniform mat4 mvpMatrix;

in vec4 in_Position;
in vec2 in_TextureCoord;

out vec2 pass_TextureCoord;

void main()
{
	gl_Position = mvpMatrix * in_Position;
    pass_TextureCoord = in_TextureCoord;
}