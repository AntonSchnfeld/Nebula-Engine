#version 330 core

uniform sampler2D uTextures[32];

in vec4 fCol;
in vec2 fUv;
in float fTexId;

out vec4 FragColor;

void main() {
    int texId = int(fTexId);

    if (texId >= 0) {
        FragColor = fCol * texture(uTextures[texId], fUv);
    } else {
        FragColor = fCol;
    }
}