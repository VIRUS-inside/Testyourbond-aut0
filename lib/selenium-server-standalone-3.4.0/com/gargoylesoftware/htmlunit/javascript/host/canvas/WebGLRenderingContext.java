package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class WebGLRenderingContext
  extends SimpleScriptable
{
  @JsxConstant
  public static final int DEPTH_BUFFER_BIT = 256;
  @JsxConstant
  public static final int STENCIL_BUFFER_BIT = 1024;
  @JsxConstant
  public static final int COLOR_BUFFER_BIT = 16384;
  @JsxConstant
  public static final int POINTS = 0;
  @JsxConstant
  public static final int LINES = 1;
  @JsxConstant
  public static final int LINE_LOOP = 2;
  @JsxConstant
  public static final int LINE_STRIP = 3;
  @JsxConstant
  public static final int TRIANGLES = 4;
  @JsxConstant
  public static final int TRIANGLE_STRIP = 5;
  @JsxConstant
  public static final int TRIANGLE_FAN = 6;
  @JsxConstant
  public static final int ZERO = 0;
  @JsxConstant
  public static final int ONE = 1;
  @JsxConstant
  public static final int SRC_COLOR = 768;
  @JsxConstant
  public static final int ONE_MINUS_SRC_COLOR = 769;
  @JsxConstant
  public static final int SRC_ALPHA = 770;
  @JsxConstant
  public static final int ONE_MINUS_SRC_ALPHA = 771;
  @JsxConstant
  public static final int DST_ALPHA = 772;
  @JsxConstant
  public static final int ONE_MINUS_DST_ALPHA = 773;
  @JsxConstant
  public static final int DST_COLOR = 774;
  @JsxConstant
  public static final int ONE_MINUS_DST_COLOR = 775;
  @JsxConstant
  public static final int SRC_ALPHA_SATURATE = 776;
  @JsxConstant
  public static final int CONSTANT_COLOR = 32769;
  @JsxConstant
  public static final int ONE_MINUS_CONSTANT_COLOR = 32770;
  @JsxConstant
  public static final int CONSTANT_ALPHA = 32771;
  @JsxConstant
  public static final int ONE_MINUS_CONSTANT_ALPHA = 32772;
  @JsxConstant
  public static final int FUNC_ADD = 32774;
  @JsxConstant
  public static final int FUNC_REVERSE_SUBTRACT = 32779;
  @JsxConstant
  public static final int BLEND_EQUATION = 32777;
  @JsxConstant
  public static final int BLEND_EQUATION_RGB = 32777;
  @JsxConstant
  public static final int BLEND_EQUATION_ALPHA = 34877;
  @JsxConstant
  public static final int BLEND_DST_RGB = 32968;
  @JsxConstant
  public static final int NO_ERROR = 0;
  @JsxConstant
  public static final int NONE = 0;
  @JsxConstant
  public static final int NEVER = 512;
  @JsxConstant
  public static final int LESS = 513;
  @JsxConstant
  public static final int EQUAL = 514;
  @JsxConstant
  public static final int LEQUAL = 515;
  @JsxConstant
  public static final int GREATER = 516;
  @JsxConstant
  public static final int NOTEQUAL = 517;
  @JsxConstant
  public static final int GEQUAL = 518;
  @JsxConstant
  public static final int ALWAYS = 519;
  @JsxConstant
  public static final int FRONT = 1028;
  @JsxConstant
  public static final int BACK = 1029;
  @JsxConstant
  public static final int FRONT_AND_BACK = 1032;
  @JsxConstant
  public static final int INVALID_ENUM = 1280;
  @JsxConstant
  public static final int INVALID_VALUE = 1281;
  @JsxConstant
  public static final int INVALID_OPERATION = 1282;
  @JsxConstant
  public static final int OUT_OF_MEMORY = 1285;
  @JsxConstant
  public static final int INVALID_FRAMEBUFFER_OPERATION = 1286;
  @JsxConstant
  public static final int CW = 2304;
  @JsxConstant
  public static final int CCW = 2305;
  @JsxConstant
  public static final int LINE_WIDTH = 2849;
  @JsxConstant
  public static final int CULL_FACE = 2884;
  @JsxConstant
  public static final int CULL_FACE_MODE = 2885;
  @JsxConstant
  public static final int FRONT_FACE = 2886;
  @JsxConstant
  public static final int DEPTH_RANGE = 2928;
  @JsxConstant
  public static final int DEPTH_TEST = 2929;
  @JsxConstant
  public static final int DEPTH_WRITEMASK = 2930;
  @JsxConstant
  public static final int DEPTH_CLEAR_VALUE = 2931;
  @JsxConstant
  public static final int DEPTH_FUNC = 2932;
  @JsxConstant
  public static final int STENCIL_TEST = 2960;
  @JsxConstant
  public static final int STENCIL_CLEAR_VALUE = 2961;
  @JsxConstant
  public static final int STENCIL_FUNC = 2962;
  @JsxConstant
  public static final int STENCIL_VALUE_MASK = 2963;
  @JsxConstant
  public static final int STENCIL_FAIL = 2964;
  @JsxConstant
  public static final int STENCIL_PASS_DEPTH_FAIL = 2965;
  @JsxConstant
  public static final int STENCIL_PASS_DEPTH_PASS = 2966;
  @JsxConstant
  public static final int STENCIL_REF = 2967;
  @JsxConstant
  public static final int STENCIL_WRITEMASK = 2968;
  @JsxConstant
  public static final int VIEWPORT = 2978;
  @JsxConstant
  public static final int DITHER = 3024;
  @JsxConstant
  public static final int BLEND = 3042;
  @JsxConstant
  public static final int SCISSOR_BOX = 3088;
  @JsxConstant
  public static final int SCISSOR_TEST = 3089;
  @JsxConstant
  public static final int COLOR_CLEAR_VALUE = 3106;
  @JsxConstant
  public static final int COLOR_WRITEMASK = 3107;
  @JsxConstant
  public static final int UNPACK_ALIGNMENT = 3317;
  @JsxConstant
  public static final int PACK_ALIGNMENT = 3333;
  @JsxConstant
  public static final int MAX_TEXTURE_SIZE = 3379;
  @JsxConstant
  public static final int MAX_VIEWPORT_DIMS = 3386;
  @JsxConstant
  public static final int SUBPIXEL_BITS = 3408;
  @JsxConstant
  public static final int RED_BITS = 3410;
  @JsxConstant
  public static final int GREEN_BITS = 3411;
  @JsxConstant
  public static final int BLUE_BITS = 3412;
  @JsxConstant
  public static final int ALPHA_BITS = 3413;
  @JsxConstant
  public static final int DEPTH_BITS = 3414;
  @JsxConstant
  public static final int STENCIL_BITS = 3415;
  @JsxConstant
  public static final int TEXTURE_2D = 3553;
  @JsxConstant
  public static final int DONT_CARE = 4352;
  @JsxConstant
  public static final int FASTEST = 4353;
  @JsxConstant
  public static final int NICEST = 4354;
  @JsxConstant
  public static final int BYTE = 5120;
  @JsxConstant
  public static final int UNSIGNED_BYTE = 5121;
  @JsxConstant
  public static final int SHORT = 5122;
  @JsxConstant
  public static final int UNSIGNED_SHORT = 5123;
  @JsxConstant
  public static final int INT = 5124;
  @JsxConstant
  public static final int UNSIGNED_INT = 5125;
  @JsxConstant
  public static final int FLOAT = 5126;
  @JsxConstant
  public static final int INVERT = 5386;
  @JsxConstant
  public static final int TEXTURE = 5890;
  @JsxConstant
  public static final int STENCIL_INDEX = 6401;
  @JsxConstant
  public static final int DEPTH_COMPONENT = 6402;
  @JsxConstant
  public static final int ALPHA = 6406;
  @JsxConstant
  public static final int RGB = 6407;
  @JsxConstant
  public static final int RGBA = 6408;
  @JsxConstant
  public static final int LUMINANCE = 6409;
  @JsxConstant
  public static final int LUMINANCE_ALPHA = 6410;
  @JsxConstant
  public static final int KEEP = 7680;
  @JsxConstant
  public static final int REPLACE = 7681;
  @JsxConstant
  public static final int INCR = 7682;
  @JsxConstant
  public static final int DECR = 7683;
  @JsxConstant
  public static final int VENDOR = 7936;
  @JsxConstant
  public static final int RENDERER = 7937;
  @JsxConstant
  public static final int VERSION = 7938;
  @JsxConstant
  public static final int NEAREST = 9728;
  @JsxConstant
  public static final int LINEAR = 9729;
  @JsxConstant
  public static final int NEAREST_MIPMAP_NEAREST = 9984;
  @JsxConstant
  public static final int LINEAR_MIPMAP_NEAREST = 9985;
  @JsxConstant
  public static final int NEAREST_MIPMAP_LINEAR = 9986;
  @JsxConstant
  public static final int LINEAR_MIPMAP_LINEAR = 9987;
  @JsxConstant
  public static final int TEXTURE_MAG_FILTER = 10240;
  @JsxConstant
  public static final int TEXTURE_MIN_FILTER = 10241;
  @JsxConstant
  public static final int TEXTURE_WRAP_S = 10242;
  @JsxConstant
  public static final int TEXTURE_WRAP_T = 10243;
  @JsxConstant
  public static final int REPEAT = 10497;
  @JsxConstant
  public static final int POLYGON_OFFSET_UNITS = 10752;
  @JsxConstant
  public static final int BLEND_COLOR = 32773;
  @JsxConstant
  public static final int FUNC_SUBTRACT = 32778;
  @JsxConstant
  public static final int UNSIGNED_SHORT_4_4_4_4 = 32819;
  @JsxConstant
  public static final int UNSIGNED_SHORT_5_5_5_1 = 32820;
  @JsxConstant
  public static final int POLYGON_OFFSET_FILL = 32823;
  @JsxConstant
  public static final int POLYGON_OFFSET_FACTOR = 32824;
  @JsxConstant
  public static final int RGBA4 = 32854;
  @JsxConstant
  public static final int RGB5_A1 = 32855;
  @JsxConstant
  public static final int TEXTURE_BINDING_2D = 32873;
  @JsxConstant
  public static final int SAMPLE_ALPHA_TO_COVERAGE = 32926;
  @JsxConstant
  public static final int SAMPLE_COVERAGE = 32928;
  @JsxConstant
  public static final int SAMPLE_BUFFERS = 32936;
  @JsxConstant
  public static final int SAMPLES = 32937;
  @JsxConstant
  public static final int SAMPLE_COVERAGE_VALUE = 32938;
  @JsxConstant
  public static final int SAMPLE_COVERAGE_INVERT = 32939;
  @JsxConstant
  public static final int BLEND_SRC_RGB = 32969;
  @JsxConstant
  public static final int BLEND_DST_ALPHA = 32970;
  @JsxConstant
  public static final int BLEND_SRC_ALPHA = 32971;
  @JsxConstant
  public static final int CLAMP_TO_EDGE = 33071;
  @JsxConstant
  public static final int GENERATE_MIPMAP_HINT = 33170;
  @JsxConstant
  public static final int DEPTH_COMPONENT16 = 33189;
  @JsxConstant
  public static final int DEPTH_STENCIL_ATTACHMENT = 33306;
  @JsxConstant
  public static final int UNSIGNED_SHORT_5_6_5 = 33635;
  @JsxConstant
  public static final int MIRRORED_REPEAT = 33648;
  @JsxConstant
  public static final int ALIASED_POINT_SIZE_RANGE = 33901;
  @JsxConstant
  public static final int ALIASED_LINE_WIDTH_RANGE = 33902;
  @JsxConstant
  public static final int TEXTURE0 = 33984;
  @JsxConstant
  public static final int TEXTURE1 = 33985;
  @JsxConstant
  public static final int TEXTURE2 = 33986;
  @JsxConstant
  public static final int TEXTURE3 = 33987;
  @JsxConstant
  public static final int TEXTURE4 = 33988;
  @JsxConstant
  public static final int TEXTURE5 = 33989;
  @JsxConstant
  public static final int TEXTURE6 = 33990;
  @JsxConstant
  public static final int TEXTURE7 = 33991;
  @JsxConstant
  public static final int TEXTURE8 = 33992;
  @JsxConstant
  public static final int TEXTURE9 = 33993;
  @JsxConstant
  public static final int TEXTURE10 = 33994;
  @JsxConstant
  public static final int TEXTURE11 = 33995;
  @JsxConstant
  public static final int TEXTURE12 = 33996;
  @JsxConstant
  public static final int TEXTURE13 = 33997;
  @JsxConstant
  public static final int TEXTURE14 = 33998;
  @JsxConstant
  public static final int TEXTURE15 = 33999;
  @JsxConstant
  public static final int TEXTURE16 = 34000;
  @JsxConstant
  public static final int TEXTURE17 = 34001;
  @JsxConstant
  public static final int TEXTURE18 = 34002;
  @JsxConstant
  public static final int TEXTURE19 = 34003;
  @JsxConstant
  public static final int TEXTURE20 = 34004;
  @JsxConstant
  public static final int TEXTURE21 = 34005;
  @JsxConstant
  public static final int TEXTURE22 = 34006;
  @JsxConstant
  public static final int TEXTURE23 = 34007;
  @JsxConstant
  public static final int TEXTURE24 = 34008;
  @JsxConstant
  public static final int TEXTURE25 = 34009;
  @JsxConstant
  public static final int TEXTURE26 = 34010;
  @JsxConstant
  public static final int TEXTURE27 = 34011;
  @JsxConstant
  public static final int TEXTURE28 = 34012;
  @JsxConstant
  public static final int TEXTURE29 = 34013;
  @JsxConstant
  public static final int TEXTURE30 = 34014;
  @JsxConstant
  public static final int TEXTURE31 = 34015;
  @JsxConstant
  public static final int ACTIVE_TEXTURE = 34016;
  @JsxConstant
  public static final int MAX_RENDERBUFFER_SIZE = 34024;
  @JsxConstant
  public static final int DEPTH_STENCIL = 34041;
  @JsxConstant
  public static final int INCR_WRAP = 34055;
  @JsxConstant
  public static final int DECR_WRAP = 34056;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP = 34067;
  @JsxConstant
  public static final int TEXTURE_BINDING_CUBE_MAP = 34068;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
  @JsxConstant
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
  @JsxConstant
  public static final int MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_ENABLED = 34338;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_SIZE = 34339;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_STRIDE = 34340;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_TYPE = 34341;
  @JsxConstant
  public static final int CURRENT_VERTEX_ATTRIB = 34342;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_POINTER = 34373;
  @JsxConstant
  public static final int COMPRESSED_TEXTURE_FORMATS = 34467;
  @JsxConstant
  public static final int BUFFER_SIZE = 34660;
  @JsxConstant
  public static final int BUFFER_USAGE = 34661;
  @JsxConstant
  public static final int STENCIL_BACK_FUNC = 34816;
  @JsxConstant
  public static final int STENCIL_BACK_FAIL = 34817;
  @JsxConstant
  public static final int STENCIL_BACK_PASS_DEPTH_FAIL = 34818;
  @JsxConstant
  public static final int STENCIL_BACK_PASS_DEPTH_PASS = 34819;
  @JsxConstant
  public static final int MAX_VERTEX_ATTRIBS = 34921;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922;
  @JsxConstant
  public static final int MAX_TEXTURE_IMAGE_UNITS = 34930;
  @JsxConstant
  public static final int ARRAY_BUFFER = 34962;
  @JsxConstant
  public static final int ELEMENT_ARRAY_BUFFER = 34963;
  @JsxConstant
  public static final int ARRAY_BUFFER_BINDING = 34964;
  @JsxConstant
  public static final int ELEMENT_ARRAY_BUFFER_BINDING = 34965;
  @JsxConstant
  public static final int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975;
  @JsxConstant
  public static final int STREAM_DRAW = 35040;
  @JsxConstant
  public static final int STATIC_DRAW = 35044;
  @JsxConstant
  public static final int DYNAMIC_DRAW = 35048;
  @JsxConstant
  public static final int FRAGMENT_SHADER = 35632;
  @JsxConstant
  public static final int VERTEX_SHADER = 35633;
  @JsxConstant
  public static final int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660;
  @JsxConstant
  public static final int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661;
  @JsxConstant
  public static final int SHADER_TYPE = 35663;
  @JsxConstant
  public static final int FLOAT_VEC2 = 35664;
  @JsxConstant
  public static final int FLOAT_VEC3 = 35665;
  @JsxConstant
  public static final int FLOAT_VEC4 = 35666;
  @JsxConstant
  public static final int INT_VEC2 = 35667;
  @JsxConstant
  public static final int INT_VEC3 = 35668;
  @JsxConstant
  public static final int INT_VEC4 = 35669;
  @JsxConstant
  public static final int BOOL = 35670;
  @JsxConstant
  public static final int BOOL_VEC2 = 35671;
  @JsxConstant
  public static final int BOOL_VEC3 = 35672;
  @JsxConstant
  public static final int BOOL_VEC4 = 35673;
  @JsxConstant
  public static final int FLOAT_MAT2 = 35674;
  @JsxConstant
  public static final int FLOAT_MAT3 = 35675;
  @JsxConstant
  public static final int FLOAT_MAT4 = 35676;
  @JsxConstant
  public static final int SAMPLER_2D = 35678;
  @JsxConstant
  public static final int SAMPLER_CUBE = 35680;
  @JsxConstant
  public static final int DELETE_STATUS = 35712;
  @JsxConstant
  public static final int COMPILE_STATUS = 35713;
  @JsxConstant
  public static final int LINK_STATUS = 35714;
  @JsxConstant
  public static final int VALIDATE_STATUS = 35715;
  @JsxConstant
  public static final int ATTACHED_SHADERS = 35717;
  @JsxConstant
  public static final int ACTIVE_UNIFORMS = 35718;
  @JsxConstant
  public static final int ACTIVE_ATTRIBUTES = 35721;
  @JsxConstant
  public static final int SHADING_LANGUAGE_VERSION = 35724;
  @JsxConstant
  public static final int CURRENT_PROGRAM = 35725;
  @JsxConstant
  public static final int IMPLEMENTATION_COLOR_READ_TYPE = 35738;
  @JsxConstant
  public static final int IMPLEMENTATION_COLOR_READ_FORMAT = 35739;
  @JsxConstant
  public static final int STENCIL_BACK_REF = 36003;
  @JsxConstant
  public static final int STENCIL_BACK_VALUE_MASK = 36004;
  @JsxConstant
  public static final int STENCIL_BACK_WRITEMASK = 36005;
  @JsxConstant
  public static final int FRAMEBUFFER_BINDING = 36006;
  @JsxConstant
  public static final int RENDERBUFFER_BINDING = 36007;
  @JsxConstant
  public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048;
  @JsxConstant
  public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049;
  @JsxConstant
  public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050;
  @JsxConstant
  public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051;
  @JsxConstant
  public static final int FRAMEBUFFER_COMPLETE = 36053;
  @JsxConstant
  public static final int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
  @JsxConstant
  public static final int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
  @JsxConstant
  public static final int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057;
  @JsxConstant
  public static final int FRAMEBUFFER_UNSUPPORTED = 36061;
  @JsxConstant
  public static final int COLOR_ATTACHMENT0 = 36064;
  @JsxConstant
  public static final int DEPTH_ATTACHMENT = 36096;
  @JsxConstant
  public static final int STENCIL_ATTACHMENT = 36128;
  @JsxConstant
  public static final int FRAMEBUFFER = 36160;
  @JsxConstant
  public static final int RENDERBUFFER = 36161;
  @JsxConstant
  public static final int RENDERBUFFER_WIDTH = 36162;
  @JsxConstant
  public static final int RENDERBUFFER_HEIGHT = 36163;
  @JsxConstant
  public static final int RENDERBUFFER_INTERNAL_FORMAT = 36164;
  @JsxConstant
  public static final int STENCIL_INDEX8 = 36168;
  @JsxConstant
  public static final int RENDERBUFFER_RED_SIZE = 36176;
  @JsxConstant
  public static final int RENDERBUFFER_GREEN_SIZE = 36177;
  @JsxConstant
  public static final int RENDERBUFFER_BLUE_SIZE = 36178;
  @JsxConstant
  public static final int RENDERBUFFER_ALPHA_SIZE = 36179;
  @JsxConstant
  public static final int RENDERBUFFER_DEPTH_SIZE = 36180;
  @JsxConstant
  public static final int RENDERBUFFER_STENCIL_SIZE = 36181;
  @JsxConstant
  public static final int RGB565 = 36194;
  @JsxConstant
  public static final int LOW_FLOAT = 36336;
  @JsxConstant
  public static final int MEDIUM_FLOAT = 36337;
  @JsxConstant
  public static final int HIGH_FLOAT = 36338;
  @JsxConstant
  public static final int LOW_INT = 36339;
  @JsxConstant
  public static final int MEDIUM_INT = 36340;
  @JsxConstant
  public static final int HIGH_INT = 36341;
  @JsxConstant
  public static final int MAX_VERTEX_UNIFORM_VECTORS = 36347;
  @JsxConstant
  public static final int MAX_VARYING_VECTORS = 36348;
  @JsxConstant
  public static final int MAX_FRAGMENT_UNIFORM_VECTORS = 36349;
  @JsxConstant
  public static final int UNPACK_FLIP_Y_WEBGL = 37440;
  @JsxConstant
  public static final int UNPACK_PREMULTIPLY_ALPHA_WEBGL = 37441;
  @JsxConstant
  public static final int CONTEXT_LOST_WEBGL = 37442;
  @JsxConstant
  public static final int UNPACK_COLORSPACE_CONVERSION_WEBGL = 37443;
  @JsxConstant
  public static final int BROWSER_DEFAULT_WEBGL = 37444;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public WebGLRenderingContext() {}
}
