package org.apache.commons.codec.language;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
















































public class DaitchMokotoffSoundex
  implements StringEncoder
{
  private static final String COMMENT = "//";
  private static final String DOUBLE_QUOTE = "\"";
  private static final String MULTILINE_COMMENT_END = "*/";
  private static final String MULTILINE_COMMENT_START = "/*";
  private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
  private static final int MAX_LENGTH = 6;
  
  private static final class Branch
  {
    private final StringBuilder builder;
    private String cachedString;
    private String lastReplacement;
    
    private Branch()
    {
      builder = new StringBuilder();
      lastReplacement = null;
      cachedString = null;
    }
    




    public Branch createBranch()
    {
      Branch branch = new Branch();
      builder.append(toString());
      lastReplacement = lastReplacement;
      return branch;
    }
    
    public boolean equals(Object other)
    {
      if (this == other) {
        return true;
      }
      if (!(other instanceof Branch)) {
        return false;
      }
      
      return toString().equals(((Branch)other).toString());
    }
    


    public void finish()
    {
      while (builder.length() < 6) {
        builder.append('0');
        cachedString = null;
      }
    }
    
    public int hashCode()
    {
      return toString().hashCode();
    }
    







    public void processNextReplacement(String replacement, boolean forceAppend)
    {
      boolean append = (lastReplacement == null) || (!lastReplacement.endsWith(replacement)) || (forceAppend);
      
      if ((append) && (builder.length() < 6)) {
        builder.append(replacement);
        
        if (builder.length() > 6) {
          builder.delete(6, builder.length());
        }
        cachedString = null;
      }
      
      lastReplacement = replacement;
    }
    
    public String toString()
    {
      if (cachedString == null) {
        cachedString = builder.toString();
      }
      return cachedString;
    }
  }
  

  private static final class Rule
  {
    private final String pattern;
    
    private final String[] replacementAtStart;
    private final String[] replacementBeforeVowel;
    private final String[] replacementDefault;
    
    protected Rule(String pattern, String replacementAtStart, String replacementBeforeVowel, String replacementDefault)
    {
      this.pattern = pattern;
      this.replacementAtStart = replacementAtStart.split("\\|");
      this.replacementBeforeVowel = replacementBeforeVowel.split("\\|");
      this.replacementDefault = replacementDefault.split("\\|");
    }
    
    public int getPatternLength() {
      return pattern.length();
    }
    
    public String[] getReplacements(String context, boolean atStart) {
      if (atStart) {
        return replacementAtStart;
      }
      
      int nextIndex = getPatternLength();
      boolean nextCharIsVowel = nextIndex < context.length() ? isVowel(context.charAt(nextIndex)) : false;
      if (nextCharIsVowel) {
        return replacementBeforeVowel;
      }
      
      return replacementDefault;
    }
    
    private boolean isVowel(char ch) {
      return (ch == 'a') || (ch == 'e') || (ch == 'i') || (ch == 'o') || (ch == 'u');
    }
    
    public boolean matches(String context) {
      return context.startsWith(pattern);
    }
    
    public String toString()
    {
      return String.format("%s=(%s,%s,%s)", new Object[] { pattern, Arrays.asList(replacementAtStart), Arrays.asList(replacementBeforeVowel), Arrays.asList(replacementDefault) });
    }
  }
  















  private static final Map<Character, List<Rule>> RULES = new HashMap();
  

  private static final Map<Character, Character> FOLDINGS = new HashMap();
  private final boolean folding;
  
  static { InputStream rulesIS = DaitchMokotoffSoundex.class.getClassLoader().getResourceAsStream("org/apache/commons/codec/language/dmrules.txt");
    if (rulesIS == null) {
      throw new IllegalArgumentException("Unable to load resource: org/apache/commons/codec/language/dmrules.txt");
    }
    
    Scanner scanner = new Scanner(rulesIS, "UTF-8");
    parseRules(scanner, "org/apache/commons/codec/language/dmrules.txt", RULES, FOLDINGS);
    scanner.close();
    

    for (Map.Entry<Character, List<Rule>> rule : RULES.entrySet()) {
      List<Rule> ruleList = (List)rule.getValue();
      Collections.sort(ruleList, new Comparator()
      {
        public int compare(DaitchMokotoffSoundex.Rule rule1, DaitchMokotoffSoundex.Rule rule2) {
          return rule2.getPatternLength() - rule1.getPatternLength();
        }
      });
    }
  }
  
  private static void parseRules(Scanner scanner, String location, Map<Character, List<Rule>> ruleMapping, Map<Character, Character> asciiFoldings)
  {
    int currentLine = 0;
    boolean inMultilineComment = false;
    
    while (scanner.hasNextLine()) {
      currentLine++;
      String rawLine = scanner.nextLine();
      String line = rawLine;
      
      if (inMultilineComment) {
        if (line.endsWith("*/")) {
          inMultilineComment = false;
        }
        

      }
      else if (line.startsWith("/*")) {
        inMultilineComment = true;
      }
      else {
        int cmtI = line.indexOf("//");
        if (cmtI >= 0) {
          line = line.substring(0, cmtI);
        }
        

        line = line.trim();
        
        if (line.length() != 0)
        {


          if (line.contains("="))
          {
            String[] parts = line.split("=");
            if (parts.length != 2) {
              throw new IllegalArgumentException("Malformed folding statement split into " + parts.length + " parts: " + rawLine + " in " + location);
            }
            
            String leftCharacter = parts[0];
            String rightCharacter = parts[1];
            
            if ((leftCharacter.length() != 1) || (rightCharacter.length() != 1)) {
              throw new IllegalArgumentException("Malformed folding statement - patterns are not single characters: " + rawLine + " in " + location);
            }
            

            asciiFoldings.put(Character.valueOf(leftCharacter.charAt(0)), Character.valueOf(rightCharacter.charAt(0)));
          }
          else
          {
            String[] parts = line.split("\\s+");
            if (parts.length != 4) {
              throw new IllegalArgumentException("Malformed rule statement split into " + parts.length + " parts: " + rawLine + " in " + location);
            }
            try
            {
              String pattern = stripQuotes(parts[0]);
              String replacement1 = stripQuotes(parts[1]);
              String replacement2 = stripQuotes(parts[2]);
              String replacement3 = stripQuotes(parts[3]);
              
              Rule r = new Rule(pattern, replacement1, replacement2, replacement3);
              char patternKey = pattern.charAt(0);
              List<Rule> rules = (List)ruleMapping.get(Character.valueOf(patternKey));
              if (rules == null) {
                rules = new ArrayList();
                ruleMapping.put(Character.valueOf(patternKey), rules);
              }
              rules.add(r);
            } catch (IllegalArgumentException e) {
              throw new IllegalStateException("Problem parsing line '" + currentLine + "' in " + location, e);
            }
          }
        }
      }
    }
  }
  
  private static String stripQuotes(String str)
  {
    if (str.startsWith("\"")) {
      str = str.substring(1);
    }
    
    if (str.endsWith("\"")) {
      str = str.substring(0, str.length() - 1);
    }
    
    return str;
  }
  





  public DaitchMokotoffSoundex()
  {
    this(true);
  }
  









  public DaitchMokotoffSoundex(boolean folding)
  {
    this.folding = folding;
  }
  









  private String cleanup(String input)
  {
    StringBuilder sb = new StringBuilder();
    for (char ch : input.toCharArray())
      if (!Character.isWhitespace(ch))
      {


        ch = Character.toLowerCase(ch);
        if ((folding) && (FOLDINGS.containsKey(Character.valueOf(ch)))) {
          ch = ((Character)FOLDINGS.get(Character.valueOf(ch))).charValue();
        }
        sb.append(ch);
      }
    return sb.toString();
  }
  

















  public Object encode(Object obj)
    throws EncoderException
  {
    if (!(obj instanceof String)) {
      throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
    }
    
    return encode((String)obj);
  }
  











  public String encode(String source)
  {
    if (source == null) {
      return null;
    }
    return soundex(source, false)[0];
  }
  






















  public String soundex(String source)
  {
    String[] branches = soundex(source, true);
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (String branch : branches) {
      sb.append(branch);
      index++; if (index < branches.length) {
        sb.append('|');
      }
    }
    return sb.toString();
  }
  









  private String[] soundex(String source, boolean branching)
  {
    if (source == null) {
      return null;
    }
    
    String input = cleanup(source);
    
    Set<Branch> currentBranches = new LinkedHashSet();
    currentBranches.add(new Branch(null));
    
    char lastChar = '\000';
    for (int index = 0; index < input.length(); index++) {
      char ch = input.charAt(index);
      

      if (!Character.isWhitespace(ch))
      {


        String inputContext = input.substring(index);
        List<Rule> rules = (List)RULES.get(Character.valueOf(ch));
        if (rules != null)
        {




          List<Branch> nextBranches = branching ? new ArrayList() : Collections.EMPTY_LIST;
          
          for (Rule rule : rules) {
            if (rule.matches(inputContext)) {
              if (branching) {
                nextBranches.clear();
              }
              String[] replacements = rule.getReplacements(inputContext, lastChar == 0);
              boolean branchingRequired = (replacements.length > 1) && (branching);
              
              for (Branch branch : currentBranches) {
                for (String nextReplacement : replacements)
                {
                  Branch nextBranch = branchingRequired ? branch.createBranch() : branch;
                  

                  boolean force = ((lastChar == 'm') && (ch == 'n')) || ((lastChar == 'n') && (ch == 'm'));
                  
                  nextBranch.processNextReplacement(nextReplacement, force);
                  
                  if (!branching) break;
                  nextBranches.add(nextBranch);
                }
              }
              



              if (branching) {
                currentBranches.clear();
                currentBranches.addAll(nextBranches);
              }
              index += rule.getPatternLength() - 1;
              break;
            }
          }
          
          lastChar = ch;
        }
      } }
    String[] result = new String[currentBranches.size()];
    int index = 0;
    for (Branch branch : currentBranches) {
      branch.finish();
      result[(index++)] = branch.toString();
    }
    
    return result;
  }
}
