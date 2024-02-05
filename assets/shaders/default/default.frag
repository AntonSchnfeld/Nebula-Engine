#version 330 core

uniform sampler2D uTextures[32];

in vec4 fCol;
in vec2 fUv;
in float fTexId;

out vec4 FragColor;

void main() {

    for (int i = 0; i < uTextures.length(); i++) {
        if (i == fTexId) {
            FragColor = fCol * texture(uTextures[i], fUv);
        }
    }

    FragColor = fCol;
}