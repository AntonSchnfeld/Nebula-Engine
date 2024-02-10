#version 330 core

layout (location = 0) in vec2 vPos;
layout (location = 1) in vec4 vCol;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fCol;

void main() {
    fCol = vCol;
    gl_Position = uView * uProjection * vec4(vPos, 0.0, 1.0);
}