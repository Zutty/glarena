attribute vec3 vTangent;
varying vec3 lightVec;
varying vec3 halfVec;
varying vec3 regularNormal;
varying vec3 regularPos;

void main() {
	regularNormal = gl_NormalMatrix * gl_Normal;
	regularPos = gl_ModelViewMatrix * gl_Vertex;

	vec3 n = gl_NormalMatrix * normalize(gl_Normal);
	vec3 t = gl_NormalMatrix * normalize(vTangent);
	vec3 b = cross(n, t);
    mat3 tbn = mat3(t, b, n);

	vec3 position = gl_ModelViewMatrix * gl_Vertex;
	vec3 lightDir = gl_LightSource[0].position;
	vec3 eyeVec = tbn * normalize(-position.xyz);
	lightVec = tbn * normalize(-lightDir.xyz);
	halfVec = normalize(eyeVec + lightVec);

	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_TexCoord[0] =  gl_MultiTexCoord0;
}