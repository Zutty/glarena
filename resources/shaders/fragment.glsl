#version 150

in vec3 pass_normal;
in vec3 pass_lightDir;
in vec2 pass_texCoord;

out vec4 out_color;

uniform sampler2D colorMap;

void main() {
    vec3 normal = normalize(pass_normal);
    vec3 lightDir = normalize(pass_lightDir);
    float diffuse = max(0.0, dot(normal, lightDir));

    out_color = diffuse * texture(colorMap, pass_texCoord);

    vec3 reflection = normalize(reflect(-lightDir, normal));
    float specular = max(0.0, dot(normal, reflection));

    if(diffuse != 0) {
        float fSpec = pow(specular, 128.0);
        out_color.rgb += vec3(fSpec, fSpec, fSpec);
    }
}