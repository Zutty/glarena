#version 330

layout(points) in;
layout(triangle_strip) out;
layout(max_vertices = 4) out;

uniform mat4 gVP;

in bb_vertex {
    vec4 position;
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
    vec3 bbpos = vertex[0].position.xyz;
    vec3 Pos = vec3(0,0,0);
    vec3 forward = vec3(0,1,0);
    vec3 ortho = vec3(1.0, 0.0, 0.0);
    vec3 right = rotationMatrix(forward, vertex[0].rotation) * normalize(cross(forward, ortho));
    ortho = normalize(cross(right, forward));

    Pos -= (right * 0.5);
    Pos -= (ortho * 0.5);
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(0.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos += ortho;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(0.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos -= ortho;
    Pos += right;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(1.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    Pos += ortho;
    gl_Position = gVP * vec4((Pos * vertex[0].scale) + bbpos, 1.0);
    frag.texCoord = vec2(1.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    EndPrimitive();
}
