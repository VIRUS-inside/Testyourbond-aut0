package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;






















































































public class ErrorMessages_zh
  extends ListResourceBundle
{
  public ErrorMessages_zh() {}
  
  public Object[][] getContents()
  {
    return new Object[][] { { "MULTIPLE_STYLESHEET_ERR", "同一文件中定义了多个样式表。" }, { "TEMPLATE_REDEF_ERR", "此样式表中已经定义了模板“{0}”。" }, { "TEMPLATE_UNDEF_ERR", "此样式表中未定义模板“{0}”。" }, { "VARIABLE_REDEF_ERR", "同一作用域中多次定义了变量“{0}”。" }, { "VARIABLE_UNDEF_ERR", "未定义变量或参数“{0}”。" }, { "CLASS_NOT_FOUND_ERR", "找不到类“{0}”。" }, { "METHOD_NOT_FOUND_ERR", "找不到外部方法“{0}”（必须是 public）。" }, { "ARGUMENT_CONVERSION_ERR", "无法将调用中的参数／返回类型转换为方法“{0}”" }, { "FILE_NOT_FOUND_ERR", "找不到文件或 URI“{0}”。" }, { "INVALID_URI_ERR", "URI“{0}”无效。" }, { "FILE_ACCESS_ERR", "无法打开文件或 URI“{0}”。" }, { "MISSING_ROOT_ERR", "预期存在 <xsl:stylesheet> 或 <xsl:transform> 元素。" }, { "NAMESPACE_UNDEF_ERR", "未声明名称空间前缀“{0}”。" }, { "FUNCTION_RESOLVE_ERR", "无法解析对函数“{0}”的调用。" }, { "NEED_LITERAL_ERR", "“{0}”的自变量必须是文字串。" }, { "XPATH_PARSER_ERR", "解析 XPath 表达式“{0}”时出错。" }, { "REQUIRED_ATTR_ERR", "缺少必需的属性“{0}”。" }, { "ILLEGAL_CHAR_ERR", "XPath 表达式中的字符“{0}”非法。" }, { "ILLEGAL_PI_ERR", "处理指令的名称“{0}”非法。" }, { "STRAY_ATTRIBUTE_ERR", "属性“{0}”在元素外。" }, { "ILLEGAL_ATTRIBUTE_ERR", "属性“{0}”非法。" }, { "CIRCULAR_INCLUDE_ERR", "循环 import／include。已装入样式表“{0}”。" }, { "RESULT_TREE_SORT_ERR", "无法对结果树片段排序（<xsl:sort> 元素被忽略）。必须在创建结果树时对节点进行排序。" }, { "SYMBOLS_REDEF_ERR", "已经定义了十进制格式编排“{0}”。" }, { "XSL_VERSION_ERR", "XSLTC 不支持 XSL V{0}。" }, { "CIRCULAR_VARIABLE_ERR", "“{0}”中的循环变量／参数引用。" }, { "ILLEGAL_BINARY_OP_ERR", "二进制表达式的运算符未知。" }, { "ILLEGAL_ARG_ERR", "函数调用的参数非法。" }, { "DOCUMENT_ARG_ERR", "函数 document() 的第二个参数必须是节点集。" }, { "MISSING_WHEN_ERR", "<xsl:choose> 中至少要有一个 <xsl:when> 元素。" }, { "MULTIPLE_OTHERWISE_ERR", "<xsl:choose> 中只允许有一个 <xsl:otherwise> 元素。" }, { "STRAY_OTHERWISE_ERR", "<xsl:otherwise> 只能在 <xsl:choose> 中使用。" }, { "STRAY_WHEN_ERR", "<xsl:when> 只能在 <xsl:choose> 中使用。" }, { "WHEN_ELEMENT_ERR", "<xsl:choose> 中只允许使用 <xsl:when> 和 <xsl:otherwise>。" }, { "UNNAMED_ATTRIBSET_ERR", "<xsl:attribute-set> 缺少“name”属性。" }, { "ILLEGAL_CHILD_ERR", "子元素非法。" }, { "ILLEGAL_ELEM_NAME_ERR", "不能调用元素“{0}”" }, { "ILLEGAL_ATTR_NAME_ERR", "不能调用属性“{0}”" }, { "ILLEGAL_TEXT_NODE_ERR", "文本数据在顶级 <xsl:stylesheet> 元素外。" }, { "SAX_PARSER_CONFIG_ERR", "JAXP 解析器没有正确配置" }, { "INTERNAL_ERR", "不可恢复的 XSLTC 内部错误：“{0}”" }, { "UNSUPPORTED_XSL_ERR", "不受支持的 XSL 元素“{0}”。" }, { "UNSUPPORTED_EXT_ERR", "未被识别的 XSLTC 扩展名“{0}”。" }, { "MISSING_XSLT_URI_ERR", "输入文档不是样式表（XSL 名称空间没有在根元素中声明）。" }, { "MISSING_XSLT_TARGET_ERR", "找不到样式表目标“{0}”。" }, { "NOT_IMPLEMENTED_ERR", "没有实现：“{0}”。" }, { "NOT_STYLESHEET_ERR", "输入文档不包含 XSL 样式表。" }, { "ELEMENT_PARSE_ERR", "无法解析元素“{0}”" }, { "KEY_USE_ATTR_ERR", "<key> 的 use 属性必须是 node、node-set、string 或 number。" }, { "OUTPUT_VERSION_ERR", "输出 XML 文档的版本应当是 1.0" }, { "ILLEGAL_RELAT_OP_ERR", "关系表达式的运算符未知" }, { "ATTRIBSET_UNDEF_ERR", "试图使用不存在的属性集“{0}”。" }, { "ATTR_VAL_TEMPLATE_ERR", "无法解析属性值模板“{0}”。" }, { "UNKNOWN_SIG_TYPE_ERR", "类“{0}”的特征符中的数据类型未知。" }, { "DATA_CONVERSION_ERR", "无法将数据类型“{0}”转换成“{1}”。" }, { "NO_TRANSLET_CLASS_ERR", "此 Templates 不包含有效的 translet 类定义。" }, { "NO_MAIN_TRANSLET_ERR", "此 Templates 不包含名为“{0}”的类。" }, { "TRANSLET_CLASS_ERR", "无法装入 translet 类“{0}”。" }, { "TRANSLET_OBJECT_ERR", "Translet 类已装入，但无法创建 translet 实例。" }, { "ERROR_LISTENER_NULL_ERR", "试图将“{0}”的 ErrorListener 设置为空" }, { "JAXP_UNKNOWN_SOURCE_ERR", "XSLTC 只支持 StreamSource、SAXSource 和 DOMSource" }, { "JAXP_NO_SOURCE_ERR", "传递给“{0}”的源对象没有内容。" }, { "JAXP_COMPILE_ERR", "无法编译样式表" }, { "JAXP_INVALID_ATTR_ERR", "TransformerFactory 无法识别属性“{0}”。" }, { "JAXP_SET_RESULT_ERR", "setResult() 必须在 startDocument() 之前调用。" }, { "JAXP_NO_TRANSLET_ERR", "Transformer 没有封装的 translet 对象。" }, { "JAXP_NO_HANDLER_ERR", "没有为转换结果定义输出处理器。" }, { "JAXP_NO_RESULT_ERR", "传递给“{0}”的 Result 对象无效。" }, { "JAXP_UNKNOWN_PROP_ERR", "试图访问无效的 Transformer 属性“{0}”。" }, { "SAX2DOM_ADAPTER_ERR", "无法创建 SAX2DOM 适配器：“{0}”。" }, { "XSLTC_SOURCE_ERR", "没有设置 systemId 就调用了 XSLTCSource.build()。" }, { "ER_RESULT_NULL", "结果不应为空" }, { "JAXP_INVALID_SET_PARAM_VALUE", "参数 {0} 的值必须为有效的 Java 对象" }, { "COMPILE_STDIN_ERR", "-i 选项必须与 -o 选项一起使用。" }, { "COMPILE_USAGE_STR", "SYNOPSIS\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <output>]\n      [-d <directory>] [-j <jarfile>] [-p <package>]\n      [-n] [-x] [-u] [-v] [-h] { <stylesheet> | -i }\n\nOPTIONS\n   -o <output>    将名称 <output> 指定给生成的\n                  translet。缺省情况下，translet 名称\n                  派生自 <stylesheet> 名称。\n                  如果编译多个样式表，则忽略此选项。\n-d <directory> 指定 translet 的目标目录\n   -j <jarfile>   将 translet 类打包成命名为 <jarfile>\n                  的 jar 文件\n   -p <package>   为所有生成的 translet 类\n                  指定软件包名称的前缀。\n   -n             启用模板在线（平均缺省\n                  行为更佳）。\n   -x             打开额外调试消息输出\n   -u             将 <stylesheet> 自变量解释为 URL\n   -i             强制编译器从 stdin 读入样式表\n   -v             打印编译器的版本\n   -h             打印此用法语句\n" }, { "TRANSFORM_USAGE_STR", "SYNOPSIS \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <jarfile>]\n      [-x] [-n <iterations>] {-u <document_url> | <document>}\n      <class> [<param1>=<value1> ...]\n\n   使用 translet <class> 来转换指定为 <document>\n   的 XML 文档。translet <class> 要么在\n   用户的 CLASSPATH 中，要么在任意指定的 <jarfile> 中。\nOPTIONS\n   -j <jarfile>    指定装入 translet 的 jarfile\n   -x              打开额外调试消息输出\n   -n <iterations> 运行转换过程 <iterations> 数次并\n                   显示概要分析信息\n   -u <document_url> 将 XML 输入指定为 URL\n" }, { "STRAY_SORT_ERR", "<xsl:sort> 只能在 <xsl:for-each> 或 <xsl:apply-templates> 中使用。" }, { "UNSUPPORTED_ENCODING", "此 JVM 中不支持输出编码“{0}”。" }, { "SYNTAX_ERR", "“{0}”中出现语法错误。" }, { "CONSTRUCTOR_NOT_FOUND", "找不到外部构造函数“{0}”。" }, { "NO_JAVA_FUNCT_THIS_REF", "非 static Java 函数“{0}”的第一个参数不是有效的对象引用。" }, { "TYPE_CHECK_ERR", "检查表达式“{0}”的类型时出错。" }, { "TYPE_CHECK_UNK_LOC_ERR", "检查未知位置的表达式类型时出错。" }, { "ILLEGAL_CMDLINE_OPTION_ERR", "命令行选项“{0}”无效。" }, { "CMDLINE_OPT_MISSING_ARG_ERR", "命令行选项“{0}”缺少必需的自变量。" }, { "WARNING_PLUS_WRAPPED_MSG", "警告：“{0}”\n    ：{1}" }, { "WARNING_MSG", "警告：“{0}”" }, { "FATAL_ERR_PLUS_WRAPPED_MSG", "致命错误：“{0}”\n        ：{1}" }, { "FATAL_ERR_MSG", "致命错误：“{0}”" }, { "ERROR_PLUS_WRAPPED_MSG", "错误：{0}“\n    ：{1}" }, { "ERROR_MSG", "错误：“{0}”" }, { "TRANSFORM_WITH_TRANSLET_STR", "使用 translet“{0}”转换" }, { "TRANSFORM_WITH_JAR_STR", "使用 translet“{0}”从 jar 文件“{1}”转换" }, { "COULD_NOT_CREATE_TRANS_FACT", "无法创建 TransformerFactory 类“{0}”的实例。" }, { "TRANSLET_NAME_JAVA_CONFLICT", "由于名称“{0}”包含 Java 类名称中不允许使用的字符，因此不能将它用作 translet 类的名称。使用名称“{1}”来代替。" }, { "COMPILER_ERROR_KEY", "编译器错误：" }, { "COMPILER_WARNING_KEY", "编译器警告：" }, { "RUNTIME_ERROR_KEY", "Translet 错误：" }, { "INVALID_QNAME_ERR", "值必须为 QName 或为用空格分开的 QName 列表的属性具有值“{0}”" }, { "INVALID_NCNAME_ERR", "值必须为 NCName 的属性具有值“{0}”" }, { "INVALID_METHOD_IN_OUTPUT", "<xsl:output> 元素的 method 属性具有值“{0}”。此值必须为“xml”、“html”、“text” 或 qname-but-not-ncname 中的一个" }, { "JAXP_GET_FEATURE_NULL_NAME", "在 TransformerFactory.getFeature(String name) 中特征名不能为空。" }, { "JAXP_SET_FEATURE_NULL_NAME", "在 TransformerFactory.setFeature(String name, boolean value) 中特征名不能为空。" }, { "JAXP_UNSUPPORTED_FEATURE", "无法对此 TransformerFactory 设置特征“{0}”。" } };
  }
}
