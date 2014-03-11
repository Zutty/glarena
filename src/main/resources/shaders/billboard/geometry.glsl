#version 330

layout(points) in;
layout(triangle_strip) out;
layout(max_vertices = 8) out;

uniform mat4 gVP;
uniform vec3 gCameraPos;
uniform vec3 gCameraCenter;

in bb_vertex {
    float rotation;
    float scale;
    float fade;
} vertex[];

out bb_fragment {
    vec2 texCoord;
    float fade;
} frag;

mat3 rotationMatrix(vec3 axis, float angle) {
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;
    return mat3(oc * axis.x * axis.x + c, oc * axis.x * axis.y - axis.z * s, oc * axis.z * axis.x + axis.y * s,
        oc * axis.x * axis.y + axis.z * s, oc * axis.y * axis.y + c, oc * axis.y * axis.z - axis.x * s,
        oc * axis.z * axis.x - axis.y * s, oc * axis.y * axis.z + axis.x * s, oc * axis.z * axis.z + c);
}

void main()
{
    vec3 bbpos = gl_in[0].gl_Position.xyz;
    vec3 Pos = vec3(0,0,0);
    vec3 toCamera = normalize(gCameraPos - gCameraCenter);
    vec3 up = vec3(0.0, 1.0, 0.0);
    vec3 right = rotationMatrix(toCamera, vertex[0].rotation) * normalize(cross(toCamera, up));
    up = normalize(cross(right, toCamera));

    Pos -= (right * 0.5);
    Pos -= (up * 0.5);
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(0.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos += up;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(0.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos -= up;
    Pos += right;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(1.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos += up;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(1.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    EndPrimitive();
}
