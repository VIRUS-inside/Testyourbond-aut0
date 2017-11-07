package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class WebGL2RenderingContext
  extends SimpleScriptable
{
  @JsxConstant
  public static final long ACTIVE_ATTRIBUTES = 35721L;
  @JsxConstant
  public static final long ACTIVE_TEXTURE = 34016L;
  @JsxConstant
  public static final long ACTIVE_UNIFORMS = 35718L;
  @JsxConstant
  public static final long ACTIVE_UNIFORM_BLOCKS = 35382L;
  @JsxConstant
  public static final long ALIASED_LINE_WIDTH_RANGE = 33902L;
  @JsxConstant
  public static final long ALIASED_POINT_SIZE_RANGE = 33901L;
  @JsxConstant
  public static final long ALPHA = 6406L;
  @JsxConstant
  public static final long ALPHA_BITS = 3413L;
  @JsxConstant
  public static final long ALREADY_SIGNALED = 37146L;
  @JsxConstant
  public static final long ALWAYS = 519L;
  @JsxConstant
  public static final long ANY_SAMPLES_PASSED = 35887L;
  @JsxConstant
  public static final long ANY_SAMPLES_PASSED_CONSERVATIVE = 36202L;
  @JsxConstant
  public static final long ARRAY_BUFFER = 34962L;
  @JsxConstant
  public static final long ARRAY_BUFFER_BINDING = 34964L;
  @JsxConstant
  public static final long ATTACHED_SHADERS = 35717L;
  @JsxConstant
  public static final long BACK = 1029L;
  @JsxConstant
  public static final long BLEND = 3042L;
  @JsxConstant
  public static final long BLEND_COLOR = 32773L;
  @JsxConstant
  public static final long BLEND_DST_ALPHA = 32970L;
  @JsxConstant
  public static final long BLEND_DST_RGB = 32968L;
  @JsxConstant
  public static final long BLEND_EQUATION = 32777L;
  @JsxConstant
  public static final long BLEND_EQUATION_ALPHA = 34877L;
  @JsxConstant
  public static final long BLEND_EQUATION_RGB = 32777L;
  @JsxConstant
  public static final long BLEND_SRC_ALPHA = 32971L;
  @JsxConstant
  public static final long BLEND_SRC_RGB = 32969L;
  @JsxConstant
  public static final long BLUE_BITS = 3412L;
  @JsxConstant
  public static final long BOOL = 35670L;
  @JsxConstant
  public static final long BOOL_VEC2 = 35671L;
  @JsxConstant
  public static final long BOOL_VEC3 = 35672L;
  @JsxConstant
  public static final long BOOL_VEC4 = 35673L;
  @JsxConstant
  public static final long BROWSER_DEFAULT_WEBGL = 37444L;
  @JsxConstant
  public static final long BUFFER_SIZE = 34660L;
  @JsxConstant
  public static final long BUFFER_USAGE = 34661L;
  @JsxConstant
  public static final long BYTE = 5120L;
  @JsxConstant
  public static final long CCW = 2305L;
  @JsxConstant
  public static final long CLAMP_TO_EDGE = 33071L;
  @JsxConstant
  public static final long COLOR = 6144L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT0 = 36064L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT1 = 36065L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT10 = 36074L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT11 = 36075L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT12 = 36076L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT13 = 36077L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT14 = 36078L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT15 = 36079L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT2 = 36066L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT3 = 36067L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT4 = 36068L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT5 = 36069L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT6 = 36070L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT7 = 36071L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT8 = 36072L;
  @JsxConstant
  public static final long COLOR_ATTACHMENT9 = 36073L;
  @JsxConstant
  public static final long COLOR_BUFFER_BIT = 16384L;
  @JsxConstant
  public static final long COLOR_CLEAR_VALUE = 3106L;
  @JsxConstant
  public static final long COLOR_WRITEMASK = 3107L;
  @JsxConstant
  public static final long COMPARE_REF_TO_TEXTURE = 34894L;
  @JsxConstant
  public static final long COMPILE_STATUS = 35713L;
  @JsxConstant
  public static final long COMPRESSED_TEXTURE_FORMATS = 34467L;
  @JsxConstant
  public static final long CONDITION_SATISFIED = 37148L;
  @JsxConstant
  public static final long CONSTANT_ALPHA = 32771L;
  @JsxConstant
  public static final long CONSTANT_COLOR = 32769L;
  @JsxConstant
  public static final long CONTEXT_LOST_WEBGL = 37442L;
  @JsxConstant
  public static final long COPY_READ_BUFFER = 36662L;
  @JsxConstant
  public static final long COPY_READ_BUFFER_BINDING = 36662L;
  @JsxConstant
  public static final long COPY_WRITE_BUFFER = 36663L;
  @JsxConstant
  public static final long COPY_WRITE_BUFFER_BINDING = 36663L;
  @JsxConstant
  public static final long CULL_FACE = 2884L;
  @JsxConstant
  public static final long CULL_FACE_MODE = 2885L;
  @JsxConstant
  public static final long CURRENT_PROGRAM = 35725L;
  @JsxConstant
  public static final long CURRENT_QUERY = 34917L;
  @JsxConstant
  public static final long CURRENT_VERTEX_ATTRIB = 34342L;
  @JsxConstant
  public static final long CW = 2304L;
  @JsxConstant
  public static final long DECR = 7683L;
  @JsxConstant
  public static final long DECR_WRAP = 34056L;
  @JsxConstant
  public static final long DELETE_STATUS = 35712L;
  @JsxConstant
  public static final long DEPTH = 6145L;
  @JsxConstant
  public static final long DEPTH24_STENCIL8 = 35056L;
  @JsxConstant
  public static final long DEPTH32F_STENCIL8 = 36013L;
  @JsxConstant
  public static final long DEPTH_ATTACHMENT = 36096L;
  @JsxConstant
  public static final long DEPTH_BITS = 3414L;
  @JsxConstant
  public static final long DEPTH_BUFFER_BIT = 256L;
  @JsxConstant
  public static final long DEPTH_CLEAR_VALUE = 2931L;
  @JsxConstant
  public static final long DEPTH_COMPONENT = 6402L;
  @JsxConstant
  public static final long DEPTH_COMPONENT16 = 33189L;
  @JsxConstant
  public static final long DEPTH_COMPONENT24 = 33190L;
  @JsxConstant
  public static final long DEPTH_COMPONENT32F = 36012L;
  @JsxConstant
  public static final long DEPTH_FUNC = 2932L;
  @JsxConstant
  public static final long DEPTH_RANGE = 2928L;
  @JsxConstant
  public static final long DEPTH_STENCIL = 34041L;
  @JsxConstant
  public static final long DEPTH_STENCIL_ATTACHMENT = 33306L;
  @JsxConstant
  public static final long DEPTH_TEST = 2929L;
  @JsxConstant
  public static final long DEPTH_WRITEMASK = 2930L;
  @JsxConstant
  public static final long DITHER = 3024L;
  @JsxConstant
  public static final long DONT_CARE = 4352L;
  @JsxConstant
  public static final long DRAW_BUFFER0 = 34853L;
  @JsxConstant
  public static final long DRAW_BUFFER1 = 34854L;
  @JsxConstant
  public static final long DRAW_BUFFER10 = 34863L;
  @JsxConstant
  public static final long DRAW_BUFFER11 = 34864L;
  @JsxConstant
  public static final long DRAW_BUFFER12 = 34865L;
  @JsxConstant
  public static final long DRAW_BUFFER13 = 34866L;
  @JsxConstant
  public static final long DRAW_BUFFER14 = 34867L;
  @JsxConstant
  public static final long DRAW_BUFFER15 = 34868L;
  @JsxConstant
  public static final long DRAW_BUFFER2 = 34855L;
  @JsxConstant
  public static final long DRAW_BUFFER3 = 34856L;
  @JsxConstant
  public static final long DRAW_BUFFER4 = 34857L;
  @JsxConstant
  public static final long DRAW_BUFFER5 = 34858L;
  @JsxConstant
  public static final long DRAW_BUFFER6 = 34859L;
  @JsxConstant
  public static final long DRAW_BUFFER7 = 34860L;
  @JsxConstant
  public static final long DRAW_BUFFER8 = 34861L;
  @JsxConstant
  public static final long DRAW_BUFFER9 = 34862L;
  @JsxConstant
  public static final long DRAW_FRAMEBUFFER = 36009L;
  @JsxConstant
  public static final long DRAW_FRAMEBUFFER_BINDING = 36006L;
  @JsxConstant
  public static final long DST_ALPHA = 772L;
  @JsxConstant
  public static final long DST_COLOR = 774L;
  @JsxConstant
  public static final long DYNAMIC_COPY = 35050L;
  @JsxConstant
  public static final long DYNAMIC_DRAW = 35048L;
  @JsxConstant
  public static final long DYNAMIC_READ = 35049L;
  @JsxConstant
  public static final long ELEMENT_ARRAY_BUFFER = 34963L;
  @JsxConstant
  public static final long ELEMENT_ARRAY_BUFFER_BINDING = 34965L;
  @JsxConstant
  public static final long EQUAL = 514L;
  @JsxConstant
  public static final long FASTEST = 4353L;
  @JsxConstant
  public static final long FLOAT = 5126L;
  @JsxConstant
  public static final long FLOAT_32_UNSIGNED_INT_24_8_REV = 36269L;
  @JsxConstant
  public static final long FLOAT_MAT2 = 35674L;
  @JsxConstant
  public static final long FLOAT_MAT2x3 = 35685L;
  @JsxConstant
  public static final long FLOAT_MAT2x4 = 35686L;
  @JsxConstant
  public static final long FLOAT_MAT3 = 35675L;
  @JsxConstant
  public static final long FLOAT_MAT3x2 = 35687L;
  @JsxConstant
  public static final long FLOAT_MAT3x4 = 35688L;
  @JsxConstant
  public static final long FLOAT_MAT4 = 35676L;
  @JsxConstant
  public static final long FLOAT_MAT4x2 = 35689L;
  @JsxConstant
  public static final long FLOAT_MAT4x3 = 35690L;
  @JsxConstant
  public static final long FLOAT_VEC2 = 35664L;
  @JsxConstant
  public static final long FLOAT_VEC3 = 35665L;
  @JsxConstant
  public static final long FLOAT_VEC4 = 35666L;
  @JsxConstant
  public static final long FRAGMENT_SHADER = 35632L;
  @JsxConstant
  public static final long FRAGMENT_SHADER_DERIVATIVE_HINT = 35723L;
  @JsxConstant
  public static final long FRAMEBUFFER = 36160L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 33301L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 33300L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 33296L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 33297L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 33302L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 33299L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 36049L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 36048L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_RED_SIZE = 33298L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 33303L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 36051L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 36052L;
  @JsxConstant
  public static final long FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 36050L;
  @JsxConstant
  public static final long FRAMEBUFFER_BINDING = 36006L;
  @JsxConstant
  public static final long FRAMEBUFFER_COMPLETE = 36053L;
  @JsxConstant
  public static final long FRAMEBUFFER_DEFAULT = 33304L;
  @JsxConstant
  public static final long FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054L;
  @JsxConstant
  public static final long FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 36057L;
  @JsxConstant
  public static final long FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055L;
  @JsxConstant
  public static final long FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 36182L;
  @JsxConstant
  public static final long FRAMEBUFFER_UNSUPPORTED = 36061L;
  @JsxConstant
  public static final long FRONT = 1028L;
  @JsxConstant
  public static final long FRONT_AND_BACK = 1032L;
  @JsxConstant
  public static final long FRONT_FACE = 2886L;
  @JsxConstant
  public static final long FUNC_ADD = 32774L;
  @JsxConstant
  public static final long FUNC_REVERSE_SUBTRACT = 32779L;
  @JsxConstant
  public static final long FUNC_SUBTRACT = 32778L;
  @JsxConstant
  public static final long GENERATE_MIPMAP_HINT = 33170L;
  @JsxConstant
  public static final long GEQUAL = 518L;
  @JsxConstant
  public static final long GREATER = 516L;
  @JsxConstant
  public static final long GREEN_BITS = 3411L;
  @JsxConstant
  public static final long HALF_FLOAT = 5131L;
  @JsxConstant
  public static final long HIGH_FLOAT = 36338L;
  @JsxConstant
  public static final long HIGH_INT = 36341L;
  @JsxConstant
  public static final long IMPLEMENTATION_COLOR_READ_FORMAT = 35739L;
  @JsxConstant
  public static final long IMPLEMENTATION_COLOR_READ_TYPE = 35738L;
  @JsxConstant
  public static final long INCR = 7682L;
  @JsxConstant
  public static final long INCR_WRAP = 34055L;
  @JsxConstant
  public static final long INT = 5124L;
  @JsxConstant
  public static final long INTERLEAVED_ATTRIBS = 35980L;
  @JsxConstant
  public static final long INT_2_10_10_10_REV = 36255L;
  @JsxConstant
  public static final long INT_SAMPLER_2D = 36298L;
  @JsxConstant
  public static final long INT_SAMPLER_2D_ARRAY = 36303L;
  @JsxConstant
  public static final long INT_SAMPLER_3D = 36299L;
  @JsxConstant
  public static final long INT_SAMPLER_CUBE = 36300L;
  @JsxConstant
  public static final long INT_VEC2 = 35667L;
  @JsxConstant
  public static final long INT_VEC3 = 35668L;
  @JsxConstant
  public static final long INT_VEC4 = 35669L;
  @JsxConstant
  public static final long INVALID_ENUM = 1280L;
  @JsxConstant
  public static final long INVALID_FRAMEBUFFER_OPERATION = 1286L;
  @JsxConstant
  public static final long INVALID_INDEX = 4294967295L;
  @JsxConstant
  public static final long INVALID_OPERATION = 1282L;
  @JsxConstant
  public static final long INVALID_VALUE = 1281L;
  @JsxConstant
  public static final long INVERT = 5386L;
  @JsxConstant
  public static final long KEEP = 7680L;
  @JsxConstant
  public static final long LEQUAL = 515L;
  @JsxConstant
  public static final long LESS = 513L;
  @JsxConstant
  public static final long LINEAR = 9729L;
  @JsxConstant
  public static final long LINEAR_MIPMAP_LINEAR = 9987L;
  @JsxConstant
  public static final long LINEAR_MIPMAP_NEAREST = 9985L;
  @JsxConstant
  public static final long LINES = 1L;
  @JsxConstant
  public static final long LINE_LOOP = 2L;
  @JsxConstant
  public static final long LINE_STRIP = 3L;
  @JsxConstant
  public static final long LINE_WIDTH = 2849L;
  @JsxConstant
  public static final long LINK_STATUS = 35714L;
  @JsxConstant
  public static final long LOW_FLOAT = 36336L;
  @JsxConstant
  public static final long LOW_INT = 36339L;
  @JsxConstant
  public static final long LUMINANCE = 6409L;
  @JsxConstant
  public static final long LUMINANCE_ALPHA = 6410L;
  @JsxConstant
  public static final long MAX = 32776L;
  @JsxConstant
  public static final long MAX_3D_TEXTURE_SIZE = 32883L;
  @JsxConstant
  public static final long MAX_ARRAY_TEXTURE_LAYERS = 35071L;
  @JsxConstant
  public static final long MAX_CLIENT_WAIT_TIMEOUT_WEBGL = 37447L;
  @JsxConstant
  public static final long MAX_COLOR_ATTACHMENTS = 36063L;
  @JsxConstant
  public static final long MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 35379L;
  @JsxConstant
  public static final long MAX_COMBINED_TEXTURE_IMAGE_UNITS = 35661L;
  @JsxConstant
  public static final long MAX_COMBINED_UNIFORM_BLOCKS = 35374L;
  @JsxConstant
  public static final long MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 35377L;
  @JsxConstant
  public static final long MAX_CUBE_MAP_TEXTURE_SIZE = 34076L;
  @JsxConstant
  public static final long MAX_DRAW_BUFFERS = 34852L;
  @JsxConstant
  public static final long MAX_ELEMENTS_INDICES = 33001L;
  @JsxConstant
  public static final long MAX_ELEMENTS_VERTICES = 33000L;
  @JsxConstant
  public static final long MAX_ELEMENT_INDEX = 36203L;
  @JsxConstant
  public static final long MAX_FRAGMENT_INPUT_COMPONENTS = 37157L;
  @JsxConstant
  public static final long MAX_FRAGMENT_UNIFORM_BLOCKS = 35373L;
  @JsxConstant
  public static final long MAX_FRAGMENT_UNIFORM_COMPONENTS = 35657L;
  @JsxConstant
  public static final long MAX_FRAGMENT_UNIFORM_VECTORS = 36349L;
  @JsxConstant
  public static final long MAX_PROGRAM_TEXEL_OFFSET = 35077L;
  @JsxConstant
  public static final long MAX_RENDERBUFFER_SIZE = 34024L;
  @JsxConstant
  public static final long MAX_SAMPLES = 36183L;
  @JsxConstant
  public static final long MAX_SERVER_WAIT_TIMEOUT = 37137L;
  @JsxConstant
  public static final long MAX_TEXTURE_IMAGE_UNITS = 34930L;
  @JsxConstant
  public static final long MAX_TEXTURE_LOD_BIAS = 34045L;
  @JsxConstant
  public static final long MAX_TEXTURE_SIZE = 3379L;
  @JsxConstant
  public static final long MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 35978L;
  @JsxConstant
  public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 35979L;
  @JsxConstant
  public static final long MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 35968L;
  @JsxConstant
  public static final long MAX_UNIFORM_BLOCK_SIZE = 35376L;
  @JsxConstant
  public static final long MAX_UNIFORM_BUFFER_BINDINGS = 35375L;
  @JsxConstant
  public static final long MAX_VARYING_COMPONENTS = 35659L;
  @JsxConstant
  public static final long MAX_VARYING_VECTORS = 36348L;
  @JsxConstant
  public static final long MAX_VERTEX_ATTRIBS = 34921L;
  @JsxConstant
  public static final long MAX_VERTEX_OUTPUT_COMPONENTS = 37154L;
  @JsxConstant
  public static final long MAX_VERTEX_TEXTURE_IMAGE_UNITS = 35660L;
  @JsxConstant
  public static final long MAX_VERTEX_UNIFORM_BLOCKS = 35371L;
  @JsxConstant
  public static final long MAX_VERTEX_UNIFORM_COMPONENTS = 35658L;
  @JsxConstant
  public static final long MAX_VERTEX_UNIFORM_VECTORS = 36347L;
  @JsxConstant
  public static final long MAX_VIEWPORT_DIMS = 3386L;
  @JsxConstant
  public static final long MEDIUM_FLOAT = 36337L;
  @JsxConstant
  public static final long MEDIUM_INT = 36340L;
  @JsxConstant
  public static final long MIN = 32775L;
  @JsxConstant
  public static final long MIN_PROGRAM_TEXEL_OFFSET = 35076L;
  @JsxConstant
  public static final long MIRRORED_REPEAT = 33648L;
  @JsxConstant
  public static final long NEAREST = 9728L;
  @JsxConstant
  public static final long NEAREST_MIPMAP_LINEAR = 9986L;
  @JsxConstant
  public static final long NEAREST_MIPMAP_NEAREST = 9984L;
  @JsxConstant
  public static final long NEVER = 512L;
  @JsxConstant
  public static final long NICEST = 4354L;
  @JsxConstant
  public static final long NONE = 0L;
  @JsxConstant
  public static final long NOTEQUAL = 517L;
  @JsxConstant
  public static final long NO_ERROR = 0L;
  @JsxConstant
  public static final long OBJECT_TYPE = 37138L;
  @JsxConstant
  public static final long ONE = 1L;
  @JsxConstant
  public static final long ONE_MINUS_CONSTANT_ALPHA = 32772L;
  @JsxConstant
  public static final long ONE_MINUS_CONSTANT_COLOR = 32770L;
  @JsxConstant
  public static final long ONE_MINUS_DST_ALPHA = 773L;
  @JsxConstant
  public static final long ONE_MINUS_DST_COLOR = 775L;
  @JsxConstant
  public static final long ONE_MINUS_SRC_ALPHA = 771L;
  @JsxConstant
  public static final long ONE_MINUS_SRC_COLOR = 769L;
  @JsxConstant
  public static final long OUT_OF_MEMORY = 1285L;
  @JsxConstant
  public static final long PACK_ALIGNMENT = 3333L;
  @JsxConstant
  public static final long PACK_ROW_LENGTH = 3330L;
  @JsxConstant
  public static final long PACK_SKIP_PIXELS = 3332L;
  @JsxConstant
  public static final long PACK_SKIP_ROWS = 3331L;
  @JsxConstant
  public static final long PIXEL_PACK_BUFFER = 35051L;
  @JsxConstant
  public static final long PIXEL_PACK_BUFFER_BINDING = 35053L;
  @JsxConstant
  public static final long PIXEL_UNPACK_BUFFER = 35052L;
  @JsxConstant
  public static final long PIXEL_UNPACK_BUFFER_BINDING = 35055L;
  @JsxConstant
  public static final long POINTS = 0L;
  @JsxConstant
  public static final long POLYGON_OFFSET_FACTOR = 32824L;
  @JsxConstant
  public static final long POLYGON_OFFSET_FILL = 32823L;
  @JsxConstant
  public static final long POLYGON_OFFSET_UNITS = 10752L;
  @JsxConstant
  public static final long QUERY_RESULT = 34918L;
  @JsxConstant
  public static final long QUERY_RESULT_AVAILABLE = 34919L;
  @JsxConstant
  public static final long R11F_G11F_B10F = 35898L;
  @JsxConstant
  public static final long R16F = 33325L;
  @JsxConstant
  public static final long R16I = 33331L;
  @JsxConstant
  public static final long R16UI = 33332L;
  @JsxConstant
  public static final long R32F = 33326L;
  @JsxConstant
  public static final long R32I = 33333L;
  @JsxConstant
  public static final long R32UI = 33334L;
  @JsxConstant
  public static final long R8 = 33321L;
  @JsxConstant
  public static final long R8I = 33329L;
  @JsxConstant
  public static final long R8UI = 33330L;
  @JsxConstant
  public static final long R8_SNORM = 36756L;
  @JsxConstant
  public static final long RASTERIZER_DISCARD = 35977L;
  @JsxConstant
  public static final long READ_BUFFER = 3074L;
  @JsxConstant
  public static final long READ_FRAMEBUFFER = 36008L;
  @JsxConstant
  public static final long READ_FRAMEBUFFER_BINDING = 36010L;
  @JsxConstant
  public static final long RED = 6403L;
  @JsxConstant
  public static final long RED_BITS = 3410L;
  @JsxConstant
  public static final long RED_INTEGER = 36244L;
  @JsxConstant
  public static final long RENDERBUFFER = 36161L;
  @JsxConstant
  public static final long RENDERBUFFER_ALPHA_SIZE = 36179L;
  @JsxConstant
  public static final long RENDERBUFFER_BINDING = 36007L;
  @JsxConstant
  public static final long RENDERBUFFER_BLUE_SIZE = 36178L;
  @JsxConstant
  public static final long RENDERBUFFER_DEPTH_SIZE = 36180L;
  @JsxConstant
  public static final long RENDERBUFFER_GREEN_SIZE = 36177L;
  @JsxConstant
  public static final long RENDERBUFFER_HEIGHT = 36163L;
  @JsxConstant
  public static final long RENDERBUFFER_INTERNAL_FORMAT = 36164L;
  @JsxConstant
  public static final long RENDERBUFFER_RED_SIZE = 36176L;
  @JsxConstant
  public static final long RENDERBUFFER_SAMPLES = 36011L;
  @JsxConstant
  public static final long RENDERBUFFER_STENCIL_SIZE = 36181L;
  @JsxConstant
  public static final long RENDERBUFFER_WIDTH = 36162L;
  @JsxConstant
  public static final long RENDERER = 7937L;
  @JsxConstant
  public static final long REPEAT = 10497L;
  @JsxConstant
  public static final long REPLACE = 7681L;
  @JsxConstant
  public static final long RG = 33319L;
  @JsxConstant
  public static final long RG16F = 33327L;
  @JsxConstant
  public static final long RG16I = 33337L;
  @JsxConstant
  public static final long RG16UI = 33338L;
  @JsxConstant
  public static final long RG32F = 33328L;
  @JsxConstant
  public static final long RG32I = 33339L;
  @JsxConstant
  public static final long RG32UI = 33340L;
  @JsxConstant
  public static final long RG8 = 33323L;
  @JsxConstant
  public static final long RG8I = 33335L;
  @JsxConstant
  public static final long RG8UI = 33336L;
  @JsxConstant
  public static final long RG8_SNORM = 36757L;
  @JsxConstant
  public static final long RGB = 6407L;
  @JsxConstant
  public static final long RGB10_A2 = 32857L;
  @JsxConstant
  public static final long RGB10_A2UI = 36975L;
  @JsxConstant
  public static final long RGB16F = 34843L;
  @JsxConstant
  public static final long RGB16I = 36233L;
  @JsxConstant
  public static final long RGB16UI = 36215L;
  @JsxConstant
  public static final long RGB32F = 34837L;
  @JsxConstant
  public static final long RGB32I = 36227L;
  @JsxConstant
  public static final long RGB32UI = 36209L;
  @JsxConstant
  public static final long RGB565 = 36194L;
  @JsxConstant
  public static final long RGB5_A1 = 32855L;
  @JsxConstant
  public static final long RGB8 = 32849L;
  @JsxConstant
  public static final long RGB8I = 36239L;
  @JsxConstant
  public static final long RGB8UI = 36221L;
  @JsxConstant
  public static final long RGB8_SNORM = 36758L;
  @JsxConstant
  public static final long RGB9_E5 = 35901L;
  @JsxConstant
  public static final long RGBA = 6408L;
  @JsxConstant
  public static final long RGBA16F = 34842L;
  @JsxConstant
  public static final long RGBA16I = 36232L;
  @JsxConstant
  public static final long RGBA16UI = 36214L;
  @JsxConstant
  public static final long RGBA32F = 34836L;
  @JsxConstant
  public static final long RGBA32I = 36226L;
  @JsxConstant
  public static final long RGBA32UI = 36208L;
  @JsxConstant
  public static final long RGBA4 = 32854L;
  @JsxConstant
  public static final long RGBA8 = 32856L;
  @JsxConstant
  public static final long RGBA8I = 36238L;
  @JsxConstant
  public static final long RGBA8UI = 36220L;
  @JsxConstant
  public static final long RGBA8_SNORM = 36759L;
  @JsxConstant
  public static final long RGBA_INTEGER = 36249L;
  @JsxConstant
  public static final long RGB_INTEGER = 36248L;
  @JsxConstant
  public static final long RG_INTEGER = 33320L;
  @JsxConstant
  public static final long SAMPLER_2D = 35678L;
  @JsxConstant
  public static final long SAMPLER_2D_ARRAY = 36289L;
  @JsxConstant
  public static final long SAMPLER_2D_ARRAY_SHADOW = 36292L;
  @JsxConstant
  public static final long SAMPLER_2D_SHADOW = 35682L;
  @JsxConstant
  public static final long SAMPLER_3D = 35679L;
  @JsxConstant
  public static final long SAMPLER_BINDING = 35097L;
  @JsxConstant
  public static final long SAMPLER_CUBE = 35680L;
  @JsxConstant
  public static final long SAMPLER_CUBE_SHADOW = 36293L;
  @JsxConstant
  public static final long SAMPLES = 32937L;
  @JsxConstant
  public static final long SAMPLE_ALPHA_TO_COVERAGE = 32926L;
  @JsxConstant
  public static final long SAMPLE_BUFFERS = 32936L;
  @JsxConstant
  public static final long SAMPLE_COVERAGE = 32928L;
  @JsxConstant
  public static final long SAMPLE_COVERAGE_INVERT = 32939L;
  @JsxConstant
  public static final long SAMPLE_COVERAGE_VALUE = 32938L;
  @JsxConstant
  public static final long SCISSOR_BOX = 3088L;
  @JsxConstant
  public static final long SCISSOR_TEST = 3089L;
  @JsxConstant
  public static final long SEPARATE_ATTRIBS = 35981L;
  @JsxConstant
  public static final long SHADER_TYPE = 35663L;
  @JsxConstant
  public static final long SHADING_LANGUAGE_VERSION = 35724L;
  @JsxConstant
  public static final long SHORT = 5122L;
  @JsxConstant
  public static final long SIGNALED = 37145L;
  @JsxConstant
  public static final long SIGNED_NORMALIZED = 36764L;
  @JsxConstant
  public static final long SRC_ALPHA = 770L;
  @JsxConstant
  public static final long SRC_ALPHA_SATURATE = 776L;
  @JsxConstant
  public static final long SRC_COLOR = 768L;
  @JsxConstant
  public static final long SRGB = 35904L;
  @JsxConstant
  public static final long SRGB8 = 35905L;
  @JsxConstant
  public static final long SRGB8_ALPHA8 = 35907L;
  @JsxConstant
  public static final long STATIC_COPY = 35046L;
  @JsxConstant
  public static final long STATIC_DRAW = 35044L;
  @JsxConstant
  public static final long STATIC_READ = 35045L;
  @JsxConstant
  public static final long STENCIL = 6146L;
  @JsxConstant
  public static final long STENCIL_ATTACHMENT = 36128L;
  @JsxConstant
  public static final long STENCIL_BACK_FAIL = 34817L;
  @JsxConstant
  public static final long STENCIL_BACK_FUNC = 34816L;
  @JsxConstant
  public static final long STENCIL_BACK_PASS_DEPTH_FAIL = 34818L;
  @JsxConstant
  public static final long STENCIL_BACK_PASS_DEPTH_PASS = 34819L;
  @JsxConstant
  public static final long STENCIL_BACK_REF = 36003L;
  @JsxConstant
  public static final long STENCIL_BACK_VALUE_MASK = 36004L;
  @JsxConstant
  public static final long STENCIL_BACK_WRITEMASK = 36005L;
  @JsxConstant
  public static final long STENCIL_BITS = 3415L;
  @JsxConstant
  public static final long STENCIL_BUFFER_BIT = 1024L;
  @JsxConstant
  public static final long STENCIL_CLEAR_VALUE = 2961L;
  @JsxConstant
  public static final long STENCIL_FAIL = 2964L;
  @JsxConstant
  public static final long STENCIL_FUNC = 2962L;
  @JsxConstant
  public static final long STENCIL_INDEX = 6401L;
  @JsxConstant
  public static final long STENCIL_INDEX8 = 36168L;
  @JsxConstant
  public static final long STENCIL_PASS_DEPTH_FAIL = 2965L;
  @JsxConstant
  public static final long STENCIL_PASS_DEPTH_PASS = 2966L;
  @JsxConstant
  public static final long STENCIL_REF = 2967L;
  @JsxConstant
  public static final long STENCIL_TEST = 2960L;
  @JsxConstant
  public static final long STENCIL_VALUE_MASK = 2963L;
  @JsxConstant
  public static final long STENCIL_WRITEMASK = 2968L;
  @JsxConstant
  public static final long STREAM_COPY = 35042L;
  @JsxConstant
  public static final long STREAM_DRAW = 35040L;
  @JsxConstant
  public static final long STREAM_READ = 35041L;
  @JsxConstant
  public static final long SUBPIXEL_BITS = 3408L;
  @JsxConstant
  public static final long SYNC_CONDITION = 37139L;
  @JsxConstant
  public static final long SYNC_FENCE = 37142L;
  @JsxConstant
  public static final long SYNC_FLAGS = 37141L;
  @JsxConstant
  public static final long SYNC_FLUSH_COMMANDS_BIT = 1L;
  @JsxConstant
  public static final long SYNC_GPU_COMMANDS_COMPLETE = 37143L;
  @JsxConstant
  public static final long SYNC_STATUS = 37140L;
  @JsxConstant
  public static final long TEXTURE = 5890L;
  @JsxConstant
  public static final long TEXTURE0 = 33984L;
  @JsxConstant
  public static final long TEXTURE1 = 33985L;
  @JsxConstant
  public static final long TEXTURE10 = 33994L;
  @JsxConstant
  public static final long TEXTURE11 = 33995L;
  @JsxConstant
  public static final long TEXTURE12 = 33996L;
  @JsxConstant
  public static final long TEXTURE13 = 33997L;
  @JsxConstant
  public static final long TEXTURE14 = 33998L;
  @JsxConstant
  public static final long TEXTURE15 = 33999L;
  @JsxConstant
  public static final long TEXTURE16 = 34000L;
  @JsxConstant
  public static final long TEXTURE17 = 34001L;
  @JsxConstant
  public static final long TEXTURE18 = 34002L;
  @JsxConstant
  public static final long TEXTURE19 = 34003L;
  @JsxConstant
  public static final long TEXTURE2 = 33986L;
  @JsxConstant
  public static final long TEXTURE20 = 34004L;
  @JsxConstant
  public static final long TEXTURE21 = 34005L;
  @JsxConstant
  public static final long TEXTURE22 = 34006L;
  @JsxConstant
  public static final long TEXTURE23 = 34007L;
  @JsxConstant
  public static final long TEXTURE24 = 34008L;
  @JsxConstant
  public static final long TEXTURE25 = 34009L;
  @JsxConstant
  public static final long TEXTURE26 = 34010L;
  @JsxConstant
  public static final long TEXTURE27 = 34011L;
  @JsxConstant
  public static final long TEXTURE28 = 34012L;
  @JsxConstant
  public static final long TEXTURE29 = 34013L;
  @JsxConstant
  public static final long TEXTURE3 = 33987L;
  @JsxConstant
  public static final long TEXTURE30 = 34014L;
  @JsxConstant
  public static final long TEXTURE31 = 34015L;
  @JsxConstant
  public static final long TEXTURE4 = 33988L;
  @JsxConstant
  public static final long TEXTURE5 = 33989L;
  @JsxConstant
  public static final long TEXTURE6 = 33990L;
  @JsxConstant
  public static final long TEXTURE7 = 33991L;
  @JsxConstant
  public static final long TEXTURE8 = 33992L;
  @JsxConstant
  public static final long TEXTURE9 = 33993L;
  @JsxConstant
  public static final long TEXTURE_2D = 3553L;
  @JsxConstant
  public static final long TEXTURE_2D_ARRAY = 35866L;
  @JsxConstant
  public static final long TEXTURE_3D = 32879L;
  @JsxConstant
  public static final long TEXTURE_BASE_LEVEL = 33084L;
  @JsxConstant
  public static final long TEXTURE_BINDING_2D = 32873L;
  @JsxConstant
  public static final long TEXTURE_BINDING_2D_ARRAY = 35869L;
  @JsxConstant
  public static final long TEXTURE_BINDING_3D = 32874L;
  @JsxConstant
  public static final long TEXTURE_BINDING_CUBE_MAP = 34068L;
  @JsxConstant
  public static final long TEXTURE_COMPARE_FUNC = 34893L;
  @JsxConstant
  public static final long TEXTURE_COMPARE_MODE = 34892L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP = 34067L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_NEGATIVE_X = 34070L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_POSITIVE_X = 34069L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_POSITIVE_Y = 34071L;
  @JsxConstant
  public static final long TEXTURE_CUBE_MAP_POSITIVE_Z = 34073L;
  @JsxConstant
  public static final long TEXTURE_IMMUTABLE_FORMAT = 37167L;
  @JsxConstant
  public static final long TEXTURE_IMMUTABLE_LEVELS = 33503L;
  @JsxConstant
  public static final long TEXTURE_MAG_FILTER = 10240L;
  @JsxConstant
  public static final long TEXTURE_MAX_LEVEL = 33085L;
  @JsxConstant
  public static final long TEXTURE_MAX_LOD = 33083L;
  @JsxConstant
  public static final long TEXTURE_MIN_FILTER = 10241L;
  @JsxConstant
  public static final long TEXTURE_MIN_LOD = 33082L;
  @JsxConstant
  public static final long TEXTURE_WRAP_R = 32882L;
  @JsxConstant
  public static final long TEXTURE_WRAP_S = 10242L;
  @JsxConstant
  public static final long TEXTURE_WRAP_T = 10243L;
  @JsxConstant
  public static final long TIMEOUT_EXPIRED = 37147L;
  @JsxConstant
  public static final long TIMEOUT_IGNORED = -1L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK = 36386L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_ACTIVE = 36388L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BINDING = 36389L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BUFFER = 35982L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BUFFER_BINDING = 35983L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BUFFER_MODE = 35967L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BUFFER_SIZE = 35973L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_BUFFER_START = 35972L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_PAUSED = 36387L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 35976L;
  @JsxConstant
  public static final long TRANSFORM_FEEDBACK_VARYINGS = 35971L;
  @JsxConstant
  public static final long TRIANGLES = 4L;
  @JsxConstant
  public static final long TRIANGLE_FAN = 6L;
  @JsxConstant
  public static final long TRIANGLE_STRIP = 5L;
  @JsxConstant
  public static final long UNIFORM_ARRAY_STRIDE = 35388L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_ACTIVE_UNIFORMS = 35394L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 35395L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_BINDING = 35391L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_DATA_SIZE = 35392L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_INDEX = 35386L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 35398L;
  @JsxConstant
  public static final long UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 35396L;
  @JsxConstant
  public static final long UNIFORM_BUFFER = 35345L;
  @JsxConstant
  public static final long UNIFORM_BUFFER_BINDING = 35368L;
  @JsxConstant
  public static final long UNIFORM_BUFFER_OFFSET_ALIGNMENT = 35380L;
  @JsxConstant
  public static final long UNIFORM_BUFFER_SIZE = 35370L;
  @JsxConstant
  public static final long UNIFORM_BUFFER_START = 35369L;
  @JsxConstant
  public static final long UNIFORM_IS_ROW_MAJOR = 35390L;
  @JsxConstant
  public static final long UNIFORM_MATRIX_STRIDE = 35389L;
  @JsxConstant
  public static final long UNIFORM_OFFSET = 35387L;
  @JsxConstant
  public static final long UNIFORM_SIZE = 35384L;
  @JsxConstant
  public static final long UNIFORM_TYPE = 35383L;
  @JsxConstant
  public static final long UNPACK_ALIGNMENT = 3317L;
  @JsxConstant
  public static final long UNPACK_COLORSPACE_CONVERSION_WEBGL = 37443L;
  @JsxConstant
  public static final long UNPACK_FLIP_Y_WEBGL = 37440L;
  @JsxConstant
  public static final long UNPACK_IMAGE_HEIGHT = 32878L;
  @JsxConstant
  public static final long UNPACK_PREMULTIPLY_ALPHA_WEBGL = 37441L;
  @JsxConstant
  public static final long UNPACK_ROW_LENGTH = 3314L;
  @JsxConstant
  public static final long UNPACK_SKIP_IMAGES = 32877L;
  @JsxConstant
  public static final long UNPACK_SKIP_PIXELS = 3316L;
  @JsxConstant
  public static final long UNPACK_SKIP_ROWS = 3315L;
  @JsxConstant
  public static final long UNSIGNALED = 37144L;
  @JsxConstant
  public static final long UNSIGNED_BYTE = 5121L;
  @JsxConstant
  public static final long UNSIGNED_INT = 5125L;
  @JsxConstant
  public static final long UNSIGNED_INT_10F_11F_11F_REV = 35899L;
  @JsxConstant
  public static final long UNSIGNED_INT_24_8 = 34042L;
  @JsxConstant
  public static final long UNSIGNED_INT_2_10_10_10_REV = 33640L;
  @JsxConstant
  public static final long UNSIGNED_INT_5_9_9_9_REV = 35902L;
  @JsxConstant
  public static final long UNSIGNED_INT_SAMPLER_2D = 36306L;
  @JsxConstant
  public static final long UNSIGNED_INT_SAMPLER_2D_ARRAY = 36311L;
  @JsxConstant
  public static final long UNSIGNED_INT_SAMPLER_3D = 36307L;
  @JsxConstant
  public static final long UNSIGNED_INT_SAMPLER_CUBE = 36308L;
  @JsxConstant
  public static final long UNSIGNED_INT_VEC2 = 36294L;
  @JsxConstant
  public static final long UNSIGNED_INT_VEC3 = 36295L;
  @JsxConstant
  public static final long UNSIGNED_INT_VEC4 = 36296L;
  @JsxConstant
  public static final long UNSIGNED_NORMALIZED = 35863L;
  @JsxConstant
  public static final long UNSIGNED_SHORT = 5123L;
  @JsxConstant
  public static final long UNSIGNED_SHORT_4_4_4_4 = 32819L;
  @JsxConstant
  public static final long UNSIGNED_SHORT_5_5_5_1 = 32820L;
  @JsxConstant
  public static final long UNSIGNED_SHORT_5_6_5 = 33635L;
  @JsxConstant
  public static final long VALIDATE_STATUS = 35715L;
  @JsxConstant
  public static final long VENDOR = 7936L;
  @JsxConstant
  public static final long VERSION = 7938L;
  @JsxConstant
  public static final long VERTEX_ARRAY_BINDING = 34229L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 34975L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_DIVISOR = 35070L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_ENABLED = 34338L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_INTEGER = 35069L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_NORMALIZED = 34922L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_POINTER = 34373L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_SIZE = 34339L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_STRIDE = 34340L;
  @JsxConstant
  public static final long VERTEX_ATTRIB_ARRAY_TYPE = 34341L;
  @JsxConstant
  public static final long VERTEX_SHADER = 35633L;
  @JsxConstant
  public static final long VIEWPORT = 2978L;
  @JsxConstant
  public static final long WAIT_FAILED = 37149L;
  @JsxConstant
  public static final long ZERO = 0L;
  
  @JsxConstructor
  public WebGL2RenderingContext() {}
}
