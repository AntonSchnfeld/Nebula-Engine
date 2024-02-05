#version 330 core

uniform mat4 uProjection;
uniform mat4 uView;

layout(location = 0) in vec3 vPos;
layout(location = 1) in vec4 vCol;
layout(location = 2) in vec2 vUv;
layout(location = 3) in float vTexId;

out vec4 fCol;
out vec2 fUv;
out float fTexId;

void main() {
    fCol = vCol;
    fUv = vUv;
    fTexId = vTexId;

    gl_Position = uView * uProjection * vec4(vPos, 1.0);
}