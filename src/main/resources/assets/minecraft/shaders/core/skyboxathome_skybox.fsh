#version 150

#moj_import <fog.glsl>
#define M_PI 3.1415926535897932384626433832795

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform mat3 IViewRotMat;

in float vertexDistance;
in vec3 texOff;

out vec4 fragColor;

void main() {
    float radius = sqrt(texOff.x * texOff.x + texOff.z * texOff.z);
    float a = atan(-texOff.x, texOff.z);
    float b = atan(texOff.y, radius);
    vec4 color = texture(Sampler0, vec2(
        (a - M_PI) / (-2 * M_PI),
        1 - ((b + M_PI / 2) / M_PI)
    ));
    // vec4 color = vec4(normalize(texCoord) / 2 + vec3(0.5, 0.5, 0.5), 1.0);
    color *= ColorModulator;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
