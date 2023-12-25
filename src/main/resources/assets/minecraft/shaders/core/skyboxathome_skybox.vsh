#version 150

#moj_import <light.glsl>
#moj_import <fog.glsl>

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform int FogShape;

uniform mat4 IBoblessProjMat;

out float vertexDistance;
out vec3 texOff;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);
    vec4 texOff4 = IBoblessProjMat * gl_Position;
    texOff = IViewRotMat * (texOff4.xyz / texOff4.w);
}
