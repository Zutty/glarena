#version 150

layout(points) in;
layout(triangle_strip) out;
layout(max_vertices = 8) out;

uniform mat4 gVP;

in bullet_vertex {
    vec4 position;
    vec3 velocity;
    float scale;
    float fade;
} vertex[];

out bullet_fragment {
    vec2 texCoord;
    float fade;
} frag;

void main()
{
    vec3 position = vertex[0].position.xyz;
    vec3 corner = vec3(0,0,0);
    vec3 forward = normalize(vertex[0].velocity);
    vec3 up = vec3(0.0, 1.0, 0.0);
    vec3 right = cross(forward, up);

    corner -= (forward * 0.5);
    corner.y -= 0.5;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(0.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner.y += 1.0;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(0.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner.y -= 1.0;
    corner += forward;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(1.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner.y += 1.0;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(1.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    EndPrimitive();

    // Cross quad
    corner = vec3(0,0,0);
    corner -= (right * 0.5);
    corner -= (forward * 0.5);
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(0.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner += right;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(0.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner -= right;
    corner += forward;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(1.0, 0.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    corner += right;
    gl_Position = gVP * vec4(position + (corner * vertex[0].scale), 1.0);
    frag.texCoord = vec2(1.0, 1.0);
    frag.fade = vertex[0].fade;
    EmitVertex();

    EndPrimitive();
}
