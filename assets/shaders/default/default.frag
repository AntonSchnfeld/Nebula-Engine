#version 330 core

in vec4 fCol;
in vec2 fUv;
in float fTexId;

void main() {
    gl_FragColor = fCol;
}
