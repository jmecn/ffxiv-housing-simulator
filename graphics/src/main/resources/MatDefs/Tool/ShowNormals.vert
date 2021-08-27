#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"

uniform mat3 g_WorldMatrixInverseTranspose;

attribute vec4 inPosition;
attribute vec4 inTexCoord;
attribute vec4 inNormal;
attribute vec4 inTangent;
attribute vec4 inBinormal;
attribute vec4 inColor;

varying vec4 v_WsPos;
varying vec4 v_TexCoord;
varying vec4 v_Normal;
varying vec4 v_Binormal;
varying vec4 v_Tangent;
varying vec4 v_Color;

varying mat3 v_TBNMatrix;

varying vec3 v_WsNormal;// world space normal
varying vec3 v_WsBinormal;// world space normal
varying vec3 v_WsTangent;// world space normal

void main(){
    v_WsPos = g_WorldMatrix * vec4(inPosition.xyz, 1.0);
    gl_Position = g_ViewProjectionMatrix * v_WsPos;
    v_TexCoord = inTexCoord;
    v_Color = inColor;

    v_Normal = vec4(normalize(inNormal.xyz), inNormal.w);
    v_WsNormal = g_WorldMatrixInverseTranspose * inNormal.xyz;

    v_Binormal = normalize(inBinormal * 2.0 - 1.0);
    v_WsBinormal = g_WorldMatrixInverseTranspose * v_Binormal.xyz;

    v_Tangent.xyz = cross(v_Binormal.xyz, v_Normal.xyz) * v_Binormal.w;
    v_WsTangent = cross(v_WsBinormal, v_WsNormal);

    v_TBNMatrix = mat3(
        vec3(v_Tangent.x, v_Binormal.x, v_Normal.x),
        vec3(v_Tangent.y, v_Binormal.y, v_Normal.y),
        vec3(v_Tangent.z, v_Binormal.z, v_Normal.z)
    );

}