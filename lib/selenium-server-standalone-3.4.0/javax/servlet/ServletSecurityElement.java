package javax.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.ServletSecurity;














































public class ServletSecurityElement
  extends HttpConstraintElement
{
  private Collection<String> methodNames;
  private Collection<HttpMethodConstraintElement> methodConstraints;
  
  public ServletSecurityElement()
  {
    methodConstraints = new HashSet();
    methodNames = Collections.emptySet();
  }
  







  public ServletSecurityElement(HttpConstraintElement constraint)
  {
    super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
    

    methodConstraints = new HashSet();
    methodNames = Collections.emptySet();
  }
  












  public ServletSecurityElement(Collection<HttpMethodConstraintElement> methodConstraints)
  {
    this.methodConstraints = (methodConstraints == null ? new HashSet() : methodConstraints);
    
    methodNames = checkMethodNames(this.methodConstraints);
  }
  













  public ServletSecurityElement(HttpConstraintElement constraint, Collection<HttpMethodConstraintElement> methodConstraints)
  {
    super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
    

    this.methodConstraints = (methodConstraints == null ? new HashSet() : methodConstraints);
    
    methodNames = checkMethodNames(this.methodConstraints);
  }
  







  public ServletSecurityElement(ServletSecurity annotation)
  {
    super(annotation.value().value(), annotation.value().transportGuarantee(), annotation.value().rolesAllowed());
    

    methodConstraints = new HashSet();
    
    for (HttpMethodConstraint constraint : annotation.httpMethodConstraints()) {
      methodConstraints.add(new HttpMethodConstraintElement(constraint.value(), new HttpConstraintElement(constraint.emptyRoleSemantic(), constraint.transportGuarantee(), constraint.rolesAllowed())));
    }
    




    methodNames = checkMethodNames(methodConstraints);
  }
  










  public Collection<HttpMethodConstraintElement> getHttpMethodConstraints()
  {
    return Collections.unmodifiableCollection(methodConstraints);
  }
  









  public Collection<String> getMethodNames()
  {
    return Collections.unmodifiableCollection(methodNames);
  }
  










  private Collection<String> checkMethodNames(Collection<HttpMethodConstraintElement> methodConstraints)
  {
    Collection<String> methodNames = new HashSet();
    
    for (HttpMethodConstraintElement methodConstraint : methodConstraints) {
      String methodName = methodConstraint.getMethodName();
      if (!methodNames.add(methodName)) {
        throw new IllegalArgumentException("Duplicate HTTP method name: " + methodName);
      }
    }
    
    return methodNames;
  }
}
