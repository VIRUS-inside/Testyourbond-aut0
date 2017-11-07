package org.apache.xpath.jaxp;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.VariableStack;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;





























public class JAXPVariableStack
  extends VariableStack
{
  private final XPathVariableResolver resolver;
  
  public JAXPVariableStack(XPathVariableResolver resolver)
  {
    super(2);
    this.resolver = resolver;
  }
  
  public XObject getVariableOrParam(XPathContext xctxt, org.apache.xml.utils.QName qname) throws TransformerException, IllegalArgumentException
  {
    if (qname == null)
    {

      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "Variable qname" });
      

      throw new IllegalArgumentException(fmsg);
    }
    javax.xml.namespace.QName name = new javax.xml.namespace.QName(qname.getNamespace(), qname.getLocalPart());
    


    Object varValue = resolver.resolveVariable(name);
    if (varValue == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_RESOLVE_VARIABLE_RETURNS_NULL", new Object[] { name.toString() });
      

      throw new TransformerException(fmsg);
    }
    return XObject.create(varValue, xctxt);
  }
}
