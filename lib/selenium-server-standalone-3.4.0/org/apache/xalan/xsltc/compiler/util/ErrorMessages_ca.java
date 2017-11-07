package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;






















































































public class ErrorMessages_ca
  extends ListResourceBundle
{
  public ErrorMessages_ca() {}
  
  public Object[][] getContents()
  {
    return new Object[][] { { "MULTIPLE_STYLESHEET_ERR", "S'ha definit més d'un full d'estils en el mateix fitxer." }, { "TEMPLATE_REDEF_ERR", "La plantilla ''{0}'' ja està definida en aquest full d''estil. " }, { "TEMPLATE_UNDEF_ERR", "La plantilla ''{0}'' no està definida en aquest full d''estil. " }, { "VARIABLE_REDEF_ERR", "La variable ''{0}'' s''ha definit més d''una vegada en el mateix àmbit. " }, { "VARIABLE_UNDEF_ERR", "La variable o el paràmetre ''{0}'' no s''ha definit. " }, { "CLASS_NOT_FOUND_ERR", "No es pot trobar la classe ''{0}''." }, { "METHOD_NOT_FOUND_ERR", "No es pot trobar el mètode extern ''{0}'' (ha de ser públic)." }, { "ARGUMENT_CONVERSION_ERR", "No es pot convertir el tipus d''argument/retorn en la crida al mètode ''{0}''" }, { "FILE_NOT_FOUND_ERR", "No s''ha trobat el fitxer o URI ''{0}''. " }, { "INVALID_URI_ERR", "L''URI ''{0}'' no és vàlid." }, { "FILE_ACCESS_ERR", "No es pot obrir el fitxer o URI ''{0}''." }, { "MISSING_ROOT_ERR", "S'esperava l'element <xsl:stylesheet> o <xsl:transform>." }, { "NAMESPACE_UNDEF_ERR", "No s''ha declarat el prefix de l''espai de noms ''{0}''. " }, { "FUNCTION_RESOLVE_ERR", "No es pot resoldre la crida a la funció ''{0}''." }, { "NEED_LITERAL_ERR", "L''argument de ''{0}'' ha de ser una cadena de literals. " }, { "XPATH_PARSER_ERR", "Error en analitzar l''expressió XPath ''{0}''." }, { "REQUIRED_ATTR_ERR", "Falta l''atribut obligatori ''{0}''. " }, { "ILLEGAL_CHAR_ERR", "L''expressió XPath conté el caràcter no permès ''{0}''. " }, { "ILLEGAL_PI_ERR", "La instrucció de procés conté el nom no permès ''{0}''. " }, { "STRAY_ATTRIBUTE_ERR", "L''atribut ''{0}'' es troba fora de l''element. " }, { "ILLEGAL_ATTRIBUTE_ERR", "Atribut no permès ''{0}''." }, { "CIRCULAR_INCLUDE_ERR", "Import/include circular. El full d''estil ''{0}'' ja s''ha carregat. " }, { "RESULT_TREE_SORT_ERR", "Els fragments de l'arbre de resultats no es poden classificar (es passen per alt els elements <xsl:sort>). Heu de classificar els nodes quan creeu l'arbre de resultats." }, { "SYMBOLS_REDEF_ERR", "El formatatge decimal ''{0}'' ja està definit." }, { "XSL_VERSION_ERR", "XSLTC no dóna suport a la versió XSL ''{0}''. " }, { "CIRCULAR_VARIABLE_ERR", "Hi ha una referència de variable/paràmetre circular a ''{0}''." }, { "ILLEGAL_BINARY_OP_ERR", "L'operador de l'expressió binària és desconegut." }, { "ILLEGAL_ARG_ERR", "La crida de funció té arguments no permesos." }, { "DOCUMENT_ARG_ERR", "El segon argument de la funció document() ha de ser un conjunt de nodes." }, { "MISSING_WHEN_ERR", "Es necessita com a mínim un element <xsl:when> a <xsl:choose>." }, { "MULTIPLE_OTHERWISE_ERR", "Només es permet un element <xsl:otherwise> a <xsl:choose>." }, { "STRAY_OTHERWISE_ERR", "<xsl:otherwise> només es pot utilitzar dins de <xsl:choose>." }, { "STRAY_WHEN_ERR", "<xsl:when> només es pot utilitzar dins de <xsl:choose>." }, { "WHEN_ELEMENT_ERR", "A <xsl:choose> només es permeten els elements <xsl:when> i <xsl:otherwise>." }, { "UNNAMED_ATTRIBSET_ERR", "L'atribut 'name' falta a <xsl:attribute-set>." }, { "ILLEGAL_CHILD_ERR", "L'element subordinat no és permès." }, { "ILLEGAL_ELEM_NAME_ERR", "No podeu cridar un element ''{0}''" }, { "ILLEGAL_ATTR_NAME_ERR", "No podeu cridar un atribut ''{0}''" }, { "ILLEGAL_TEXT_NODE_ERR", "Hi ha dades fora de l'element de nivell superior <xsl:stylesheet>." }, { "SAX_PARSER_CONFIG_ERR", "L'analitzador JAXP no s'ha configurat correctament" }, { "INTERNAL_ERR", "S''ha produït un error intern d''XSLTC irrecuperable: ''{0}''" }, { "UNSUPPORTED_XSL_ERR", "L''element XSL ''{0}'' no té suport." }, { "UNSUPPORTED_EXT_ERR", "No es reconeix l''extensió XSLTC ''{0}''." }, { "MISSING_XSLT_URI_ERR", "El document d'entrada no és un full d'estils (l'espai de noms XSL no s'ha declarat en l'element arrel)." }, { "MISSING_XSLT_TARGET_ERR", "No s''ha pogut trobar la destinació del full d''estil ''{0}''." }, { "NOT_IMPLEMENTED_ERR", "No s''ha implementat: ''{0}''." }, { "NOT_STYLESHEET_ERR", "El document d'entrada no conté cap full d'estils XSL." }, { "ELEMENT_PARSE_ERR", "No s''ha pogut analitzar l''element ''{0}''" }, { "KEY_USE_ATTR_ERR", "L'atribut use de <key> ha de ser node, node-set, string o number." }, { "OUTPUT_VERSION_ERR", "La versió del document XML de sortida ha de ser 1.0" }, { "ILLEGAL_RELAT_OP_ERR", "L'operador de l'expressió relacional és desconegut." }, { "ATTRIBSET_UNDEF_ERR", "S''ha intentat utilitzar el conjunt d''atributs ''{0}'' que no existeix." }, { "ATTR_VAL_TEMPLATE_ERR", "No es pot analitzar la plantilla del valor d''atribut ''{0}''." }, { "UNKNOWN_SIG_TYPE_ERR", "Es desconeix el tipus de dades de la signatura de la classe ''{0}''." }, { "DATA_CONVERSION_ERR", "No es pot convertir el tipus de dades ''{0}'' a ''{1}''." }, { "NO_TRANSLET_CLASS_ERR", "Templates no conté cap definició de classe translet." }, { "NO_MAIN_TRANSLET_ERR", "Templates no conté cap classe amb el nom ''{0}''." }, { "TRANSLET_CLASS_ERR", "No s''ha pogut carregar la classe translet ''{0}''." }, { "TRANSLET_OBJECT_ERR", "La classe translet s'ha carregat, però no s'ha pogut crear la instància translet." }, { "ERROR_LISTENER_NULL_ERR", "S''ha intentat establir ErrorListener de ''{0}'' en nul" }, { "JAXP_UNKNOWN_SOURCE_ERR", "XSLTC només dóna suport a StreamSource, SAXSource i DOMSource." }, { "JAXP_NO_SOURCE_ERR", "L''objecte source passat a ''{0}'' no té contingut. " }, { "JAXP_COMPILE_ERR", "No s'ha pogut compilar el full d'estils." }, { "JAXP_INVALID_ATTR_ERR", "TransformerFactory no reconeix l''atribut ''{0}''." }, { "JAXP_SET_RESULT_ERR", "setResult() s'ha de cridar abans de startDocument()." }, { "JAXP_NO_TRANSLET_ERR", "Transformer no conté cap objecte translet." }, { "JAXP_NO_HANDLER_ERR", "No s'ha definit cap manejador de sortida per al resultat de transformació." }, { "JAXP_NO_RESULT_ERR", "L''objecte result passat a ''{0}'' no és vàlid. " }, { "JAXP_UNKNOWN_PROP_ERR", "S''ha intentat accedir a una propietat Transformer no vàlida ''{0}''." }, { "SAX2DOM_ADAPTER_ERR", "No s''ha pogut crear l''adaptador SAX2DOMr: ''{0}''." }, { "XSLTC_SOURCE_ERR", "S'ha cridat XSLTCSource.build() sense que s'hagués establert la identificació del sistema." }, { "ER_RESULT_NULL", "El resultat no ha de ser nul" }, { "JAXP_INVALID_SET_PARAM_VALUE", "El valor del paràmetre {0} ha de ser un objecte Java vàlid " }, { "COMPILE_STDIN_ERR", "L'opció -i s'ha d'utilitzar amb l'opció -o." }, { "COMPILE_USAGE_STR", "RESUM\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <sortida>]\n      [-d <directori>] [-j <fitxer_jar>] [-p <paquet>]\n      [-n] [-x] [-u] [-v] [-h] { <full_estil> | -i }\n\nOPCIONS\n   -o <sortida>    assigna el nom <sortida> al translet\n                  generat. Per defecte, el nom del translet s'obté\n                  del nom de <full_estils>. Aquesta opció\n no es té en compte si es compilen diversos fulls d'estils.\n   -d <directori> especifica un directori de destinació per al translet\n   -j <fitxer_jar>   empaqueta les classes translet en un fitxer jar del nom\n                  especificat com a <fitxer_jar>\n   -p <paquet> especifica un prefix de nom de paquet per a totes les classes\n                  translet generades.\n   -n habilita l'inlining (com a mitjana, el funcionament per defecte\n és millor).\n   -x             habilita la sortida de missatges de depuració addicionals\n   -u             interpreta els arguments del <full_estil> com URL\n   -i             obliga el compilador a llegir el full d'estil des de l'entrada estàndardn\n   -v          imprimeix la versió del compilador\n   -h             imprimeix aquesta sentència d'ús\n" }, { "TRANSFORM_USAGE_STR", "RESUM \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <fitxer_jar>]\n      [-x] [-n <iteracions>] {-u <url_document> | <document>}\n      <classe> [<param1>=<valor1> ...]\n\n   fa servir la <classe> translet per transformar un document XML \n   especificat com <document>. La <classe> translet es troba\n   o bé a la CLASSPATH de l'usuari o bé al <fitxer_jar> que es pot especificar opcionalment.\nOPCIONS\n   -j <fitxer_jar>    especifica un fitxer jar des del qual es carrega la classe translet\n   -x              habilita la sortida de missatges de depuració addicionals \n   -n <iteracions> executa la transformació el nombre de vegades <iteracions> i\n                   mostra la informació de perfil\n   -u <url_document> especifica el document d'entrada XML com una URL\n" }, { "STRAY_SORT_ERR", "<xsl:sort> només es pot utilitzar amb <xsl:for-each> o <xsl:apply-templates>." }, { "UNSUPPORTED_ENCODING", "A aquesta JVM, no es dóna suport a la codificació de sortida ''{0}''." }, { "SYNTAX_ERR", "S''ha produït un error de sintaxi a ''{0}''." }, { "CONSTRUCTOR_NOT_FOUND", "No es pot trobar el constructor extern ''{0}''." }, { "NO_JAVA_FUNCT_THIS_REF", "El primer argument de la funció Java no static ''{0}'' no és una referència d''objecte vàlida. " }, { "TYPE_CHECK_ERR", "S''ha produït un error en comprovar el tipus de l''expressió ''{0}''." }, { "TYPE_CHECK_UNK_LOC_ERR", "S'ha produït un error en comprovar el tipus d'expressió en una ubicació desconeguda." }, { "ILLEGAL_CMDLINE_OPTION_ERR", "L''opció de línia d''ordres ''{0}'' no és vàlida. " }, { "CMDLINE_OPT_MISSING_ARG_ERR", "A l''opció de línia d''ordres ''{0}'' falta un argument obligatori. " }, { "WARNING_PLUS_WRAPPED_MSG", "AVÍS:  ''{0}''\n       :{1}" }, { "WARNING_MSG", "AVÍS:  ''{0}''" }, { "FATAL_ERR_PLUS_WRAPPED_MSG", "ERROR MOLT GREU:  ''{0}''\n           :{1}" }, { "FATAL_ERR_MSG", "ERROR MOLT GREU:  ''{0}''" }, { "ERROR_PLUS_WRAPPED_MSG", "ERROR:  ''{0}''\n     :{1}" }, { "ERROR_MSG", "ERROR:  ''{0}''" }, { "TRANSFORM_WITH_TRANSLET_STR", "Transformació mitjançant la classe translet ''{0}'' " }, { "TRANSFORM_WITH_JAR_STR", "Transformació mitjançant la classe translet ''{0}'' des del fitxer jar ''{1}''" }, { "COULD_NOT_CREATE_TRANS_FACT", "No s''ha pogut crear una instància de la classe TransformerFactory ''{0}''." }, { "TRANSLET_NAME_JAVA_CONFLICT", "El nom ''{0}'' no s''ha pogut fer servir com nom de la classe translet perquè conté caràcters que no estan permesos al nom de la classe Java. En lloc d''això, es va fer servir el nom ''{1}''. " }, { "COMPILER_ERROR_KEY", "Errors del compilador:" }, { "COMPILER_WARNING_KEY", "Avisos del compilador:" }, { "RUNTIME_ERROR_KEY", "Errors del translet:" }, { "INVALID_QNAME_ERR", "Un atribut que ha de tenir el valor QName o una llista separada per espais de QNames tenia el valor ''{0}''" }, { "INVALID_NCNAME_ERR", "Un atribut, que ha de tenir el valor NCName, tenia el valor ''{0}''" }, { "INVALID_METHOD_IN_OUTPUT", "L''atribut del mètode d''un element <xsl:sortida> tenia el valor ''{0}''. El valor ha de ser un de ''xml'', ''html'', ''text'', o ncname però no qname " }, { "JAXP_GET_FEATURE_NULL_NAME", "El nom de la característica no pot ser nul a TransformerFactory.getFeature(nom de cadena). " }, { "JAXP_SET_FEATURE_NULL_NAME", "El nom de la característica no pot ser nul a TransformerFactory.setFeature(nom de la cadena, valor booleà). " }, { "JAXP_UNSUPPORTED_FEATURE", "No es pot establir la característica ''{0}'' en aquesta TransformerFactory." } };
  }
}
