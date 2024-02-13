#version 330 core

struct Transform {
    vec2 translation;
    vec2 scale;
    float rotation;
};

vec2 applyTransform(vec2 pos, Transform transform) {
    float cosine = cos(radians(transform.rotation));
    float sine = sin(radians(transform.rotation));

    pos.x = pos.x * cosine - pos.y * sine;
    pos.y = pos.x * sine - pos.y * cosine;

    pos.x *= transform.scale.x;
    pos.y *= transform.scale.y;

    pos.x += transform.translation.x;
    pos.y += transform.translation.y;

    return pos;
}

Transform new_Transform(vec2 translation, vec2 scale, float rotation) {
    Transform transform;
    transform.translation = translation;
    transform.scale = scale;
    transform.rotation = rotation;
    return transform;
}

uniform mat4 uView;
uniform mat4 uProjection;

layout(location = 0) in vec2 vPos;
layout(location = 1) in vec4 vCol;
layout(location = 2) in vec2 vTranslation;
layout(location = 3) in vec2 vScale;
layout(location = 4) in float vRotation;

out vec4 fCol;

void main() {
    Transform transform = new_Transform(vTranslation, vScale, vRotation);

    vec2 transformedPos = applyTransform(vPos, transform);

    fCol = vCol;
    gl_Position = uView * uProjection * vec4(transformedPos, 0, 1);
}