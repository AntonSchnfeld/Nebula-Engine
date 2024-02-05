#version 330 core

uniform sampler2DArray uTextures;

in vec4 fCol;
in vec2 fUv;
in float fTexId;

out vec4 FragColor;

void main() {
    if (fTexId >= 0) {
        FragColor = fCol * texture(uTextures, vec3(fUv, fTexId));
    } else {
        FragColor = fCol;
    }
}