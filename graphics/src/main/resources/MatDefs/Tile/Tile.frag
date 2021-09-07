#import "Common/ShaderLib/GLSLCompat.glsllib"

#if defined(DISCARD_ALPHA)
    uniform float m_AlphaDiscardThreshold;
#endif

uniform vec4 m_Color;
uniform sampler2D m_ColorMap;

varying vec2 uv;

void main(){
    vec4 color = vec4(1.0);

    color *= texture2D(m_ColorMap, uv);

    #if defined(DISCARD_ALPHA)
        if(color.a < m_AlphaDiscardThreshold){
           discard;
        }
    #endif

    gl_FragColor = color;
}
