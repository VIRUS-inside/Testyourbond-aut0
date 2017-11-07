package org.seleniumhq.jetty9.http.pathmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
























public class UriTemplatePathSpec
  extends RegexPathSpec
{
  private static final Logger LOG = Log.getLogger(UriTemplatePathSpec.class);
  
  private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.*)\\}");
  


  private static final String VARIABLE_RESERVED = ":/?#[]@!$&'()*+,;=";
  

  private static final String VARIABLE_SYMBOLS = "-._";
  

  private static final Set<String> FORBIDDEN_SEGMENTS = new HashSet();
  static { FORBIDDEN_SEGMENTS.add("/./");
    FORBIDDEN_SEGMENTS.add("/../");
    FORBIDDEN_SEGMENTS.add("//");
  }
  



  public UriTemplatePathSpec(String rawSpec)
  {
    Objects.requireNonNull(rawSpec, "Path Param Spec cannot be null");
    
    if (("".equals(rawSpec)) || ("/".equals(rawSpec)))
    {
      pathSpec = "/";
      pattern = Pattern.compile("^/$");
      pathDepth = 1;
      specLength = 1;
      variables = new String[0];
      group = PathSpecGroup.EXACT; return;
    }
    
    StringBuilder err;
    if (rawSpec.charAt(0) != '/')
    {

      err = new StringBuilder();
      err.append("Syntax Error: path spec \"");
      err.append(rawSpec);
      err.append("\" must start with '/'");
      throw new IllegalArgumentException(err.toString());
    }
    
    for (String forbidden : FORBIDDEN_SEGMENTS)
    {
      if (rawSpec.contains(forbidden))
      {
        StringBuilder err = new StringBuilder();
        err.append("Syntax Error: segment ");
        err.append(forbidden);
        err.append(" is forbidden in path spec: ");
        err.append(rawSpec);
        throw new IllegalArgumentException(err.toString());
      }
    }
    
    pathSpec = rawSpec;
    
    StringBuilder regex = new StringBuilder();
    regex.append('^');
    
    List<String> varNames = new ArrayList();
    
    String[] segments = rawSpec.substring(1).split("/");
    char[] segmentSignature = new char[segments.length];
    pathDepth = segments.length;
    for (int i = 0; i < segments.length; i++)
    {
      String segment = segments[i];
      Matcher mat = VARIABLE_PATTERN.matcher(segment);
      StringBuilder err;
      if (mat.matches())
      {

        String variable = mat.group(1);
        if (varNames.contains(variable))
        {

          err = new StringBuilder();
          err.append("Syntax Error: variable ");
          err.append(variable);
          err.append(" is duplicated in path spec: ");
          err.append(rawSpec);
          throw new IllegalArgumentException(err.toString());
        }
        
        assertIsValidVariableLiteral(variable);
        
        segmentSignature[i] = 'v';
        
        varNames.add(variable);
        
        regex.append("/([^/]+)");
      } else {
        if (mat.find(0))
        {

          StringBuilder err = new StringBuilder();
          err.append("Syntax Error: variable ");
          err.append(mat.group());
          err.append(" must exist as entire path segment: ");
          err.append(rawSpec);
          throw new IllegalArgumentException(err.toString());
        }
        if ((segment.indexOf('{') >= 0) || (segment.indexOf('}') >= 0))
        {

          StringBuilder err = new StringBuilder();
          err.append("Syntax Error: invalid path segment /");
          err.append(segment);
          err.append("/ variable declaration incomplete: ");
          err.append(rawSpec);
          throw new IllegalArgumentException(err.toString());
        }
        if (segment.indexOf('*') >= 0)
        {

          err = new StringBuilder();
          err.append("Syntax Error: path segment /");
          err.append(segment);
          err.append("/ contains a wildcard symbol (not supported by this uri-template implementation): ");
          err.append(rawSpec);
          throw new IllegalArgumentException(err.toString());
        }
        


        segmentSignature[i] = 'e';
        
        regex.append('/');
        
        StringBuilder err = segment.toCharArray();err = err.length; for (StringBuilder localStringBuilder1 = 0; localStringBuilder1 < err; localStringBuilder1++) { char c = err[localStringBuilder1];
          
          if ((c == '.') || (c == '[') || (c == ']') || (c == '\\'))
          {
            regex.append('\\');
          }
          regex.append(c);
        }
      }
    }
    

    if (rawSpec.charAt(rawSpec.length() - 1) == '/')
    {
      regex.append('/');
    }
    
    regex.append('$');
    
    pattern = Pattern.compile(regex.toString());
    
    int varcount = varNames.size();
    variables = ((String[])varNames.toArray(new String[varcount]));
    

    String sig = String.valueOf(segmentSignature);
    
    if (Pattern.matches("^e*$", sig))
    {
      group = PathSpecGroup.EXACT;
    }
    else if (Pattern.matches("^e*v+", sig))
    {
      group = PathSpecGroup.PREFIX_GLOB;
    }
    else if (Pattern.matches("^v+e+", sig))
    {
      group = PathSpecGroup.SUFFIX_GLOB;
    }
    else
    {
      group = PathSpecGroup.MIDDLE_GLOB;
    }
  }
  


  private String[] variables;
  

  private void assertIsValidVariableLiteral(String variable)
  {
    int len = variable.length();
    
    int i = 0;
    
    boolean valid = len > 0;
    
    while ((valid) && (i < len))
    {
      int codepoint = variable.codePointAt(i);
      i += Character.charCount(codepoint);
      

      if ((!isValidBasicLiteralCodepoint(codepoint)) && 
      




        (!Character.isSupplementaryCodePoint(codepoint)))
      {




        if (codepoint == 37)
        {
          if (i + 2 > len)
          {

            valid = false;
          }
          else {
            codepoint = TypeUtil.convertHexDigit(variable.codePointAt(i++)) << 4;
            codepoint |= TypeUtil.convertHexDigit(variable.codePointAt(i++));
            

            if (isValidBasicLiteralCodepoint(codepoint)) {}
          }
          

        }
        else
          valid = false;
      }
    }
    if (!valid)
    {

      StringBuilder err = new StringBuilder();
      err.append("Syntax Error: variable {");
      err.append(variable);
      err.append("} an invalid variable name: ");
      err.append(pathSpec);
      throw new IllegalArgumentException(err.toString());
    }
  }
  

  private boolean isValidBasicLiteralCodepoint(int codepoint)
  {
    if (((codepoint >= 97) && (codepoint <= 122)) || ((codepoint >= 65) && (codepoint <= 90)) || ((codepoint >= 48) && (codepoint <= 57)))
    {


      return true;
    }
    

    if ("-._".indexOf(codepoint) >= 0)
    {
      return true;
    }
    

    if (":/?#[]@!$&'()*+,;=".indexOf(codepoint) >= 0)
    {
      LOG.warn("Detected URI Template reserved symbol [{}] in path spec \"{}\"", new Object[] { Character.valueOf((char)codepoint), pathSpec });
      return false;
    }
    
    return false;
  }
  
  public Map<String, String> getPathParams(String path)
  {
    Matcher matcher = getMatcher(path);
    if (matcher.matches())
    {
      if (group == PathSpecGroup.EXACT)
      {
        return Collections.emptyMap();
      }
      Map<String, String> ret = new HashMap();
      int groupCount = matcher.groupCount();
      for (int i = 1; i <= groupCount; i++)
      {
        ret.put(variables[(i - 1)], matcher.group(i));
      }
      return ret;
    }
    return null;
  }
  
  public int getVariableCount()
  {
    return variables.length;
  }
  
  public String[] getVariables()
  {
    return variables;
  }
}
