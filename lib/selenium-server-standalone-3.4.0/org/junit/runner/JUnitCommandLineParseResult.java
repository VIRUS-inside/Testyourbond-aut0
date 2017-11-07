package org.junit.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.InitializationError;


class JUnitCommandLineParseResult
{
  private final List<String> filterSpecs = new ArrayList();
  private final List<Class<?>> classes = new ArrayList();
  private final List<Throwable> parserErrors = new ArrayList();
  



  JUnitCommandLineParseResult() {}
  


  public List<String> getFilterSpecs()
  {
    return Collections.unmodifiableList(filterSpecs);
  }
  


  public List<Class<?>> getClasses()
  {
    return Collections.unmodifiableList(classes);
  }
  




  public static JUnitCommandLineParseResult parse(String[] args)
  {
    JUnitCommandLineParseResult result = new JUnitCommandLineParseResult();
    
    result.parseArgs(args);
    
    return result;
  }
  
  private void parseArgs(String[] args) {
    parseParameters(parseOptions(args));
  }
  
  String[] parseOptions(String... args) {
    for (int i = 0; i != args.length; i++) {
      String arg = args[i];
      
      if (arg.equals("--"))
        return copyArray(args, i + 1, args.length);
      if (arg.startsWith("--")) {
        if ((arg.startsWith("--filter=")) || (arg.equals("--filter"))) {
          String filterSpec;
          if (arg.equals("--filter")) {
            i++;
            String filterSpec;
            if (i < args.length) {
              filterSpec = args[i];
            } else {
              parserErrors.add(new CommandLineParserError(arg + " value not specified"));
              break;
            }
          } else {
            filterSpec = arg.substring(arg.indexOf('=') + 1);
          }
          
          filterSpecs.add(filterSpec);
        } else {
          parserErrors.add(new CommandLineParserError("JUnit knows nothing about the " + arg + " option"));
        }
      } else {
        return copyArray(args, i, args.length);
      }
    }
    
    return new String[0];
  }
  
  private String[] copyArray(String[] args, int from, int to) {
    ArrayList<String> result = new ArrayList();
    
    for (int j = from; j != to; j++) {
      result.add(args[j]);
    }
    
    return (String[])result.toArray(new String[result.size()]);
  }
  
  void parseParameters(String[] args) {
    for (String arg : args) {
      try {
        classes.add(Classes.getClass(arg));
      } catch (ClassNotFoundException e) {
        parserErrors.add(new IllegalArgumentException("Could not find class [" + arg + "]", e));
      }
    }
  }
  
  private Request errorReport(Throwable cause) {
    return Request.errorReport(JUnitCommandLineParseResult.class, cause);
  }
  




  public Request createRequest(Computer computer)
  {
    if (parserErrors.isEmpty()) {
      Request request = Request.classes(computer, (Class[])classes.toArray(new Class[classes.size()]));
      
      return applyFilterSpecs(request);
    }
    return errorReport(new InitializationError(parserErrors));
  }
  
  private Request applyFilterSpecs(Request request)
  {
    try {
      for (String filterSpec : filterSpecs) {
        Filter filter = FilterFactories.createFilterFromFilterSpec(request, filterSpec);
        
        request = request.filterWith(filter);
      }
      return request;
    } catch (FilterFactory.FilterNotCreatedException e) {
      return errorReport(e);
    }
  }
  
  public static class CommandLineParserError
    extends Exception
  {
    private static final long serialVersionUID = 1L;
    
    public CommandLineParserError(String message)
    {
      super();
    }
  }
}
