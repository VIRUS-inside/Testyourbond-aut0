package org.yaml.snakeyaml.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.scanner.Scanner;
import org.yaml.snakeyaml.scanner.ScannerImpl;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;
import org.yaml.snakeyaml.util.ArrayStack;













































































public final class ParserImpl
  implements Parser
{
  private static final Map<String, String> DEFAULT_TAGS = new HashMap();
  
  static { DEFAULT_TAGS.put("!", "!");
    DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
  }
  

  private final Scanner scanner;
  
  private Event currentEvent;
  
  private final ArrayStack<Production> states;
  public ParserImpl(StreamReader reader)
  {
    scanner = new ScannerImpl(reader);
    currentEvent = null;
    directives = new VersionTagsTuple(null, new HashMap(DEFAULT_TAGS));
    states = new ArrayStack(100);
    marks = new ArrayStack(10);
    state = new ParseStreamStart(null);
  }
  


  public boolean checkEvent(Event.ID choices)
  {
    peekEvent();
    if ((currentEvent != null) && 
      (currentEvent.is(choices))) {
      return true;
    }
    
    return false;
  }
  


  public Event peekEvent()
  {
    if ((currentEvent == null) && 
      (state != null)) {
      currentEvent = state.produce();
    }
    
    return currentEvent;
  }
  


  public Event getEvent()
  {
    peekEvent();
    Event value = currentEvent;
    currentEvent = null;
    return value;
  }
  


  private class ParseStreamStart
    implements Production
  {
    private ParseStreamStart() {}
    

    public Event produce()
    {
      StreamStartToken token = (StreamStartToken)scanner.getToken();
      Event event = new StreamStartEvent(token.getStartMark(), token.getEndMark());
      
      state = new ParserImpl.ParseImplicitDocumentStart(ParserImpl.this, null);
      return event;
    }
  }
  
  private class ParseImplicitDocumentStart implements Production {
    private ParseImplicitDocumentStart() {}
    
    public Event produce() { if (!scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
        directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
        Token token = scanner.peekToken();
        Mark startMark = token.getStartMark();
        Mark endMark = startMark;
        Event event = new DocumentStartEvent(startMark, endMark, false, null, null);
        
        states.push(new ParserImpl.ParseDocumentEnd(ParserImpl.this, null));
        state = new ParserImpl.ParseBlockNode(ParserImpl.this, null);
        return event;
      }
      Production p = new ParserImpl.ParseDocumentStart(ParserImpl.this, null);
      return p.produce();
    }
  }
  
  private class ParseDocumentStart implements Production {
    private ParseDocumentStart() {}
    
    public Event produce() {
      while (scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
        scanner.getToken();
      }
      
      Event event;
      if (!scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
        Token token = scanner.peekToken();
        Mark startMark = token.getStartMark();
        VersionTagsTuple tuple = ParserImpl.this.processDirectives();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
          throw new ParserException(null, null, "expected '<document start>', but found " + scanner.peekToken().getTokenId(), scanner.peekToken().getStartMark());
        }
        
        token = scanner.getToken();
        Mark endMark = token.getEndMark();
        Event event = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
        
        states.push(new ParserImpl.ParseDocumentEnd(ParserImpl.this, null));
        state = new ParserImpl.ParseDocumentContent(ParserImpl.this, null);
      }
      else {
        StreamEndToken token = (StreamEndToken)scanner.getToken();
        event = new StreamEndEvent(token.getStartMark(), token.getEndMark());
        if (!states.isEmpty()) {
          throw new YAMLException("Unexpected end of stream. States left: " + states);
        }
        if (!marks.isEmpty()) {
          throw new YAMLException("Unexpected end of stream. Marks left: " + marks);
        }
        state = null;
      }
      return event;
    }
  }
  
  private class ParseDocumentEnd implements Production {
    private ParseDocumentEnd() {}
    
    public Event produce() { Token token = scanner.peekToken();
      Mark startMark = token.getStartMark();
      Mark endMark = startMark;
      boolean explicit = false;
      if (scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
        token = scanner.getToken();
        endMark = token.getEndMark();
        explicit = true;
      }
      Event event = new DocumentEndEvent(startMark, endMark, explicit);
      
      state = new ParserImpl.ParseDocumentStart(ParserImpl.this, null);
      return event;
    }
  }
  
  private class ParseDocumentContent implements Production {
    private ParseDocumentContent() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd }))
      {
        Event event = ParserImpl.this.processEmptyScalar(scanner.peekToken().getStartMark());
        state = ((Production)states.pop());
        return event;
      }
      Production p = new ParserImpl.ParseBlockNode(ParserImpl.this, null);
      return p.produce();
    }
  }
  

  private VersionTagsTuple processDirectives()
  {
    DumperOptions.Version yamlVersion = null;
    HashMap<String, String> tagHandles = new HashMap();
    while (scanner.checkToken(new Token.ID[] { Token.ID.Directive }))
    {
      DirectiveToken token = (DirectiveToken)scanner.getToken();
      if (token.getName().equals("YAML")) {
        if (yamlVersion != null) {
          throw new ParserException(null, null, "found duplicate YAML directive", token.getStartMark());
        }
        
        List<Integer> value = token.getValue();
        Integer major = (Integer)value.get(0);
        if (major.intValue() != 1) {
          throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token.getStartMark());
        }
        

        Integer minor = (Integer)value.get(1);
        switch (minor.intValue()) {
        case 0: 
          yamlVersion = DumperOptions.Version.V1_0;
          break;
        
        default: 
          yamlVersion = DumperOptions.Version.V1_1;
        }
      }
      else if (token.getName().equals("TAG")) {
        List<String> value = token.getValue();
        String handle = (String)value.get(0);
        String prefix = (String)value.get(1);
        if (tagHandles.containsKey(handle)) {
          throw new ParserException(null, null, "duplicate tag handle " + handle, token.getStartMark());
        }
        
        tagHandles.put(handle, prefix);
      }
    }
    if ((yamlVersion != null) || (!tagHandles.isEmpty()))
    {
      for (String key : DEFAULT_TAGS.keySet())
      {
        if (!tagHandles.containsKey(key)) {
          tagHandles.put(key, DEFAULT_TAGS.get(key));
        }
      }
      directives = new VersionTagsTuple(yamlVersion, tagHandles);
    }
    return directives;
  }
  



  private final ArrayStack<Mark> marks;
  


  private Production state;
  

  private VersionTagsTuple directives;
  

  private class ParseBlockNode
    implements Production
  {
    private ParseBlockNode() {}
    


    public Event produce()
    {
      return ParserImpl.this.parseNode(true, false);
    }
  }
  
  private Event parseFlowNode() {
    return parseNode(false, false);
  }
  
  private Event parseBlockNodeOrIndentlessSequence() {
    return parseNode(true, true);
  }
  
  private Event parseNode(boolean block, boolean indentlessSequence)
  {
    Mark startMark = null;
    Mark endMark = null;
    Mark tagMark = null;
    Event event; if (scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
      AliasToken token = (AliasToken)scanner.getToken();
      Event event = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
    } else {
      String anchor = null;
      TagTuple tagTokenTag = null;
      if (scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
        AnchorToken token = (AnchorToken)scanner.getToken();
        startMark = token.getStartMark();
        endMark = token.getEndMark();
        anchor = token.getValue();
        if (scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
          TagToken tagToken = (TagToken)scanner.getToken();
          tagMark = tagToken.getStartMark();
          endMark = tagToken.getEndMark();
          tagTokenTag = tagToken.getValue();
        }
      } else if (scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
        TagToken tagToken = (TagToken)scanner.getToken();
        startMark = tagToken.getStartMark();
        tagMark = startMark;
        endMark = tagToken.getEndMark();
        tagTokenTag = tagToken.getValue();
        if (scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
          AnchorToken token = (AnchorToken)scanner.getToken();
          endMark = token.getEndMark();
          anchor = token.getValue();
        }
      }
      String tag = null;
      if (tagTokenTag != null) {
        String handle = tagTokenTag.getHandle();
        String suffix = tagTokenTag.getSuffix();
        if (handle != null) {
          if (!directives.getTags().containsKey(handle)) {
            throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
          }
          
          tag = (String)directives.getTags().get(handle) + suffix;
        } else {
          tag = suffix;
        }
      }
      if (startMark == null) {
        startMark = scanner.peekToken().getStartMark();
        endMark = startMark;
      }
      event = null;
      boolean implicit = (tag == null) || (tag.equals("!"));
      if (indentlessSequence) if (scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
          endMark = scanner.peekToken().getEndMark();
          event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
          
          state = new ParseIndentlessSequenceEntry(null); return event;
        }
      if (scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
        ScalarToken token = (ScalarToken)scanner.getToken();
        endMark = token.getEndMark();
        ImplicitTuple implicitValues;
        ImplicitTuple implicitValues; if (((token.getPlain()) && (tag == null)) || ("!".equals(tag))) {
          implicitValues = new ImplicitTuple(true, false); } else { ImplicitTuple implicitValues;
          if (tag == null) {
            implicitValues = new ImplicitTuple(false, true);
          } else
            implicitValues = new ImplicitTuple(false, false);
        }
        event = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, Character.valueOf(token.getStyle()));
        
        state = ((Production)states.pop());
      } else if (scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
        endMark = scanner.peekToken().getEndMark();
        event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
        
        state = new ParseFlowSequenceFirstEntry(null);
      } else if (scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
        endMark = scanner.peekToken().getEndMark();
        event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
        
        state = new ParseFlowMappingFirstKey(null);
      } else { if (block) if (scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
            endMark = scanner.peekToken().getStartMark();
            event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
            
            state = new ParseBlockSequenceFirstEntry(null); return event; }
        if (block) if (scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
            endMark = scanner.peekToken().getStartMark();
            event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
            
            state = new ParseBlockMappingFirstKey(null); return event; }
        if ((anchor != null) || (tag != null))
        {

          event = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, Character.valueOf('\000'));
          
          state = ((Production)states.pop());
        } else { String node;
          String node;
          if (block) {
            node = "block";
          } else {
            node = "flow";
          }
          Token token = scanner.peekToken();
          throw new ParserException("while parsing a " + node + " node", startMark, "expected the node content, but found " + token.getTokenId(), token.getStartMark());
        }
      }
    }
    

    return event;
  }
  
  private class ParseBlockSequenceFirstEntry implements Production
  {
    private ParseBlockSequenceFirstEntry() {}
    
    public Event produce() {
      Token token = scanner.getToken();
      marks.push(token.getStartMark());
      return new ParserImpl.ParseBlockSequenceEntry(ParserImpl.this, null).produce();
    }
  }
  
  private class ParseBlockSequenceEntry implements Production { private ParseBlockSequenceEntry() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
        BlockEntryToken token = (BlockEntryToken)scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
          states.push(new ParseBlockSequenceEntry(ParserImpl.this));
          return new ParserImpl.ParseBlockNode(ParserImpl.this, null).produce();
        }
        state = new ParseBlockSequenceEntry(ParserImpl.this);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      if (!scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
        Token token = scanner.peekToken();
        throw new ParserException("while parsing a block collection", (Mark)marks.pop(), "expected <block end>, but found " + token.getTokenId(), token.getStartMark());
      }
      

      Token token = scanner.getToken();
      Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
      marks.pop();
      return event;
    }
  }
  
  private class ParseIndentlessSequenceEntry implements Production {
    private ParseIndentlessSequenceEntry() {}
    
    public Event produce() {
      if (scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
        Token token = scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd }))
        {
          states.push(new ParseIndentlessSequenceEntry(ParserImpl.this));
          return new ParserImpl.ParseBlockNode(ParserImpl.this, null).produce();
        }
        state = new ParseIndentlessSequenceEntry(ParserImpl.this);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      Token token = scanner.peekToken();
      Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
      return event;
    }
  }
  
  private class ParseBlockMappingFirstKey implements Production { private ParseBlockMappingFirstKey() {}
    
    public Event produce() { Token token = scanner.getToken();
      marks.push(token.getStartMark());
      return new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null).produce();
    }
  }
  
  private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
        Token token = scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
          states.push(new ParserImpl.ParseBlockMappingValue(ParserImpl.this, null));
          return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
        }
        state = new ParserImpl.ParseBlockMappingValue(ParserImpl.this, null);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      if (!scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
        Token token = scanner.peekToken();
        throw new ParserException("while parsing a block mapping", (Mark)marks.pop(), "expected <block end>, but found " + token.getTokenId(), token.getStartMark());
      }
      

      Token token = scanner.getToken();
      Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
      marks.pop();
      return event;
    }
  }
  
  private class ParseBlockMappingValue implements Production { private ParseBlockMappingValue() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
        Token token = scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
          states.push(new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null));
          return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
        }
        state = new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      state = new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null);
      Token token = scanner.peekToken();
      return ParserImpl.this.processEmptyScalar(token.getStartMark());
    }
  }
  




  private class ParseFlowSequenceFirstEntry
    implements Production
  {
    private ParseFlowSequenceFirstEntry() {}
    




    public Event produce()
    {
      Token token = scanner.getToken();
      marks.push(token.getStartMark());
      return new ParserImpl.ParseFlowSequenceEntry(ParserImpl.this, true).produce();
    }
  }
  
  private class ParseFlowSequenceEntry implements Production {
    private boolean first = false;
    
    public ParseFlowSequenceEntry(boolean first) {
      this.first = first;
    }
    
    public Event produce() {
      if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
        if (!first) {
          if (scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
            scanner.getToken();
          } else {
            Token token = scanner.peekToken();
            throw new ParserException("while parsing a flow sequence", (Mark)marks.pop(), "expected ',' or ']', but got " + token.getTokenId(), token.getStartMark());
          }
        }
        

        if (scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
          Token token = scanner.peekToken();
          Event event = new MappingStartEvent(null, null, true, token.getStartMark(), token.getEndMark(), Boolean.TRUE);
          
          state = new ParserImpl.ParseFlowSequenceEntryMappingKey(ParserImpl.this, null);
          return event; }
        if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
          states.push(new ParseFlowSequenceEntry(ParserImpl.this, false));
          return ParserImpl.this.parseFlowNode();
        }
      }
      Token token = scanner.getToken();
      Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
      marks.pop();
      return event;
    }
  }
  
  private class ParseFlowSequenceEntryMappingKey implements Production { private ParseFlowSequenceEntryMappingKey() {}
    
    public Event produce() { Token token = scanner.getToken();
      if (!scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
        states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue(ParserImpl.this, null));
        return ParserImpl.this.parseFlowNode();
      }
      state = new ParserImpl.ParseFlowSequenceEntryMappingValue(ParserImpl.this, null);
      return ParserImpl.this.processEmptyScalar(token.getEndMark());
    }
  }
  
  private class ParseFlowSequenceEntryMappingValue implements Production {
    private ParseFlowSequenceEntryMappingValue() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
        Token token = scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
          states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null));
          return ParserImpl.this.parseFlowNode();
        }
        state = new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      state = new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null);
      Token token = scanner.peekToken();
      return ParserImpl.this.processEmptyScalar(token.getStartMark());
    }
  }
  
  private class ParseFlowSequenceEntryMappingEnd implements Production {
    private ParseFlowSequenceEntryMappingEnd() {}
    
    public Event produce() { state = new ParserImpl.ParseFlowSequenceEntry(ParserImpl.this, false);
      Token token = scanner.peekToken();
      return new MappingEndEvent(token.getStartMark(), token.getEndMark());
    }
  }
  


  private class ParseFlowMappingFirstKey
    implements Production
  {
    private ParseFlowMappingFirstKey() {}
    


    public Event produce()
    {
      Token token = scanner.getToken();
      marks.push(token.getStartMark());
      return new ParserImpl.ParseFlowMappingKey(ParserImpl.this, true).produce();
    }
  }
  
  private class ParseFlowMappingKey implements Production {
    private boolean first = false;
    
    public ParseFlowMappingKey(boolean first) {
      this.first = first;
    }
    
    public Event produce() {
      if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
        if (!first) {
          if (scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
            scanner.getToken();
          } else {
            Token token = scanner.peekToken();
            throw new ParserException("while parsing a flow mapping", (Mark)marks.pop(), "expected ',' or '}', but got " + token.getTokenId(), token.getStartMark());
          }
        }
        

        if (scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
          Token token = scanner.getToken();
          if (!scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd }))
          {
            states.push(new ParserImpl.ParseFlowMappingValue(ParserImpl.this, null));
            return ParserImpl.this.parseFlowNode();
          }
          state = new ParserImpl.ParseFlowMappingValue(ParserImpl.this, null);
          return ParserImpl.this.processEmptyScalar(token.getEndMark());
        }
        if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
          states.push(new ParserImpl.ParseFlowMappingEmptyValue(ParserImpl.this, null));
          return ParserImpl.this.parseFlowNode();
        }
      }
      Token token = scanner.getToken();
      Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
      state = ((Production)states.pop());
      marks.pop();
      return event;
    }
  }
  
  private class ParseFlowMappingValue implements Production { private ParseFlowMappingValue() {}
    
    public Event produce() { if (scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
        Token token = scanner.getToken();
        if (!scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
          states.push(new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false));
          return ParserImpl.this.parseFlowNode();
        }
        state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
        return ParserImpl.this.processEmptyScalar(token.getEndMark());
      }
      
      state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
      Token token = scanner.peekToken();
      return ParserImpl.this.processEmptyScalar(token.getStartMark());
    }
  }
  
  private class ParseFlowMappingEmptyValue implements Production {
    private ParseFlowMappingEmptyValue() {}
    
    public Event produce() { state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
      return ParserImpl.this.processEmptyScalar(scanner.peekToken().getStartMark());
    }
  }
  







  private Event processEmptyScalar(Mark mark)
  {
    return new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, Character.valueOf('\000'));
  }
}
