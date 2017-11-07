package org.apache.xalan.xsltc.runtime;

import java.util.ListResourceBundle;













































































public class ErrorMessages_pl
  extends ListResourceBundle
{
  public ErrorMessages_pl() {}
  
  public Object[][] getContents()
  {
    return new Object[][] { { "RUN_TIME_INTERNAL_ERR", "Błąd wewnętrzny czasu wykonywania w klasie ''{0}''" }, { "RUN_TIME_COPY_ERR", "Błąd czasu wykonywania podczas wykonywania <xsl:copy>." }, { "DATA_CONVERSION_ERR", "Niepoprawna konwersja z ''{0}'' na ''{1}''." }, { "EXTERNAL_FUNC_ERR", "Funkcja zewnętrzna ''{0}'' jest nieobsługiwana przez XSLTC." }, { "EQUALITY_EXPR_ERR", "Nieznany typ argumentu w wyrażeniu równości." }, { "INVALID_ARGUMENT_ERR", "Niepoprawny typ argumentu ''{0}'' w wywołaniu funkcji ''{1}''" }, { "FORMAT_NUMBER_ERR", "Próba sformatowania liczby ''{0}'' za pomocą wzorca ''{1}''." }, { "ITERATOR_CLONE_ERR", "Nie można utworzyć kopii iteratora ''{0}''." }, { "AXIS_SUPPORT_ERR", "Iterator dla osi ''{0}'' jest nieobsługiwany." }, { "TYPED_AXIS_SUPPORT_ERR", "Iterator dla osi ''{0}'' określonego typu jest nieobsługiwany." }, { "STRAY_ATTRIBUTE_ERR", "Atrybut ''{0}'' znajduje się poza elementem." }, { "STRAY_NAMESPACE_ERR", "Deklaracja przestrzeni nazw ''{0}''=''{1}'' znajduje się poza elementem." }, { "NAMESPACE_PREFIX_ERR", "Nie zadeklarowano przestrzeni nazw dla przedrostka ''{0}''." }, { "DOM_ADAPTER_INIT_ERR", "Utworzono DOMAdapter za pomocą źródłowego DOM o błędnym typie." }, { "PARSER_DTD_SUPPORT_ERR", "Używany analizator składni SAX nie obsługuje zdarzeń deklaracji DTD." }, { "NAMESPACES_SUPPORT_ERR", "Używany analizator składni SAX nie obsługuje przestrzeni nazw XML." }, { "CANT_RESOLVE_RELATIVE_URI_ERR", "Nie można rozstrzygnąć odwołania do identyfikatora URI ''{0}''." }, { "UNSUPPORTED_XSL_ERR", "Nieobsługiwany element XSL ''{0}''" }, { "UNSUPPORTED_EXT_ERR", "Nierozpoznane rozszerzenie XSLTC ''{0}''." }, { "UNKNOWN_TRANSLET_VERSION_ERR", "Podany translet ''{0}'' został utworzony za pomocą wersji XSLTC, która jest nowsza od używanego obecnie modułu wykonawczego XSLTC.  Trzeba zrekompilować arkusz stylów lub uruchomić ten translet za pomocą nowszej wersji XSLTC." }, { "INVALID_QNAME_ERR", "Atrybut, którego wartością musi być nazwa QName, miał wartość ''{0}''" }, { "INVALID_NCNAME_ERR", "Atrybut, którego wartością musi być nazwa NCName, miał wartość ''{0}''" }, { "UNALLOWED_EXTENSION_FUNCTION_ERR", "Użycie funkcji rozszerzenia ''{0}'' jest niedozwolone, gdy opcja przetwarzania bezpiecznego jest ustawiona na wartość true." }, { "UNALLOWED_EXTENSION_ELEMENT_ERR", "Użycie elementu rozszerzenia ''{0}'' jest niedozwolone, gdy opcja przetwarzania bezpiecznego jest ustawiona na wartość true." } };
  }
}
