#version 330 core

in vec2 fUv;
in vec4 fCol;

uniform sampler2D uScreen;

out vec4 FragColor;

void main() {
    FragColor = fCol * texture(uScreen, fUv);
}