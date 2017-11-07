package org.yaml.snakeyaml.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;



















public class Resolver
{
  public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
  





  public static final Pattern FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
  
  public static final Pattern INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
  
  public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
  public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
  public static final Pattern EMPTY = Pattern.compile("^$");
  public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
  
  public static final Pattern VALUE = Pattern.compile("^(?:=)$");
  public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
  
  protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap();
  
  protected void addImplicitResolvers() {
    addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
    




    addImplicitResolver(Tag.INT, INT, "-+0123456789");
    addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
    addImplicitResolver(Tag.MERGE, MERGE, "<");
    addImplicitResolver(Tag.NULL, NULL, "~nN\000");
    addImplicitResolver(Tag.NULL, EMPTY, null);
    addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
    



    addImplicitResolver(Tag.YAML, YAML, "!&*");
  }
  
  public Resolver() {
    addImplicitResolvers();
  }
  
  public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
    if (first == null) {
      List<ResolverTuple> curr = (List)yamlImplicitResolvers.get(null);
      if (curr == null) {
        curr = new ArrayList();
        yamlImplicitResolvers.put(null, curr);
      }
      curr.add(new ResolverTuple(tag, regexp));
    } else {
      char[] chrs = first.toCharArray();
      int i = 0; for (int j = chrs.length; i < j; i++) {
        Character theC = Character.valueOf(chrs[i]);
        if (theC.charValue() == 0)
        {
          theC = null;
        }
        List<ResolverTuple> curr = (List)yamlImplicitResolvers.get(theC);
        if (curr == null) {
          curr = new ArrayList();
          yamlImplicitResolvers.put(theC, curr);
        }
        curr.add(new ResolverTuple(tag, regexp));
      }
    }
  }
  
  public Tag resolve(NodeId kind, String value, boolean implicit) {
    if ((kind == NodeId.scalar) && (implicit)) {
      List<ResolverTuple> resolvers = null;
      if (value.length() == 0) {
        resolvers = (List)yamlImplicitResolvers.get(Character.valueOf('\000'));
      } else {
        resolvers = (List)yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
      }
      if (resolvers != null) {
        for (ResolverTuple v : resolvers) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (regexp.matcher(value).matches()) {
            return tag;
          }
        }
      }
      if (yamlImplicitResolvers.containsKey(null)) {
        for (ResolverTuple v : (List)yamlImplicitResolvers.get(null)) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (regexp.matcher(value).matches()) {
            return tag;
          }
        }
      }
    }
    switch (1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[kind.ordinal()]) {
    case 1: 
      return Tag.STR;
    case 2: 
      return Tag.SEQ;
    }
    return Tag.MAP;
  }
}
