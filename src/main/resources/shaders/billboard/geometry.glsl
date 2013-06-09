#version 150

layout(points) in;
layout(triangle_strip) out;
layout(max_vertices = 8) out;

uniform mat4 gVP;

in vec3 pass_velocity[];
out vec2 TexCoord;

void main()
{
    vec3 Pos = gl_in[0].gl_Position.xyz;
    vec3 forward = normalize(pass_velocity[0]);
    vec3 up = vec3(0.0, 1.0, 0.0);
    vec3 right = cross(forward, up);

    Pos -= (forward * 0.5);
    Pos.y -= 0.5;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(0.0, 0.0);
    EmitVertex();

    Pos.y += 1.0;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(0.0, 1.0);
    EmitVertex();

    Pos.y -= 1.0;
    Pos += forward;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(1.0, 0.0);
    EmitVertex();

    Pos.y += 1.0;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(1.0, 1.0);
    EmitVertex();

    EndPrimitive();

    // Cross quad
    Pos = gl_in[0].gl_Position.xyz;
    Pos -= (right * 0.5);
    Pos -= (forward * 0.5);
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(0.0, 0.0);
    EmitVertex();

    Pos += right;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(0.0, 1.0);
    EmitVertex();

    Pos -= right;
    Pos += forward;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(1.0, 0.0);
    EmitVertex();

    Pos += right;
    gl_Position = gVP * vec4(Pos, 1.0);
    TexCoord = vec2(1.0, 1.0);
    EmitVertex();

    EndPrimitive();

}
