package org.junit.validator;

import java.util.List;
import org.junit.runners.model.TestClass;

public abstract interface TestClassValidator
{
  public abstract List<Exception> validateTestClass(TestClass paramTestClass);
}
