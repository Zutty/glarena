#version 150

uniform sampler2D texture_diffuse;

in vec3 pass_normal;
in vec3 pass_lightDir;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {

    vec3 normal = normalize(pass_normal);
    vec3 lightDir = normalize(pass_lightDir);
    float diffuse = max(0.0, dot(normal, lightDir));

    out_Color = diffuse * texture(texture_diffuse, pass_TextureCoord);

    vec3 reflection = normalize(reflect(-lightDir, normal));
    float specular = max(0.0, dot(normal, reflection));

    if(diffuse != 0) {
        float fSpec = pow(specular, 128.0);
        out_Color.rgb += vec3(fSpec, fSpec, fSpec);
    }
}