# README

DX11渲染地板，使用了延迟着色，输出为4个ColorBuffer

GBuffer

- color0为 normal
- color1为 specular
- color2为 base color
- color3为 ???
- depth
- stencil

## 参考反编译文档

解读dxbc的一个例子

https://zhuanlan.zhihu.com/p/346324622

dxbc 指令说明

https://docs.microsoft.com/en-us/windows/win32/direct3dhlsl/dcl-indexabletemp

最终幻想14 永无止境的追求品质带来最高境界的图像

https://www.sohu.com/a/378960739_120511628


## 用到的指令说明

dcl_globalFlags  REFACTORING_ALLOWED - Permits the driver to reorder arithmetic operations for optimization, as shown here.

### in vs

mov dst, src

- dst is the destination register.
- src is a source register.

dst = src;

----
mad dst, src0, src1, src2

- dst is the destination register.
- src0 is a source register.
- src1 is a source register.
- src2 is a source register.

dst = mad(src0, src1, src2);

````
dest = src0 * src1 + src2;
````

----
dp4 dst, src0, src1

- dst is the destination register.
- src0 is a source register.
- src1 is a source register.

dst = dot(src0, src1)

````
dest.w = (src0.x * src1.x) + (src0.y * src1.y) +
(src0.z * src1.z) + (src0.w * src1.w);
dest.x = dest.y = dest.z = dest.w;
````

----
mul dst, src0, src1

dst = src0 * src1;

````
dest.x = src0.x * src1.x;
dest.y = src0.y * src1.y;
dest.z = src0.z * src1.z;
dest.w = src0.w * src1.w;
````

----
add dst, src0, src1

dst = src0 + src1;

````
dest.x = src0.x + src1.x;
dest.y = src0.y + src1.y;
dest.z = src0.z + src1.z;
dest.w = src0.w + src1.w;
````

----
lt dest[.mask], [-]src0[_abs][.swizzle], [-]src1[_abs][.swizzle]

dst = (src0 < src1);


### ps

rsq dst, src

Computes the reciprocal square root (positive only) of the source scalar.

dst = rsqrt(src); // 平方根的倒数

----
deriv_rtx_coarse[_sat] dest[.mask], [-]src0[_abs][.swizzle],

This instruction computes the rate of change of contents of each float32 component of src0 (post-swizzle), with regard to RenderTarget x direction (rtx) or RenderTarget y direction (see deriv_rty_coarse). Only a single x,y derivative pair is computed for each 2x2 stamp of pixels.

The data in the current pixel shader invocation may or may not participate in the calculation of the requested derivative, because the derivative will be calculated only once per 2x2 quad. For example, the x derivative could be a delta from the top row of pixels, and the y direction (deriv_rty_coarse) could be a delta from the left column of pixels. The exact calculation is up to the hardware vendor. There is also no specification dictating how the 2x2 quads will be aligned or tiled over a primitive.

Derivatives are calculated at a coarse level, once per 2x2 pixel quad. This instruction and deriv_rty_coarse are alternatives to deriv_rtx_fine and deriv_rty_fine. These _coarse and _fine derivative instructions are a replacement for deriv_rtxderiv_rty from previous shader models .

----
dmovc[_sat] dest[.mask], src0[.swizzle], [-]src1[_abs][.swizzle], [-]src2[_abs][.swizzle],

If src0, 
then dest = src1 
else dest = src2

----
div dst, src0, src1

div r8.xyzw, l(1.000000, 1.000000, 1.000000, 1.000000), r8.xyzw，等价于 r8 = 1. / r8;

----
exp dst x

Returns the base-e exponential, or ex, of the specified value.

EXP
ret exp(x)

----
sample_l 

sample_l[_aoffimmi(u,v,w)] dest[.mask], srcAddress[.swizzle], srcResource[.swizzle], srcSampler, srcLOD.select_component

Samples data from the specified Element/texture using the specified address and the filtering mode identified by the given sampler.

- dest [in] The address of the results of the operation.
- srcAddress [in] A set of texture coordinates. For more information see the sample instruction.
- srcResource [in] A texture register. For more information see the sample instruction.
- srcSampler [in] A sampler register. For more information see the sample instruction.
- srcLOD [in] The LOD.

----
sample_d

ssample_d[_aoffimmi(u,v,w)] dest[.mask], srcAddress[.swizzle], srcResource[.swizzle], srcSampler, srcXDerivatives[.swizzle], srcYDerivatives[.swizzle]

Samples data from the specified Element/texture using the specified address and the filtering mode identified by the given sampler.

- dest [in] The address of the results of the operation.
- srcAddress [in] A set of texture coordinates. For more information see the sample instruction.
- srcResource [in] A texture register. For more information see the sample instruction.
- srcSampler [in] A sampler register. For more information see the sample instruction.
- srcXDerivatives [in] The derivatives for the source address in the x direction. For more information, see the Remarks section.
- srcYDerivatives [in] The derivatives for the source address in the y direction. For more information, see the Remarks section.