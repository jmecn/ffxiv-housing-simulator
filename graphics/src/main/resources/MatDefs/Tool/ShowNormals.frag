#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform bool m_HasSamplerNormalMap0;

uniform sampler2D m_SamplerColorMap0;
#ifdef HAS_NORMALMAP_0
uniform sampler2D m_SamplerNormalMap0;
#endif
uniform sampler2D m_SamplerSepecularMap0;

varying vec4 v_WsPos;
varying vec4 v_TexCoord;
varying vec4 v_Normal;
varying vec4 v_Tangent;
varying vec4 v_Binormal;
varying vec4 v_Color;

varying mat3 v_TBNMatrix;

varying vec3 v_WsNormal;// world space normal
varying vec3 v_WsBinormal;// world space normal
varying vec3 v_WsTangent;// world space normal

void main(){
    #ifdef HAS_NORMALMAP_0
    vec4 mapNormal = texture2D(m_SamplerNormalMap0, v_TexCoord.st);
    vec3 bump = normalize(mapNormal.xyz * 2.0 - 1.0);
    //vec3 normal = bump * v_TBNMatrix;
    vec3 normal = (bump.x * v_Tangent.xyz) + (bump.y * v_Binormal.xyz) + (bump.z * v_Normal.xyz);
    gl_FragColor = vec4(vec3(normal * 0.5 + 0.5), 1.0);
    //gl_FragColor = vec4(mapNormal.xyz, 1.0);
    #else
    vec3 normal = normalize(v_Normal.xyz);
    gl_FragColor = vec4(vec3(normal * 0.5 + 0.5), 1.0);
    #endif
}