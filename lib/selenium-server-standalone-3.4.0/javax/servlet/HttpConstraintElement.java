package javax.servlet;

import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;


















































public class HttpConstraintElement
{
  private ServletSecurity.EmptyRoleSemantic emptyRoleSemantic;
  private ServletSecurity.TransportGuarantee transportGuarantee;
  private String[] rolesAllowed;
  
  public HttpConstraintElement()
  {
    this(ServletSecurity.EmptyRoleSemantic.PERMIT);
  }
  




  public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic)
  {
    this(semantic, ServletSecurity.TransportGuarantee.NONE, new String[0]);
  }
  









  public HttpConstraintElement(ServletSecurity.TransportGuarantee guarantee, String... roleNames)
  {
    this(ServletSecurity.EmptyRoleSemantic.PERMIT, guarantee, roleNames);
  }
  











  public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic semantic, ServletSecurity.TransportGuarantee guarantee, String... roleNames)
  {
    if ((semantic == ServletSecurity.EmptyRoleSemantic.DENY) && (roleNames.length > 0)) {
      throw new IllegalArgumentException("Deny semantic with rolesAllowed");
    }
    
    emptyRoleSemantic = semantic;
    transportGuarantee = guarantee;
    rolesAllowed = copyStrings(roleNames);
  }
  










  public ServletSecurity.EmptyRoleSemantic getEmptyRoleSemantic()
  {
    return emptyRoleSemantic;
  }
  






  public ServletSecurity.TransportGuarantee getTransportGuarantee()
  {
    return transportGuarantee;
  }
  


















  public String[] getRolesAllowed()
  {
    return copyStrings(rolesAllowed);
  }
  
  private String[] copyStrings(String[] strings) {
    String[] arr = null;
    if (strings != null) {
      int len = strings.length;
      arr = new String[len];
      if (len > 0) {
        System.arraycopy(strings, 0, arr, 0, len);
      }
    }
    
    return arr != null ? arr : new String[0];
  }
}
