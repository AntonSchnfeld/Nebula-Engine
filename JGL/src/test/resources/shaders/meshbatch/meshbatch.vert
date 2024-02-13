#version 330 core

vec2 applyTransform(vec2 pos, Transform transform) {
    float cosine = cos(radians(transform.rotation));
    float sine = sin(radians(transform.rotation));

    pos.x = pos.x * cosine - pos.y * sine;
    pos.y = pos.x * sine - pos.y * cosine;

    pos.x *= transform.scale.x;
    pos.y *= transform.scale.y;

    pos.x += transform.translation.x;
    pos.y += transform.translation.y;
}

struct Transform {
    vec2 translation;
    vec2 scale;
    float rotation;
};

uniform mat4 uView;
uniform mat4 uProjection;

layout(location = 0) in vec2 vPos;
layout(location = 1) in vec4 vCol;
layout(location = 2) in Transform vTransform;

out vec4 fCol;

void main() {
    vec2 transformedPos = applyTransform(vPos, vTransform);

    fCol = vCol;
    gl_Position = vec4(transformedPos, 0, 1);
}