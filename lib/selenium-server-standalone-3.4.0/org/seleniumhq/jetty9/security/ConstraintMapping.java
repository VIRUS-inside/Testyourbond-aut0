package org.seleniumhq.jetty9.security;

import org.seleniumhq.jetty9.util.security.Constraint;























public class ConstraintMapping
{
  String _method;
  String[] _methodOmissions;
  String _pathSpec;
  Constraint _constraint;
  
  public ConstraintMapping() {}
  
  public Constraint getConstraint()
  {
    return _constraint;
  }
  




  public void setConstraint(Constraint constraint)
  {
    _constraint = constraint;
  }
  




  public String getMethod()
  {
    return _method;
  }
  




  public void setMethod(String method)
  {
    _method = method;
  }
  




  public String getPathSpec()
  {
    return _pathSpec;
  }
  




  public void setPathSpec(String pathSpec)
  {
    _pathSpec = pathSpec;
  }
  




  public void setMethodOmissions(String[] omissions)
  {
    _methodOmissions = omissions;
  }
  

  public String[] getMethodOmissions()
  {
    return _methodOmissions;
  }
}
