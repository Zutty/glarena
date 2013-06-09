#version 150

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec4 in_Position;
in vec3 foobar;
in vec2 in_TextureCoord;

out vec3 pass_normal;
out vec3 pass_lightDir;
out vec2 pass_TextureCoord;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;

    vec3 lightPosition = vec3(1, 0, 0);
    vec4 eyePos = viewMatrix * modelMatrix * in_Position;
    vec3 pos = eyePos.xyz / eyePos.w;
    pass_lightDir = normalize(lightPosition - pos);

    pass_normal = mat3(viewMatrix * modelMatrix) * foobar;
	pass_TextureCoord = in_TextureCoord;
}