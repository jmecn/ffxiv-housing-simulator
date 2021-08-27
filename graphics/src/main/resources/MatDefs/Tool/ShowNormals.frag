#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec2 g_Resolution;

uniform bool m_HasSamplerNormalMap0;

uniform sampler2D m_SamplerColorMap0;
#ifdef HAS_NORMALMAP_0
uniform sampler2D m_SamplerNormalMap0;
#endif
uniform sampler2D m_SamplerSepecularMap0;

varying vec4 v_PosWs;
varying vec4 v_TexCoord;
varying vec4 v_Normal;
varying vec4 v_Binormal;

#ifdef HAS_NORMALMAP_0
vec4 calcMapNormal() {
    vec3 normal = normalize(v_Normal.xyz);
    vec3 binormal = normalize(v_Binormal.xyz);
    vec3 tangent = normalize(cross(binormal, normal) * v_Binormal.w);

    vec3 r4;
    r4.xy = texture2D(m_SamplerNormalMap0, v_TexCoord.st).xy;
    r4.xy = r4.xy - 0.5;
    r4.z = sqrt(max(0.25 - dot(r4.xy, r4.xy), 0.0));

    r4.xyz = normalize(r4.xyz);

    normal = r4.x * tangent + r4.y * binormal + r4.z * normal;

    return vec4(vec3(normal * 0.5 + 0.5), 1.0);
}
vec4 getMapNormal() {
    vec3 normal = normalize(v_Normal.xyz);
    vec3 binormal = normalize(v_Binormal.xyz);
    vec3 tangent = normalize(cross(binormal, normal) * v_Binormal.w);

    vec3 r4;
    r4.xyz = texture2D(m_SamplerNormalMap0, v_TexCoord.st).xyz;
    r4.xy = r4.xy - 0.5;
    r4.z = 0.5;
    r4.xyz = normalize(r4.xyz);

    normal = r4.x * tangent + r4.y * binormal + r4.z * normal;

    return vec4(vec3(normal * 0.5 + 0.5), 1.0);
}
#endif

vec4 getSpecular() {
    vec3 color = texture2D(m_SamplerSepecularMap0, v_TexCoord.st).ggg;
    return vec4(color, 1.0);
}

vec4 getColor() {
    return vec4(texture2D(m_SamplerColorMap0, v_TexCoord.st).rgb, 1.0);
}

vec4 getNormal() {
    vec3 normal = normalize(v_Normal.xyz);
    return vec4(vec3(normal * 0.5 + 0.5), 1.0);
}

void main(){
    vec2 st = gl_FragCoord.xy / g_Resolution.xy;

    float alpha = texture(m_SamplerColorMap0, v_TexCoord.st).a;
    alpha -= 0.5;
    if (alpha < 0.0) {
        discard;
    }
    // normal map
    #ifdef HAS_NORMALMAP_0
    gl_FragColor = calcMapNormal();
    #else
    gl_FragColor = getNormal();
    #endif
//
//    if (st.x < 0.5) {
//        if (st.y > 0.5) {
//            // normal map
//            #ifdef HAS_NORMALMAP_0
//            gl_FragColor = getMapNormal();
//            #endif
//        } else {
//            gl_FragColor = getNormal();
//        }
//    } else {
//        if (st.y > 0.5) {
//            // color
//            #ifdef HAS_COLORMAP_0
//            gl_FragColor = getColor();
//            #endif
//        } else {
//            // specular map
//            #ifdef HAS_SPECULARMAP_0
//            gl_FragColor = getSpecular();
//            #endif
//        }
//    }
}