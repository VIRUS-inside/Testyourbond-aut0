package org.junit.internal.requests;

import java.util.Comparator;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.manipulation.Sorter;

public class SortingRequest extends Request
{
  private final Request request;
  private final Comparator<Description> comparator;
  
  public SortingRequest(Request request, Comparator<Description> comparator)
  {
    this.request = request;
    this.comparator = comparator;
  }
  
  public org.junit.runner.Runner getRunner()
  {
    org.junit.runner.Runner runner = request.getRunner();
    new Sorter(comparator).apply(runner);
    return runner;
  }
}
