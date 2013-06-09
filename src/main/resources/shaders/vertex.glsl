#version 150

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 lightPosition;

in vec4 in_position;
in vec3 in_normal;
in vec2 in_texCoord;

out vec3 pass_normal;
out vec3 pass_lightDir;
out vec2 pass_texCoord;

void main() {
    pass_normal = mat3(viewMatrix * modelMatrix) * in_normal;
    vec4 eyePos = viewMatrix * modelMatrix * in_position;
    vec3 pos = eyePos.xyz / eyePos.w;
    pass_lightDir = normalize(lightPosition - pos);
    pass_texCoord = in_texCoord;
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_position;
}