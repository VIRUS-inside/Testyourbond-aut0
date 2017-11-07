package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;






















































































public class ErrorMessages_sl
  extends ListResourceBundle
{
  public ErrorMessages_sl() {}
  
  public Object[][] getContents()
  {
    return new Object[][] { { "MULTIPLE_STYLESHEET_ERR", "V isti datoteki je določenih več slogovnih datotek." }, { "TEMPLATE_REDEF_ERR", "Predloga ''{0}'' je že določena v tej slogovni datoteki." }, { "TEMPLATE_UNDEF_ERR", "Predloga ''{0}'' ni določena v tej slogovni datoteki." }, { "VARIABLE_REDEF_ERR", "Spremenljivka ''{0}'' je večkrat določena znotraj istega obsega." }, { "VARIABLE_UNDEF_ERR", "Spremenljivka ali parameter ''{0}'' je nedoločen." }, { "CLASS_NOT_FOUND_ERR", "Ni mogoče najti razreda ''{0}''." }, { "METHOD_NOT_FOUND_ERR", "Zunanje metode ''{0}'' ni mogoče najti (mora biti javna)." }, { "ARGUMENT_CONVERSION_ERR", "Ni mogoče pretvoriti argumenta / tipa vrnitve v klicu metode ''{0}''" }, { "FILE_NOT_FOUND_ERR", "Datoteke ali URI-ja ''{0}'' ni mogoče najti." }, { "INVALID_URI_ERR", "Neveljaven URI ''{0}''." }, { "FILE_ACCESS_ERR", "Datoteke ali URI-ja ''{0}'' ni mogoče odpreti." }, { "MISSING_ROOT_ERR", "Pričakovan element <xsl:stylesheet> ali <xsl:transform>." }, { "NAMESPACE_UNDEF_ERR", "Predpona imenskega prostora ''{0}'' ni določena." }, { "FUNCTION_RESOLVE_ERR", "Klica funkcije ''{0}'' ni mogoče razrešiti." }, { "NEED_LITERAL_ERR", "Argument za ''{0}'' mora biti dobesedni niz." }, { "XPATH_PARSER_ERR", "Napaka pri razčlenjevanju izraza XPath ''{0}''." }, { "REQUIRED_ATTR_ERR", "Manjka zahtevani atribut ''{0}''." }, { "ILLEGAL_CHAR_ERR", "Neveljavni znak ''{0}'' v izrazu XPath." }, { "ILLEGAL_PI_ERR", "Neveljavno ime ''{0}'' za navodila za obdelavo." }, { "STRAY_ATTRIBUTE_ERR", "Atribut ''{0}'' zunaj elementa." }, { "ILLEGAL_ATTRIBUTE_ERR", "Neveljaven atribut ''{0}''." }, { "CIRCULAR_INCLUDE_ERR", "Krožni uvoz/vključitev. Slogovna datoteka ''{0}'' je že naložena." }, { "RESULT_TREE_SORT_ERR", "Ne morem razvrstiti fragmentov drevesa rezultatov (elementi <xsl:sort> so prezrti). Pri pripravi drevesa rezultatov morate razvrstiti vozlišča." }, { "SYMBOLS_REDEF_ERR", "Decimalno oblikovanje ''{0}'' je že določeno." }, { "XSL_VERSION_ERR", "XSL različice ''{0}'' XSLTC ne podpira." }, { "CIRCULAR_VARIABLE_ERR", "Sklic na krožno spremenljivko/parameter v ''{0}''." }, { "ILLEGAL_BINARY_OP_ERR", "Neznan operator za binarni izraz." }, { "ILLEGAL_ARG_ERR", "Neveljavni argument(i) za klic funkcije." }, { "DOCUMENT_ARG_ERR", "Drugi argument funkcije document() mora biti skupina vozlišč." }, { "MISSING_WHEN_ERR", "Zahtevan vsaj en element <xsl:when> v <xsl:choose>." }, { "MULTIPLE_OTHERWISE_ERR", "Dovoljen samo en element <xsl:otherwise> v <xsl:choose>." }, { "STRAY_OTHERWISE_ERR", "<xsl:otherwise> lahko uporabljamo samo znotraj <xsl:choose>." }, { "STRAY_WHEN_ERR", "<xsl:when> lahko uporabljamo samo znotraj <xsl:choose>." }, { "WHEN_ELEMENT_ERR", "V <xsl:choose> sta dovoljena samo elementa <xsl:when> in <xsl:otherwise>." }, { "UNNAMED_ATTRIBSET_ERR", "V <xsl:attribute-set> manjka atribut 'name'." }, { "ILLEGAL_CHILD_ERR", "Neveljavni podrejeni element." }, { "ILLEGAL_ELEM_NAME_ERR", "Elementa ''{0}'' ni mogoče poklicati" }, { "ILLEGAL_ATTR_NAME_ERR", "Atributa ''{0}'' ni mogoče poklicati" }, { "ILLEGAL_TEXT_NODE_ERR", "Tekstovni podatki zunaj elementa na najvišji ravni <xsl:stylesheet>." }, { "SAX_PARSER_CONFIG_ERR", "Razčlenjevalnik JAXP ni pravilno konfiguriran" }, { "INTERNAL_ERR", "Nepopravljiva XSLTC-notranja napaka: ''{0}''" }, { "UNSUPPORTED_XSL_ERR", "Nepodprt XSL element ''{0}''." }, { "UNSUPPORTED_EXT_ERR", "Neprepoznana pripona XSLTC ''{0}''." }, { "MISSING_XSLT_URI_ERR", "Vhodni dokument ni slogovna datoteka (v korenskem elementu ni naveden imenski prostor XSL)." }, { "MISSING_XSLT_TARGET_ERR", "Cilja slogovne datoteke ''{0}'' ni mogoče najti." }, { "NOT_IMPLEMENTED_ERR", "Ni izvedeno: ''{0}''." }, { "NOT_STYLESHEET_ERR", "Vhodni dokument ne vsebuje slogovne datoteke XSL." }, { "ELEMENT_PARSE_ERR", "Elementa ''{0}'' ni mogoče razčleniti" }, { "KEY_USE_ATTR_ERR", "Atribut uporabe za <key> mora biti vozlišče, skupina vozlišč, niz ali številka." }, { "OUTPUT_VERSION_ERR", "Različica izhodnega dokumenta XML mora biti 1.0" }, { "ILLEGAL_RELAT_OP_ERR", "Neznan operator za relacijski izraz" }, { "ATTRIBSET_UNDEF_ERR", "Poskus uporabe neobstoječe skupine atributov ''{0}''." }, { "ATTR_VAL_TEMPLATE_ERR", "Predloge vrednosti atributa ''{0}'' ni mogoče razčleniti." }, { "UNKNOWN_SIG_TYPE_ERR", "V podpisu za razred ''{0}'' je neznan podatkovni tip." }, { "DATA_CONVERSION_ERR", "Ni mogoče pretvoriti podatkovnega tipa ''{0}'' v ''{1}''." }, { "NO_TRANSLET_CLASS_ERR", "Te Templates ne vsebujejo veljavne definicije razreda translet." }, { "NO_MAIN_TRANSLET_ERR", "Te predloge ne vsebujejo razreda z imenom ''{0}''." }, { "TRANSLET_CLASS_ERR", "Ni mogoče naložiti razreda transleta ''{0}''." }, { "TRANSLET_OBJECT_ERR", "Razred transleta je naložen, vendar priprava primerka transleta ni mogoča." }, { "ERROR_LISTENER_NULL_ERR", "Poskus nastavitve ErrorListener za ''{0}'' na null" }, { "JAXP_UNKNOWN_SOURCE_ERR", "XSLTC podpira samo StreamSource, SAXSource in DOMSource" }, { "JAXP_NO_SOURCE_ERR", "Predmet Source, ki je bil podan z ''{0}'', nima vsebine." }, { "JAXP_COMPILE_ERR", "Ni mogoče prevesti slogovne datoteke" }, { "JAXP_INVALID_ATTR_ERR", "TransformerFactory ne prepozna atributa ''{0}''." }, { "JAXP_SET_RESULT_ERR", "Klic za setResult() mora biti izveden pred klicem startDocument()." }, { "JAXP_NO_TRANSLET_ERR", "Transformer ne vsebuje enkapsuliranih translet objektov." }, { "JAXP_NO_HANDLER_ERR", "Za rezultat transformacije ni izhodne obravnave." }, { "JAXP_NO_RESULT_ERR", "Rezultat, ki je bil posredovan ''{0}'', je neveljaven." }, { "JAXP_UNKNOWN_PROP_ERR", "Poskus dostopa do neveljavne lastnosti (property) Transformer ''{0}''." }, { "SAX2DOM_ADAPTER_ERR", "Ni mogoče ustvariti adapterja SAX2DOM: ''{0}''." }, { "XSLTC_SOURCE_ERR", "Klic XSLTCSource.build() brez predhodne nastavitve systemId." }, { "ER_RESULT_NULL", "Rezultat naj ne bi bil NULL" }, { "JAXP_INVALID_SET_PARAM_VALUE", "Vrednost parametra {0} mora biti veljaven javanski objekt" }, { "COMPILE_STDIN_ERR", "Možnost -i mora biti uporabljena skupaj z možnostjo -o." }, { "COMPILE_USAGE_STR", "SYNOPSIS\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <izhodna datoteka>]\n      [-d <directory>] [-j  <datoteka jar>] [-p <paket>]\n      [-n] [-x] [-u] [-v] [-h] { <stylesheet> | -i }\n\nOPTIONS\n   -o <izhodna datoteka>    dodeli ime <izhodna datoteka> generiranemu\n                  transletu.  Ime transleta se po privzetih nastavitvah\n                 izpelje iz imena <stylesheet>.  Pri prevajanju\n                  več slogovnih datotek je ta možnost prezrta.\n   -d <directory> določi ciljno mapo za translet\n   -j <datoteka jar>   združi razrede translet v datoteko jar\n       pod imenom, določenim z <datoteka jar>\n   -p <paket>   določi predpono imena paketa vsem generiranim\n               razredom translet.\n   -n             omogoča vrivanje predlog (v povprečju boljše privzeto\n                      obnašanje).\n   -x             vklopi dodatna izhodna sporočila za iskanje napak\n   -u             prevede argumente <stylesheet> kot URL-je\n   -i             prisili prevajalnik k branju slogovne datoteke iz stdin\n   -v             natisne različico prevajalnika\n   -h             natisne ta stavek za uporabo\n" }, { "TRANSFORM_USAGE_STR", "SYNOPSIS \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <datoteka jar>]\n      [-x] [-n <ponovitve>] {-u <document_url> | <dokument>}\n      <razred> [<param1>=<value1> ...]\n\n   uporablja translet <razred> za pretvorbo dokumenta XML \n   navedenega kot <dokument>. Translet <razred> je ali v\n   uporabnikovem CLASSPATH ali v izbirno navedeni datoteki <datoteka jar>.\nOPTIONS\n   -j <datoteka jar>    določi datoteko jar, iz katere bo naložen translet\n   -x               vklopi dodatna sporočila za iskanje napak\n   n  <ponovitve> <ponovitve>-krat požene preoblikovanje in\n                   prikaže informacije profiliranja\n   -u <document_url> določi vhodni dokument XML za URL\n" }, { "STRAY_SORT_ERR", "<xsl:sort> je mogoče uporabljati samo znotraj <xsl:for-each> ali <xsl:apply-templates>." }, { "UNSUPPORTED_ENCODING", "Ta JVM ne podpira izhodnega kodiranja ''{0}''." }, { "SYNTAX_ERR", "Napaka v sintaksi v ''{0}''." }, { "CONSTRUCTOR_NOT_FOUND", "Ni mogoče najti zunanjega konstruktorja ''{0}''." }, { "NO_JAVA_FUNCT_THIS_REF", "Prvi argument nestatične (non-static) javanske funkcije ''{0}'' ni veljaven sklic na objekt." }, { "TYPE_CHECK_ERR", "Napaka pri preverjanju tipa izraza ''{0}''." }, { "TYPE_CHECK_UNK_LOC_ERR", "Napaka pri preverjanju tipa izraza na neznani lokaciji." }, { "ILLEGAL_CMDLINE_OPTION_ERR", "Možnost ukazne vrstice ''{0}'' ni veljavna." }, { "CMDLINE_OPT_MISSING_ARG_ERR", "Možnosti ukazne vrstice ''{0}'' manjka zahtevani argument." }, { "WARNING_PLUS_WRAPPED_MSG", "OPOZORILO:  ''{0}''\n       :{1}" }, { "WARNING_MSG", "OPOZORILO:  ''{0}''" }, { "FATAL_ERR_PLUS_WRAPPED_MSG", "USODNA NAPAKA:  ''{0}''\n           :{1}" }, { "FATAL_ERR_MSG", "USODNA NAPAKA:  ''{0}''" }, { "ERROR_PLUS_WRAPPED_MSG", "USODNA NAPAKA:  ''{0}''\n     :{1}" }, { "ERROR_MSG", "NAPAKA:  ''{0}''" }, { "TRANSFORM_WITH_TRANSLET_STR", "Pretvorba z uporabo transleta ''{0}'' " }, { "TRANSFORM_WITH_JAR_STR", "Pretvorba z uporabo transleta ''{0}'' iz datoteke jar ''{1}''" }, { "COULD_NOT_CREATE_TRANS_FACT", "Ni mogoče ustvariti primerka razreda TransformerFactory ''{0}''." }, { "TRANSLET_NAME_JAVA_CONFLICT", "Imena ''{0}'' ni bilo mogoče uporabiti kot ime razreda translet, saj vsebuje znake, ki v imenu javanskega razreda niso dovoljeni.  Uporabljeno je bilo ime ''{1}''." }, { "COMPILER_ERROR_KEY", "Napake prevajalnika:" }, { "COMPILER_WARNING_KEY", "Opozorila prevajalnika:" }, { "RUNTIME_ERROR_KEY", "Napake transleta:" }, { "INVALID_QNAME_ERR", "Atribut, katerega vrednost mora biti vrednost QName ali s presledki ločen seznam vrednosti Qname, je imel vrednost ''{0}''" }, { "INVALID_NCNAME_ERR", "Atribut, katerega vrednost mora biti NCName, je imel vrednost ''{0}''" }, { "INVALID_METHOD_IN_OUTPUT", "Atribut metode elementa <xsl:output> je imel vrednost ''{0}''.  Vrednost mora biti ena izmed ''xml'', ''html'', ''text'', ali qname-but-not-ncname (qname, vendar pa ne ncname)" }, { "JAXP_GET_FEATURE_NULL_NAME", "Ime funkcije ne sme biti null v TransformerFactory.getFeature(Ime niza)." }, { "JAXP_SET_FEATURE_NULL_NAME", "Ime funkcije ne sme biti null v TransformerFactory.getFeature(Ime niza, boolova vrednost)." }, { "JAXP_UNSUPPORTED_FEATURE", "Ni mogoče nastaviti funkcije ''{0}'' v tem TransformerFactory." } };
  }
}
