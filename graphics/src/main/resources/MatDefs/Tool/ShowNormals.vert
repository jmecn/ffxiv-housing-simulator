#import "Common/ShaderLib/GLSLCompat.glsllib"
//#import "Common/ShaderLib/Instancing.glsllib"

uniform mat3 g_NormalMatrix;
uniform mat4 g_WorldMatrix;
uniform mat4 g_ViewProjectionMatrix;

attribute vec4 inPosition;
attribute vec4 inTexCoord;
attribute vec4 inNormal;
attribute vec4 inTangent;
attribute vec4 inBinormal;

varying vec4 v_PosWs;
varying vec4 v_TexCoord;
varying vec4 v_Normal;
varying vec4 v_Binormal;

void main(){
    v_PosWs = g_WorldMatrix * vec4(inPosition.xyz, 1.0);
    v_PosWs.w = inPosition.w;

    gl_Position = g_ViewProjectionMatrix * vec4(v_PosWs.xyz, 1.0);

    v_Normal.xyz = inNormal.xyz;
    v_Normal.w = 1.0;

    v_Binormal.xyz = (inBinormal.xyz * 2.0 - 1.0);
    v_Binormal.w = inBinormal.w * 2.0 - 1.0;

    v_TexCoord = inTexCoord;
}