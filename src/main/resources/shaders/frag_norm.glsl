uniform sampler2D colorMap;
uniform sampler2D normalMap;

varying vec3 lightVec;
varying vec3 halfVec;
varying vec3 regularNormal;
varying vec4 regularPos;

void main() {
	vec3 normal = 2.0 * texture2D(normalMap, gl_TexCoord[0].st).xyz - 1.0;
	normal = normalize(normal);

	/*vec4 diffuseMaterial = texture2D(colorMap, gl_TexCoord[0].st);
	vec4 diffuseLight = gl_LightSource[0].diffuse;

	vec4 specularMaterial = vec4(1.0, 1.0, 1.0, 1.0);
	vec4 specularLight = gl_LightSource[0].specular;
	float shininess = gl_FrontMaterial.shininess;

	vec4 ambientLight = gl_LightSource[0].ambient;
      */
    vec4 diffuse = smoothstep(0.0, 1.0, dot(normal, lightVec));
    //vec4 specular = specularMaterial * specularLight * pow(max(dot(halfVec, normal), 0.0), shininess);

	gl_FragColor = diffuse;// + specular;
}