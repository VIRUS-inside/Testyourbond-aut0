package org.yaml.snakeyaml.constructor;

import java.beans.IntrospectionException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
















public class Constructor
  extends SafeConstructor
{
  private final Map<Tag, Class<? extends Object>> typeTags;
  protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
  
  public Constructor()
  {
    this(Object.class);
  }
  





  public Constructor(Class<? extends Object> theRoot)
  {
    this(new TypeDescription(checkRoot(theRoot)));
  }
  


  private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot)
  {
    if (theRoot == null) {
      throw new NullPointerException("Root class must be provided.");
    }
    return theRoot;
  }
  
  public Constructor(TypeDescription theRoot) {
    if (theRoot == null) {
      throw new NullPointerException("Root type must be provided.");
    }
    yamlConstructors.put(null, new ConstructYamlObject());
    if (!Object.class.equals(theRoot.getType())) {
      rootTag = new Tag(theRoot.getType());
    }
    typeTags = new HashMap();
    typeDefinitions = new HashMap();
    yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
    yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
    yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
    addTypeDescription(theRoot);
  }
  







  public Constructor(String theRoot)
    throws ClassNotFoundException
  {
    this(Class.forName(check(theRoot)));
  }
  
  private static final String check(String s) {
    if (s == null) {
      throw new NullPointerException("Root type must be provided.");
    }
    if (s.trim().length() == 0) {
      throw new YAMLException("Root type must be provided.");
    }
    return s;
  }
  









  public TypeDescription addTypeDescription(TypeDescription definition)
  {
    if (definition == null) {
      throw new NullPointerException("TypeDescription is required.");
    }
    Tag tag = definition.getTag();
    typeTags.put(tag, definition.getType());
    return (TypeDescription)typeDefinitions.put(definition.getType(), definition);
  }
  





  protected class ConstructMapping
    implements Construct
  {
    protected ConstructMapping() {}
    




    public Object construct(Node node)
    {
      MappingNode mnode = (MappingNode)node;
      if (Properties.class.isAssignableFrom(node.getType())) {
        Properties properties = new Properties();
        if (!node.isTwoStepsConstruction()) {
          constructMapping2ndStep(mnode, properties);
        } else {
          throw new YAMLException("Properties must not be recursive.");
        }
        return properties; }
      if (SortedMap.class.isAssignableFrom(node.getType())) {
        SortedMap<Object, Object> map = new TreeMap();
        if (!node.isTwoStepsConstruction()) {
          constructMapping2ndStep(mnode, map);
        }
        return map; }
      if (Map.class.isAssignableFrom(node.getType())) {
        if (node.isTwoStepsConstruction()) {
          return createDefaultMap();
        }
        return constructMapping(mnode);
      }
      if (SortedSet.class.isAssignableFrom(node.getType())) {
        SortedSet<Object> set = new TreeSet();
        

        constructSet2ndStep(mnode, set);
        
        return set; }
      if (Collection.class.isAssignableFrom(node.getType())) {
        if (node.isTwoStepsConstruction()) {
          return createDefaultSet();
        }
        return constructSet(mnode);
      }
      
      if (node.isTwoStepsConstruction()) {
        return createEmptyJavaBean(mnode);
      }
      return constructJavaBean2ndStep(mnode, createEmptyJavaBean(mnode));
    }
    


    public void construct2ndStep(Node node, Object object)
    {
      if (Map.class.isAssignableFrom(node.getType())) {
        constructMapping2ndStep((MappingNode)node, (Map)object);
      } else if (Set.class.isAssignableFrom(node.getType())) {
        constructSet2ndStep((MappingNode)node, (Set)object);
      } else {
        constructJavaBean2ndStep((MappingNode)node, object);
      }
    }
    






    protected Object createEmptyJavaBean(MappingNode node)
    {
      try
      {
        java.lang.reflect.Constructor<?> c = node.getType().getDeclaredConstructor(new Class[0]);
        c.setAccessible(true);
        return c.newInstance(new Object[0]);
      } catch (Exception e) {
        throw new YAMLException(e);
      }
    }
    
    protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
      flattenMapping(node);
      Class<? extends Object> beanType = node.getType();
      List<NodeTuple> nodeValue = node.getValue();
      for (NodeTuple tuple : nodeValue) {
        ScalarNode keyNode;
        if ((tuple.getKeyNode() instanceof ScalarNode))
        {
          keyNode = (ScalarNode)tuple.getKeyNode();
        } else
          throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
        ScalarNode keyNode;
        Node valueNode = tuple.getValueNode();
        
        keyNode.setType(String.class);
        String key = (String)constructObject(keyNode);
        try {
          Property property = getProperty(beanType, key);
          valueNode.setType(property.getType());
          TypeDescription memberDescription = (TypeDescription)typeDefinitions.get(beanType);
          boolean typeDetected = false;
          if (memberDescription != null) {
            switch (Constructor.1.$SwitchMap$org$yaml$snakeyaml$nodes$NodeId[valueNode.getNodeId().ordinal()]) {
            case 1: 
              SequenceNode snode = (SequenceNode)valueNode;
              Class<? extends Object> memberType = memberDescription.getListPropertyType(key);
              
              if (memberType != null) {
                snode.setListType(memberType);
                typeDetected = true;
              } else if (property.getType().isArray()) {
                snode.setListType(property.getType().getComponentType());
                typeDetected = true;
              }
              break;
            case 2: 
              MappingNode mnode = (MappingNode)valueNode;
              Class<? extends Object> keyType = memberDescription.getMapKeyType(key);
              if (keyType != null) {
                mnode.setTypes(keyType, memberDescription.getMapValueType(key));
                typeDetected = true;
              }
              break;
            }
            
          }
          if ((!typeDetected) && (valueNode.getNodeId() != NodeId.scalar))
          {
            Class<?>[] arguments = property.getActualTypeArguments();
            if ((arguments != null) && (arguments.length > 0))
            {

              if (valueNode.getNodeId() == NodeId.sequence) {
                Class<?> t = arguments[0];
                SequenceNode snode = (SequenceNode)valueNode;
                snode.setListType(t);
              } else if (valueNode.getTag().equals(Tag.SET)) {
                Class<?> t = arguments[0];
                MappingNode mnode = (MappingNode)valueNode;
                mnode.setOnlyKeyType(t);
                mnode.setUseClassConstructor(Boolean.valueOf(true));
              } else if (property.getType().isAssignableFrom(Map.class)) {
                Class<?> ketType = arguments[0];
                Class<?> valueType = arguments[1];
                MappingNode mnode = (MappingNode)valueNode;
                mnode.setTypes(ketType, valueType);
                mnode.setUseClassConstructor(Boolean.valueOf(true));
              }
            }
          }
          



          Object value = constructObject(valueNode);
          

          if (((property.getType() == Float.TYPE) || (property.getType() == Float.class)) && 
            ((value instanceof Double))) {
            value = Float.valueOf(((Double)value).floatValue());
          }
          

          property.set(object, value);
        } catch (Exception e) {
          throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node.getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
        }
      }
      

      return object;
    }
    
    protected Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException
    {
      return getPropertyUtils().getProperty(type, name);
    }
  }
  

  protected class ConstructYamlObject
    implements Construct
  {
    protected ConstructYamlObject() {}
    

    private Construct getConstructor(Node node)
    {
      Class<?> cl = getClassForNode(node);
      node.setType(cl);
      
      Construct constructor = (Construct)yamlClassConstructors.get(node.getNodeId());
      return constructor;
    }
    
    public Object construct(Node node) {
      Object result = null;
      try {
        result = getConstructor(node).construct(node);
      } catch (ConstructorException e) {
        throw e;
      } catch (Exception e) {
        throw new ConstructorException(null, null, "Can't construct a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
      }
      
      return result;
    }
    
    public void construct2ndStep(Node node, Object object) {
      try {
        getConstructor(node).construct2ndStep(node, object);
      } catch (Exception e) {
        throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
      }
    }
  }
  

  protected class ConstructScalar
    extends AbstractConstruct
  {
    protected ConstructScalar() {}
    
    public Object construct(Node nnode)
    {
      ScalarNode node = (ScalarNode)nnode;
      Class<?> type = node.getType();
      Object result;
      Object result; if ((type.isPrimitive()) || (type == String.class) || (Number.class.isAssignableFrom(type)) || (type == Boolean.class) || (Date.class.isAssignableFrom(type)) || (type == Character.class) || (type == BigInteger.class) || (type == BigDecimal.class) || (Enum.class.isAssignableFrom(type)) || (Tag.BINARY.equals(node.getTag())) || (Calendar.class.isAssignableFrom(type)))
      {




        result = constructStandardJavaInstance(type, node);
      }
      else {
        java.lang.reflect.Constructor<?>[] javaConstructors = type.getConstructors();
        int oneArgCount = 0;
        java.lang.reflect.Constructor<?> javaConstructor = null;
        for (java.lang.reflect.Constructor<?> c : javaConstructors) {
          if (c.getParameterTypes().length == 1) {
            oneArgCount++;
            javaConstructor = c;
          }
        }
        
        if (javaConstructor == null)
          throw new YAMLException("No single argument constructor found for " + type);
        Object argument; Object argument; if (oneArgCount == 1) {
          argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);



        }
        else
        {


          argument = constructScalar(node);
          try {
            javaConstructor = type.getConstructor(new Class[] { String.class });
          } catch (Exception e) {
            throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e.getMessage(), e);
          }
        }
        
        try
        {
          result = javaConstructor.newInstance(new Object[] { argument });
        } catch (Exception e) {
          throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
        }
      }
      

      return result;
    }
    

    private Object constructStandardJavaInstance(Class type, ScalarNode node)
    {
      Object result;
      if (type == String.class) {
        Construct stringConstructor = (Construct)yamlConstructors.get(Tag.STR);
        result = stringConstructor.construct(node); } else { Object result;
        if ((type == Boolean.class) || (type == Boolean.TYPE)) {
          Construct boolConstructor = (Construct)yamlConstructors.get(Tag.BOOL);
          result = boolConstructor.construct(node); } else { Object result;
          if ((type == Character.class) || (type == Character.TYPE)) {
            Construct charConstructor = (Construct)yamlConstructors.get(Tag.STR);
            String ch = (String)charConstructor.construct(node);
            Object result; if (ch.length() == 0) {
              result = null;
            } else { if (ch.length() != 1) {
                throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
              }
              
              result = Character.valueOf(ch.charAt(0));
            }
          } else if (Date.class.isAssignableFrom(type)) {
            Construct dateConstructor = (Construct)yamlConstructors.get(Tag.TIMESTAMP);
            Date date = (Date)dateConstructor.construct(node);
            Object result; if (type == Date.class) {
              result = date;
            } else {
              try {
                java.lang.reflect.Constructor<?> constr = type.getConstructor(new Class[] { Long.TYPE });
                result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
              } catch (RuntimeException e) { Object result;
                throw e;
              } catch (Exception e) {
                throw new YAMLException("Cannot construct: '" + type + "'");
              }
            }
          } else if ((type == Float.class) || (type == Double.class) || (type == Float.TYPE) || (type == Double.TYPE) || (type == BigDecimal.class)) {
            Object result;
            if (type == BigDecimal.class) {
              result = new BigDecimal(node.getValue());
            } else {
              Construct doubleConstructor = (Construct)yamlConstructors.get(Tag.FLOAT);
              Object result = doubleConstructor.construct(node);
              if ((type == Float.class) || (type == Float.TYPE)) {
                result = new Float(((Double)result).doubleValue());
              }
            }
          } else if ((type == Byte.class) || (type == Short.class) || (type == Integer.class) || (type == Long.class) || (type == BigInteger.class) || (type == Byte.TYPE) || (type == Short.TYPE) || (type == Integer.TYPE) || (type == Long.TYPE))
          {

            Construct intConstructor = (Construct)yamlConstructors.get(Tag.INT);
            Object result = intConstructor.construct(node);
            if ((type == Byte.class) || (type == Byte.TYPE)) {
              result = Byte.valueOf(result.toString());
            } else if ((type == Short.class) || (type == Short.TYPE)) {
              result = Short.valueOf(result.toString());
            } else if ((type == Integer.class) || (type == Integer.TYPE)) {
              result = Integer.valueOf(Integer.parseInt(result.toString()));
            } else if ((type == Long.class) || (type == Long.TYPE)) {
              result = Long.valueOf(result.toString());
            }
            else {
              result = new BigInteger(result.toString());
            }
          } else if (Enum.class.isAssignableFrom(type)) {
            String enumValueName = node.getValue();
            try {
              result = Enum.valueOf(type, enumValueName);
            } catch (Exception ex) { Object result;
              throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type.getName());
            }
          } else { Object result;
            if (Calendar.class.isAssignableFrom(type)) {
              SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
              contr.construct(node);
              result = contr.getCalendar(); } else { Object result;
              if (Number.class.isAssignableFrom(type)) {
                SafeConstructor.ConstructYamlNumber contr = new SafeConstructor.ConstructYamlNumber(Constructor.this);
                result = contr.construct(node);
              } else {
                throw new YAMLException("Unsupported class: " + type); } } } } }
      Object result;
      return result;
    }
  }
  
  protected class ConstructSequence
    implements Construct
  {
    protected ConstructSequence() {}
    
    public Object construct(Node node)
    {
      SequenceNode snode = (SequenceNode)node;
      if (Set.class.isAssignableFrom(node.getType())) {
        if (node.isTwoStepsConstruction()) {
          throw new YAMLException("Set cannot be recursive.");
        }
        return constructSet(snode);
      }
      if (Collection.class.isAssignableFrom(node.getType())) {
        if (node.isTwoStepsConstruction()) {
          return createDefaultList(snode.getValue().size());
        }
        return constructSequence(snode);
      }
      if (node.getType().isArray()) {
        if (node.isTwoStepsConstruction()) {
          return createArray(node.getType(), snode.getValue().size());
        }
        return constructArray(snode);
      }
      

      List<java.lang.reflect.Constructor<?>> possibleConstructors = new ArrayList(snode.getValue().size());
      
      for (java.lang.reflect.Constructor<?> constructor : node.getType().getConstructors())
      {
        if (snode.getValue().size() == constructor.getParameterTypes().length)
          possibleConstructors.add(constructor); }
      List<Object> argumentList;
      Class<?>[] parameterTypes;
      if (!possibleConstructors.isEmpty()) {
        if (possibleConstructors.size() == 1) {
          Object[] argumentList = new Object[snode.getValue().size()];
          java.lang.reflect.Constructor<?> c = (java.lang.reflect.Constructor)possibleConstructors.get(0);
          int index = 0;
          for (Node argumentNode : snode.getValue()) {
            Class<?> type = c.getParameterTypes()[index];
            
            argumentNode.setType(type);
            argumentList[(index++)] = constructObject(argumentNode);
          }
          try
          {
            return c.newInstance(argumentList);
          } catch (Exception e) {
            throw new YAMLException(e);
          }
        }
        

        argumentList = constructSequence(snode);
        parameterTypes = new Class[argumentList.size()];
        int index = 0;
        for (Object parameter : argumentList) {
          parameterTypes[index] = parameter.getClass();
          index++;
        }
        
        for (java.lang.reflect.Constructor<?> c : possibleConstructors) {
          Class<?>[] argTypes = c.getParameterTypes();
          boolean foundConstructor = true;
          for (int i = 0; i < argTypes.length; i++) {
            if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
              foundConstructor = false;
              break;
            }
          }
          if (foundConstructor) {
            try {
              return c.newInstance(argumentList.toArray());
            } catch (Exception e) {
              throw new YAMLException(e);
            }
          }
        }
      }
      throw new YAMLException("No suitable constructor with " + String.valueOf(snode.getValue().size()) + " arguments found for " + node.getType());
    }
    



    private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz)
    {
      if (!clazz.isPrimitive()) {
        return clazz;
      }
      if (clazz == Integer.TYPE) {
        return Integer.class;
      }
      if (clazz == Float.TYPE) {
        return Float.class;
      }
      if (clazz == Double.TYPE) {
        return Double.class;
      }
      if (clazz == Boolean.TYPE) {
        return Boolean.class;
      }
      if (clazz == Long.TYPE) {
        return Long.class;
      }
      if (clazz == Character.TYPE) {
        return Character.class;
      }
      if (clazz == Short.TYPE) {
        return Short.class;
      }
      if (clazz == Byte.TYPE) {
        return Byte.class;
      }
      throw new YAMLException("Unexpected primitive " + clazz);
    }
    
    public void construct2ndStep(Node node, Object object)
    {
      SequenceNode snode = (SequenceNode)node;
      if (List.class.isAssignableFrom(node.getType())) {
        List<Object> list = (List)object;
        constructSequenceStep2(snode, list);
      } else if (node.getType().isArray()) {
        constructArrayStep2(snode, object);
      } else {
        throw new YAMLException("Immutable objects cannot be recursive.");
      }
    }
  }
  
  protected Class<?> getClassForNode(Node node) {
    Class<? extends Object> classForTag = (Class)typeTags.get(node.getTag());
    if (classForTag == null) {
      String name = node.getTag().getClassName();
      Class<?> cl;
      try {
        cl = getClassForName(name);
      } catch (ClassNotFoundException e) {
        throw new YAMLException("Class not found: " + name);
      }
      typeTags.put(node.getTag(), cl);
      return cl;
    }
    return classForTag;
  }
  
  protected Class<?> getClassForName(String name) throws ClassNotFoundException
  {
    return Class.forName(name);
  }
}
