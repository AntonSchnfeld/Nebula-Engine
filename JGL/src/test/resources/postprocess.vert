#version 330 core
layout (location=0) in vec2 vPos;
layout (location=1) in vec4 vCol;
layout (location=2) in vec2 vUv;

out vec2 fUv;
out vec4 fCol;

void main()
{
    fUv = vUv;
    fCol = vCol;

    gl_Position = vec4(vPos.x, vPos.y, 0.0, 1.0);
}