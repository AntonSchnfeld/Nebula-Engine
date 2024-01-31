#version 330 core

layout (location = 0) in vec3 vPos;

out vec4 fCol;

void main() {
    fCol = vec4(1, 0, 0, 1);
    gl_Position = vec4(vPos.x, vPos.y, vPos.z, 1.0f);
}