package org.junit.runners.parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.runners.model.TestClass;











public class TestWithParameters
{
  private final String name;
  private final TestClass testClass;
  private final List<Object> parameters;
  
  public TestWithParameters(String name, TestClass testClass, List<Object> parameters)
  {
    notNull(name, "The name is missing.");
    notNull(testClass, "The test class is missing.");
    notNull(parameters, "The parameters are missing.");
    this.name = name;
    this.testClass = testClass;
    this.parameters = Collections.unmodifiableList(new ArrayList(parameters));
  }
  
  public String getName() {
    return name;
  }
  
  public TestClass getTestClass() {
    return testClass;
  }
  
  public List<Object> getParameters() {
    return parameters;
  }
  
  public int hashCode()
  {
    int prime = 14747;
    int result = prime + name.hashCode();
    result = prime * result + testClass.hashCode();
    return prime * result + parameters.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TestWithParameters other = (TestWithParameters)obj;
    return (name.equals(name)) && (parameters.equals(parameters)) && (testClass.equals(testClass));
  }
  


  public String toString()
  {
    return testClass.getName() + " '" + name + "' with parameters " + parameters;
  }
  
  private static void notNull(Object value, String message)
  {
    if (value == null) {
      throw new NullPointerException(message);
    }
  }
}
