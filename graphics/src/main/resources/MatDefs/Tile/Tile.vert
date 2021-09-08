#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform vec2 m_Params;
uniform float m_Time;
#ifdef SPEED
uniform float m_Speed;
#endif

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec2 uv;

void main(){
    //vec2 cur = floor(m_Params.zw);
    vec2 tile = m_Params.xy;

    float time = m_Time;
    #ifdef SPEED
        time *= m_Speed;
    #endif
    float c = floor(fract(time)  * m_Params.x * m_Params.y);
    float x = mod(c, m_Params.x);
    float y = floor(c / m_Params.x);
    vec2 cur = vec2(x, y);

    // calculate uv on tiles
    uv = inTexCoord / tile + cur / tile;

    vec4 modelSpacePos = vec4(inPosition, 1.0);

    gl_Position = g_WorldViewProjectionMatrix * modelSpacePos;
}