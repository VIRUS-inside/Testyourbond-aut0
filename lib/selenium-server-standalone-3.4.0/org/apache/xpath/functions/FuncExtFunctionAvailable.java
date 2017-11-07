package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.Expression;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;























public class FuncExtFunctionAvailable
  extends FunctionOneArg
{
  static final long serialVersionUID = 5118814314918592241L;
  private transient FunctionTable m_functionTable = null;
  





  public FuncExtFunctionAvailable() {}
  





  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    String fullName = m_arg0.execute(xctxt).str();
    int indexOfNSSep = fullName.indexOf(':');
    String methName;
    String namespace; String methName; if (indexOfNSSep < 0)
    {
      String prefix = "";
      String namespace = "http://www.w3.org/1999/XSL/Transform";
      methName = fullName;
    }
    else
    {
      String prefix = fullName.substring(0, indexOfNSSep);
      namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
      if (null == namespace)
        return XBoolean.S_FALSE;
      methName = fullName.substring(indexOfNSSep + 1);
    }
    
    if (namespace.equals("http://www.w3.org/1999/XSL/Transform"))
    {
      try
      {
        if (null == m_functionTable) m_functionTable = new FunctionTable();
        return m_functionTable.functionAvailable(methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
      }
      catch (Exception e)
      {
        return XBoolean.S_FALSE;
      }
    }
    


    ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
    return extProvider.functionAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
  








  public void setFunctionTable(FunctionTable aTable)
  {
    m_functionTable = aTable;
  }
}
