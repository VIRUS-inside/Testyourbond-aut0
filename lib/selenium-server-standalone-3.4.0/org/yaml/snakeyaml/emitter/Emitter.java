package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.CollectionEndEvent;
import org.yaml.snakeyaml.events.CollectionStartEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.scanner.Constant;
import org.yaml.snakeyaml.util.ArrayStack;























public final class Emitter
  implements Emitable
{
  private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap();
  
  public static final int MIN_INDENT = 1;
  public static final int MAX_INDENT = 10;
  private static final char[] SPACE = { ' ' };
  private static final Map<String, String> DEFAULT_TAG_PREFIXES;
  
  static { ESCAPE_REPLACEMENTS.put(Character.valueOf('\000'), "0");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
    ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
    

    DEFAULT_TAG_PREFIXES = new LinkedHashMap();
    
    DEFAULT_TAG_PREFIXES.put("!", "!");
    DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
  }
  


  private final Writer stream;
  

  private final ArrayStack<EmitterState> states;
  

  private EmitterState state;
  

  private final Queue<Event> events;
  

  private Event event;
  
  private final ArrayStack<Integer> indents;
  
  private Integer indent;
  
  private int flowLevel;
  
  private boolean rootContext;
  
  private boolean mappingContext;
  
  private boolean simpleKeyContext;
  
  private int column;
  
  private boolean whitespace;
  
  private boolean indention;
  
  private boolean openEnded;
  
  private Boolean canonical;
  
  private Boolean prettyFlow;
  
  private boolean allowUnicode;
  
  private int bestIndent;
  
  private int bestWidth;
  
  private char[] bestLineBreak;
  
  private boolean splitLines;
  
  private Map<String, String> tagPrefixes;
  
  private String preparedAnchor;
  
  private String preparedTag;
  
  private ScalarAnalysis analysis;
  
  private Character style;
  
  public Emitter(Writer stream, DumperOptions opts)
  {
    this.stream = stream;
    

    states = new ArrayStack(100);
    state = new ExpectStreamStart(null);
    
    events = new ArrayBlockingQueue(100);
    event = null;
    
    indents = new ArrayStack(10);
    indent = null;
    
    flowLevel = 0;
    
    mappingContext = false;
    simpleKeyContext = false;
    






    column = 0;
    whitespace = true;
    indention = true;
    

    openEnded = false;
    

    canonical = Boolean.valueOf(opts.isCanonical());
    prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
    allowUnicode = opts.isAllowUnicode();
    bestIndent = 2;
    if ((opts.getIndent() > 1) && (opts.getIndent() < 10)) {
      bestIndent = opts.getIndent();
    }
    bestWidth = 80;
    if (opts.getWidth() > bestIndent * 2) {
      bestWidth = opts.getWidth();
    }
    bestLineBreak = opts.getLineBreak().getString().toCharArray();
    splitLines = opts.getSplitLines();
    

    tagPrefixes = new LinkedHashMap();
    

    preparedAnchor = null;
    preparedTag = null;
    

    analysis = null;
    style = null;
  }
  
  public void emit(Event event) throws IOException {
    events.add(event);
    while (!needMoreEvents()) {
      this.event = ((Event)events.poll());
      state.expect();
      this.event = null;
    }
  }
  

  private boolean needMoreEvents()
  {
    if (events.isEmpty()) {
      return true;
    }
    Event event = (Event)events.peek();
    if ((event instanceof DocumentStartEvent))
      return needEvents(1);
    if ((event instanceof SequenceStartEvent))
      return needEvents(2);
    if ((event instanceof MappingStartEvent)) {
      return needEvents(3);
    }
    return false;
  }
  
  private boolean needEvents(int count)
  {
    int level = 0;
    Iterator<Event> iter = events.iterator();
    iter.next();
    while (iter.hasNext()) {
      Event event = (Event)iter.next();
      if (((event instanceof DocumentStartEvent)) || ((event instanceof CollectionStartEvent))) {
        level++;
      } else if (((event instanceof DocumentEndEvent)) || ((event instanceof CollectionEndEvent))) {
        level--;
      } else if ((event instanceof StreamEndEvent)) {
        level = -1;
      }
      if (level < 0) {
        return false;
      }
    }
    return events.size() < count + 1;
  }
  
  private void increaseIndent(boolean flow, boolean indentless) {
    indents.push(indent);
    if (indent == null) {
      if (flow) {
        indent = Integer.valueOf(bestIndent);
      } else {
        indent = Integer.valueOf(0);
      }
    } else if (!indentless) {
      Emitter localEmitter = this;(localEmitter.indent = Integer.valueOf(indent.intValue() + bestIndent));
    }
  }
  
  private class ExpectStreamStart implements EmitterState
  {
    private ExpectStreamStart() {}
    
    public void expect() throws IOException
    {
      if ((event instanceof StreamStartEvent)) {
        writeStreamStart();
        state = new Emitter.ExpectFirstDocumentStart(Emitter.this, null);
      } else {
        throw new EmitterException("expected StreamStartEvent, but got " + event);
      }
    }
  }
  
  private class ExpectNothing implements EmitterState { private ExpectNothing() {}
    
    public void expect() throws IOException { throw new EmitterException("expecting nothing, but got " + event); }
  }
  
  private class ExpectFirstDocumentStart implements EmitterState
  {
    private ExpectFirstDocumentStart() {}
    
    public void expect() throws IOException {
      new Emitter.ExpectDocumentStart(Emitter.this, true).expect();
    }
  }
  
  private class ExpectDocumentStart implements EmitterState {
    private boolean first;
    
    public ExpectDocumentStart(boolean first) {
      this.first = first;
    }
    
    public void expect() throws IOException {
      if ((event instanceof DocumentStartEvent)) {
        DocumentStartEvent ev = (DocumentStartEvent)event;
        if (((ev.getVersion() != null) || (ev.getTags() != null)) && (openEnded)) {
          writeIndicator("...", true, false, false);
          writeIndent();
        }
        if (ev.getVersion() != null) {
          String versionText = Emitter.this.prepareVersion(ev.getVersion());
          writeVersionDirective(versionText);
        }
        tagPrefixes = new LinkedHashMap(Emitter.DEFAULT_TAG_PREFIXES);
        if (ev.getTags() != null) {
          Set<String> handles = new TreeSet(ev.getTags().keySet());
          for (String handle : handles) {
            String prefix = (String)ev.getTags().get(handle);
            tagPrefixes.put(prefix, handle);
            String handleText = Emitter.this.prepareTagHandle(handle);
            String prefixText = Emitter.this.prepareTagPrefix(prefix);
            writeTagDirective(handleText, prefixText);
          }
        }
        boolean implicit = (first) && (!ev.getExplicit()) && (!canonical.booleanValue()) && (ev.getVersion() == null) && ((ev.getTags() == null) || (ev.getTags().isEmpty())) && (!Emitter.this.checkEmptyDocument());
        


        if (!implicit) {
          writeIndent();
          writeIndicator("---", true, false, false);
          if (canonical.booleanValue()) {
            writeIndent();
          }
        }
        state = new Emitter.ExpectDocumentRoot(Emitter.this, null);
      } else if ((event instanceof StreamEndEvent))
      {




        writeStreamEnd();
        state = new Emitter.ExpectNothing(Emitter.this, null);
      } else {
        throw new EmitterException("expected DocumentStartEvent, but got " + event);
      }
    }
  }
  
  private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
    
    public void expect() throws IOException { if ((event instanceof DocumentEndEvent)) {
        writeIndent();
        if (((DocumentEndEvent)event).getExplicit()) {
          writeIndicator("...", true, false, false);
          writeIndent();
        }
        flushStream();
        state = new Emitter.ExpectDocumentStart(Emitter.this, false);
      } else {
        throw new EmitterException("expected DocumentEndEvent, but got " + event);
      }
    }
  }
  
  private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
    
    public void expect() throws IOException { states.push(new Emitter.ExpectDocumentEnd(Emitter.this, null));
      Emitter.this.expectNode(true, false, false);
    }
  }
  
  private void expectNode(boolean root, boolean mapping, boolean simpleKey)
    throws IOException
  {
    rootContext = root;
    mappingContext = mapping;
    simpleKeyContext = simpleKey;
    if ((event instanceof AliasEvent)) {
      expectAlias();
    } else if (((event instanceof ScalarEvent)) || ((event instanceof CollectionStartEvent))) {
      processAnchor("&");
      processTag();
      if ((event instanceof ScalarEvent)) {
        expectScalar();
      } else if ((event instanceof SequenceStartEvent)) {
        if ((flowLevel != 0) || (canonical.booleanValue()) || (((SequenceStartEvent)event).getFlowStyle().booleanValue()) || (checkEmptySequence()))
        {
          expectFlowSequence();
        } else {
          expectBlockSequence();
        }
      }
      else if ((flowLevel != 0) || (canonical.booleanValue()) || (((MappingStartEvent)event).getFlowStyle().booleanValue()) || (checkEmptyMapping()))
      {
        expectFlowMapping();
      } else {
        expectBlockMapping();
      }
    }
    else {
      throw new EmitterException("expected NodeEvent, but got " + event);
    }
  }
  
  private void expectAlias() throws IOException {
    if (((NodeEvent)event).getAnchor() == null) {
      throw new EmitterException("anchor is not specified for alias");
    }
    processAnchor("*");
    state = ((EmitterState)states.pop());
  }
  
  private void expectScalar() throws IOException {
    increaseIndent(true, false);
    processScalar();
    indent = ((Integer)indents.pop());
    state = ((EmitterState)states.pop());
  }
  
  private void expectFlowSequence()
    throws IOException
  {
    writeIndicator("[", true, true, false);
    flowLevel += 1;
    increaseIndent(true, false);
    if (prettyFlow.booleanValue()) {
      writeIndent();
    }
    state = new ExpectFirstFlowSequenceItem(null);
  }
  
  private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
    
    public void expect() throws IOException { if ((event instanceof SequenceEndEvent)) {
        indent = ((Integer)indents.pop());
        Emitter.access$2010(Emitter.this);
        writeIndicator("]", false, false, false);
        state = ((EmitterState)states.pop());
      } else {
        if ((canonical.booleanValue()) || ((column > bestWidth) && (splitLines)) || (prettyFlow.booleanValue())) {
          writeIndent();
        }
        states.push(new Emitter.ExpectFlowSequenceItem(Emitter.this, null));
        Emitter.this.expectNode(false, false, false);
      }
    }
  }
  
  private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
    
    public void expect() throws IOException { if ((event instanceof SequenceEndEvent)) {
        indent = ((Integer)indents.pop());
        Emitter.access$2010(Emitter.this);
        if (canonical.booleanValue()) {
          writeIndicator(",", false, false, false);
          writeIndent();
        }
        writeIndicator("]", false, false, false);
        if (prettyFlow.booleanValue()) {
          writeIndent();
        }
        state = ((EmitterState)states.pop());
      } else {
        writeIndicator(",", false, false, false);
        if ((canonical.booleanValue()) || ((column > bestWidth) && (splitLines)) || (prettyFlow.booleanValue())) {
          writeIndent();
        }
        states.push(new ExpectFlowSequenceItem(Emitter.this));
        Emitter.this.expectNode(false, false, false);
      }
    }
  }
  
  private void expectFlowMapping()
    throws IOException
  {
    writeIndicator("{", true, true, false);
    flowLevel += 1;
    increaseIndent(true, false);
    if (prettyFlow.booleanValue()) {
      writeIndent();
    }
    state = new ExpectFirstFlowMappingKey(null);
  }
  
  private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
    
    public void expect() throws IOException { if ((event instanceof MappingEndEvent)) {
        indent = ((Integer)indents.pop());
        Emitter.access$2010(Emitter.this);
        writeIndicator("}", false, false, false);
        state = ((EmitterState)states.pop());
      } else {
        if ((canonical.booleanValue()) || ((column > bestWidth) && (splitLines)) || (prettyFlow.booleanValue())) {
          writeIndent();
        }
        if ((!canonical.booleanValue()) && (Emitter.this.checkSimpleKey())) {
          states.push(new Emitter.ExpectFlowMappingSimpleValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, true);
        } else {
          writeIndicator("?", true, false, false);
          states.push(new Emitter.ExpectFlowMappingValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, false);
        }
      }
    }
  }
  
  private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
    
    public void expect() throws IOException { if ((event instanceof MappingEndEvent)) {
        indent = ((Integer)indents.pop());
        Emitter.access$2010(Emitter.this);
        if (canonical.booleanValue()) {
          writeIndicator(",", false, false, false);
          writeIndent();
        }
        if (prettyFlow.booleanValue()) {
          writeIndent();
        }
        writeIndicator("}", false, false, false);
        state = ((EmitterState)states.pop());
      } else {
        writeIndicator(",", false, false, false);
        if ((canonical.booleanValue()) || ((column > bestWidth) && (splitLines)) || (prettyFlow.booleanValue())) {
          writeIndent();
        }
        if ((!canonical.booleanValue()) && (Emitter.this.checkSimpleKey())) {
          states.push(new Emitter.ExpectFlowMappingSimpleValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, true);
        } else {
          writeIndicator("?", true, false, false);
          states.push(new Emitter.ExpectFlowMappingValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, false);
        }
      }
    }
  }
  
  private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
    
    public void expect() throws IOException { writeIndicator(":", false, false, false);
      states.push(new Emitter.ExpectFlowMappingKey(Emitter.this, null));
      Emitter.this.expectNode(false, true, false);
    }
  }
  
  private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
    
    public void expect() throws IOException { if ((canonical.booleanValue()) || (column > bestWidth) || (prettyFlow.booleanValue())) {
        writeIndent();
      }
      writeIndicator(":", true, false, false);
      states.push(new Emitter.ExpectFlowMappingKey(Emitter.this, null));
      Emitter.this.expectNode(false, true, false);
    }
  }
  
  private void expectBlockSequence()
    throws IOException
  {
    boolean indentless = (mappingContext) && (!indention);
    increaseIndent(false, indentless);
    state = new ExpectFirstBlockSequenceItem(null);
  }
  
  private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
    
    public void expect() throws IOException { new Emitter.ExpectBlockSequenceItem(Emitter.this, true).expect(); }
  }
  
  private class ExpectBlockSequenceItem implements EmitterState
  {
    private boolean first;
    
    public ExpectBlockSequenceItem(boolean first) {
      this.first = first;
    }
    
    public void expect() throws IOException {
      if ((!first) && ((event instanceof SequenceEndEvent))) {
        indent = ((Integer)indents.pop());
        state = ((EmitterState)states.pop());
      } else {
        writeIndent();
        writeIndicator("-", true, false, true);
        states.push(new ExpectBlockSequenceItem(Emitter.this, false));
        Emitter.this.expectNode(false, false, false);
      }
    }
  }
  
  private void expectBlockMapping() throws IOException
  {
    increaseIndent(false, false);
    state = new ExpectFirstBlockMappingKey(null);
  }
  
  private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
    
    public void expect() throws IOException { new Emitter.ExpectBlockMappingKey(Emitter.this, true).expect(); }
  }
  
  private class ExpectBlockMappingKey implements EmitterState
  {
    private boolean first;
    
    public ExpectBlockMappingKey(boolean first) {
      this.first = first;
    }
    
    public void expect() throws IOException {
      if ((!first) && ((event instanceof MappingEndEvent))) {
        indent = ((Integer)indents.pop());
        state = ((EmitterState)states.pop());
      } else {
        writeIndent();
        if (Emitter.this.checkSimpleKey()) {
          states.push(new Emitter.ExpectBlockMappingSimpleValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, true);
        } else {
          writeIndicator("?", true, false, true);
          states.push(new Emitter.ExpectBlockMappingValue(Emitter.this, null));
          Emitter.this.expectNode(false, true, false);
        }
      }
    }
  }
  
  private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
    
    public void expect() throws IOException { writeIndicator(":", false, false, false);
      states.push(new Emitter.ExpectBlockMappingKey(Emitter.this, false));
      Emitter.this.expectNode(false, true, false);
    }
  }
  
  private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
    
    public void expect() throws IOException { writeIndent();
      writeIndicator(":", true, false, true);
      states.push(new Emitter.ExpectBlockMappingKey(Emitter.this, false));
      Emitter.this.expectNode(false, true, false);
    }
  }
  

  private boolean checkEmptySequence()
  {
    return ((event instanceof SequenceStartEvent)) && (!events.isEmpty()) && ((events.peek() instanceof SequenceEndEvent));
  }
  
  private boolean checkEmptyMapping() {
    return ((event instanceof MappingStartEvent)) && (!events.isEmpty()) && ((events.peek() instanceof MappingEndEvent));
  }
  
  private boolean checkEmptyDocument() {
    if ((!(this.event instanceof DocumentStartEvent)) || (events.isEmpty())) {
      return false;
    }
    Event event = (Event)events.peek();
    if ((event instanceof ScalarEvent)) {
      ScalarEvent e = (ScalarEvent)event;
      return (e.getAnchor() == null) && (e.getTag() == null) && (e.getImplicit() != null) && (e.getValue().length() == 0);
    }
    
    return false;
  }
  
  private boolean checkSimpleKey() {
    int length = 0;
    if (((event instanceof NodeEvent)) && (((NodeEvent)event).getAnchor() != null)) {
      if (preparedAnchor == null) {
        preparedAnchor = prepareAnchor(((NodeEvent)event).getAnchor());
      }
      length += preparedAnchor.length();
    }
    String tag = null;
    if ((event instanceof ScalarEvent)) {
      tag = ((ScalarEvent)event).getTag();
    } else if ((event instanceof CollectionStartEvent)) {
      tag = ((CollectionStartEvent)event).getTag();
    }
    if (tag != null) {
      if (preparedTag == null) {
        preparedTag = prepareTag(tag);
      }
      length += preparedTag.length();
    }
    if ((event instanceof ScalarEvent)) {
      if (analysis == null) {
        analysis = analyzeScalar(((ScalarEvent)event).getValue());
      }
      length += analysis.scalar.length();
    }
    return (length < 128) && (((event instanceof AliasEvent)) || (((event instanceof ScalarEvent)) && (!analysis.empty) && (!analysis.multiline)) || (checkEmptySequence()) || (checkEmptyMapping()));
  }
  


  private void processAnchor(String indicator)
    throws IOException
  {
    NodeEvent ev = (NodeEvent)event;
    if (ev.getAnchor() == null) {
      preparedAnchor = null;
      return;
    }
    if (preparedAnchor == null) {
      preparedAnchor = prepareAnchor(ev.getAnchor());
    }
    writeIndicator(indicator + preparedAnchor, true, false, false);
    preparedAnchor = null;
  }
  
  private void processTag() throws IOException {
    String tag = null;
    if ((event instanceof ScalarEvent)) {
      ScalarEvent ev = (ScalarEvent)event;
      tag = ev.getTag();
      if (style == null) {
        style = chooseScalarStyle();
      }
      if (((!canonical.booleanValue()) || (tag == null)) && (((style == null) && (ev.getImplicit().canOmitTagInPlainScalar())) || ((style != null) && (ev.getImplicit().canOmitTagInNonPlainScalar()))))
      {

        preparedTag = null;
        return;
      }
      if ((ev.getImplicit().canOmitTagInPlainScalar()) && (tag == null)) {
        tag = "!";
        preparedTag = null;
      }
    } else {
      CollectionStartEvent ev = (CollectionStartEvent)event;
      tag = ev.getTag();
      if (((!canonical.booleanValue()) || (tag == null)) && (ev.getImplicit())) {
        preparedTag = null;
        return;
      }
    }
    if (tag == null) {
      throw new EmitterException("tag is not specified");
    }
    if (preparedTag == null) {
      preparedTag = prepareTag(tag);
    }
    writeIndicator(preparedTag, true, false, false);
    preparedTag = null;
  }
  
  private Character chooseScalarStyle() {
    ScalarEvent ev = (ScalarEvent)event;
    if (analysis == null) {
      analysis = analyzeScalar(ev.getValue());
    }
    if (((ev.getStyle() != null) && (ev.getStyle().charValue() == '"')) || (canonical.booleanValue())) {
      return Character.valueOf('"');
    }
    if ((ev.getStyle() == null) && (ev.getImplicit().canOmitTagInPlainScalar()) && 
      ((!simpleKeyContext) || ((!analysis.empty) && (!analysis.multiline))) && (((flowLevel != 0) && (analysis.allowFlowPlain)) || ((flowLevel == 0) && (analysis.allowBlockPlain))))
    {
      return null;
    }
    
    if ((ev.getStyle() != null) && ((ev.getStyle().charValue() == '|') || (ev.getStyle().charValue() == '>')) && 
      (flowLevel == 0) && (!simpleKeyContext) && (analysis.allowBlock)) {
      return ev.getStyle();
    }
    
    if (((ev.getStyle() == null) || (ev.getStyle().charValue() == '\'')) && 
      (analysis.allowSingleQuoted) && ((!simpleKeyContext) || (!analysis.multiline))) {
      return Character.valueOf('\'');
    }
    
    return Character.valueOf('"');
  }
  
  private void processScalar() throws IOException {
    ScalarEvent ev = (ScalarEvent)event;
    if (analysis == null) {
      analysis = analyzeScalar(ev.getValue());
    }
    if (style == null) {
      style = chooseScalarStyle();
    }
    boolean split = (!simpleKeyContext) && (splitLines);
    if (style == null) {
      writePlain(analysis.scalar, split);
    } else {
      switch (style.charValue()) {
      case '"': 
        writeDoubleQuoted(analysis.scalar, split);
        break;
      case '\'': 
        writeSingleQuoted(analysis.scalar, split);
        break;
      case '>': 
        writeFolded(analysis.scalar, split);
        break;
      case '|': 
        writeLiteral(analysis.scalar);
        break;
      default: 
        throw new YAMLException("Unexpected style: " + style);
      }
    }
    analysis = null;
    style = null;
  }
  

  private String prepareVersion(DumperOptions.Version version)
  {
    if (version.major() != 1) {
      throw new EmitterException("unsupported YAML version: " + version);
    }
    return version.getRepresentation();
  }
  
  private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
  
  private String prepareTagHandle(String handle) {
    if (handle.length() == 0)
      throw new EmitterException("tag handle must not be empty");
    if ((handle.charAt(0) != '!') || (handle.charAt(handle.length() - 1) != '!'))
      throw new EmitterException("tag handle must start and end with '!': " + handle);
    if ((!"!".equals(handle)) && (!HANDLE_FORMAT.matcher(handle).matches())) {
      throw new EmitterException("invalid character in the tag handle: " + handle);
    }
    return handle;
  }
  
  private String prepareTagPrefix(String prefix) {
    if (prefix.length() == 0) {
      throw new EmitterException("tag prefix must not be empty");
    }
    StringBuilder chunks = new StringBuilder();
    int start = 0;
    int end = 0;
    if (prefix.charAt(0) == '!') {
      end = 1;
    }
    while (end < prefix.length()) {
      end++;
    }
    if (start < end) {
      chunks.append(prefix.substring(start, end));
    }
    return chunks.toString();
  }
  
  private String prepareTag(String tag) {
    if (tag.length() == 0) {
      throw new EmitterException("tag must not be empty");
    }
    if ("!".equals(tag)) {
      return tag;
    }
    String handle = null;
    String suffix = tag;
    
    for (String prefix : tagPrefixes.keySet()) {
      if ((tag.startsWith(prefix)) && (("!".equals(prefix)) || (prefix.length() < tag.length()))) {
        handle = prefix;
      }
    }
    if (handle != null) {
      suffix = tag.substring(handle.length());
      handle = (String)tagPrefixes.get(handle);
    }
    
    int end = suffix.length();
    String suffixText = end > 0 ? suffix.substring(0, end) : "";
    
    if (handle != null) {
      return handle + suffixText;
    }
    return "!<" + suffixText + ">";
  }
  
  private static final Pattern ANCHOR_FORMAT = Pattern.compile("^[-_\\w]*$");
  
  static String prepareAnchor(String anchor) {
    if (anchor.length() == 0) {
      throw new EmitterException("anchor must not be empty");
    }
    if (!ANCHOR_FORMAT.matcher(anchor).matches()) {
      throw new EmitterException("invalid character in the anchor: " + anchor);
    }
    return anchor;
  }
  
  private ScalarAnalysis analyzeScalar(String scalar)
  {
    if (scalar.length() == 0) {
      return new ScalarAnalysis(scalar, true, false, false, true, true, false);
    }
    
    boolean blockIndicators = false;
    boolean flowIndicators = false;
    boolean lineBreaks = false;
    boolean specialCharacters = false;
    

    boolean leadingSpace = false;
    boolean leadingBreak = false;
    boolean trailingSpace = false;
    boolean trailingBreak = false;
    boolean breakSpace = false;
    boolean spaceBreak = false;
    

    if ((scalar.startsWith("---")) || (scalar.startsWith("..."))) {
      blockIndicators = true;
      flowIndicators = true;
    }
    
    boolean preceededByWhitespace = true;
    boolean followedByWhitespace = (scalar.length() == 1) || (Constant.NULL_BL_T_LINEBR.has(scalar.charAt(1)));
    
    boolean previousSpace = false;
    

    boolean previousBreak = false;
    
    int index = 0;
    
    while (index < scalar.length()) {
      char ch = scalar.charAt(index);
      
      if (index == 0)
      {
        if ("#,[]{}&*!|>'\"%@`".indexOf(ch) != -1) {
          flowIndicators = true;
          blockIndicators = true;
        }
        if ((ch == '?') || (ch == ':')) {
          flowIndicators = true;
          if (followedByWhitespace) {
            blockIndicators = true;
          }
        }
        if ((ch == '-') && (followedByWhitespace)) {
          flowIndicators = true;
          blockIndicators = true;
        }
      }
      else {
        if (",?[]{}".indexOf(ch) != -1) {
          flowIndicators = true;
        }
        if (ch == ':') {
          flowIndicators = true;
          if (followedByWhitespace) {
            blockIndicators = true;
          }
        }
        if ((ch == '#') && (preceededByWhitespace)) {
          flowIndicators = true;
          blockIndicators = true;
        }
      }
      
      boolean isLineBreak = Constant.LINEBR.has(ch);
      if (isLineBreak) {
        lineBreaks = true;
      }
      if ((ch != '\n') && ((' ' > ch) || (ch > '~'))) {
        if (((ch == '') || ((' ' <= ch) && (ch <= 55295)) || ((57344 <= ch) && (ch <= 65533))) && (ch != 65279))
        {

          if (!allowUnicode) {
            specialCharacters = true;
          }
        } else {
          specialCharacters = true;
        }
      }
      
      if (ch == ' ') {
        if (index == 0) {
          leadingSpace = true;
        }
        if (index == scalar.length() - 1) {
          trailingSpace = true;
        }
        if (previousBreak) {
          breakSpace = true;
        }
        previousSpace = true;
        previousBreak = false;
      } else if (isLineBreak) {
        if (index == 0) {
          leadingBreak = true;
        }
        if (index == scalar.length() - 1) {
          trailingBreak = true;
        }
        if (previousSpace) {
          spaceBreak = true;
        }
        previousSpace = false;
        previousBreak = true;
      } else {
        previousSpace = false;
        previousBreak = false;
      }
      

      index++;
      preceededByWhitespace = (Constant.NULL_BL_T.has(ch)) || (isLineBreak);
      followedByWhitespace = (index + 1 >= scalar.length()) || (Constant.NULL_BL_T.has(scalar.charAt(index + 1))) || (isLineBreak);
    }
    

    boolean allowFlowPlain = true;
    boolean allowBlockPlain = true;
    boolean allowSingleQuoted = true;
    boolean allowBlock = true;
    
    if ((leadingSpace) || (leadingBreak) || (trailingSpace) || (trailingBreak)) {
      allowFlowPlain = allowBlockPlain = 0;
    }
    
    if (trailingSpace) {
      allowBlock = false;
    }
    

    if (breakSpace) {
      allowFlowPlain = allowBlockPlain = allowSingleQuoted = 0;
    }
    

    if ((spaceBreak) || (specialCharacters)) {
      allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = 0;
    }
    

    if (lineBreaks) {
      allowFlowPlain = false;
    }
    
    if (flowIndicators) {
      allowFlowPlain = false;
    }
    
    if (blockIndicators) {
      allowBlockPlain = false;
    }
    
    return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
  }
  

  void flushStream()
    throws IOException
  {
    stream.flush();
  }
  


  void writeStreamEnd()
    throws IOException
  {
    flushStream();
  }
  
  void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException
  {
    if ((!this.whitespace) && (needWhitespace)) {
      column += 1;
      stream.write(SPACE);
    }
    this.whitespace = whitespace;
    indention = ((indention) && (indentation));
    column += indicator.length();
    openEnded = false;
    stream.write(indicator);
  }
  
  void writeIndent() throws IOException { int indent;
    int indent;
    if (this.indent != null) {
      indent = this.indent.intValue();
    } else {
      indent = 0;
    }
    
    if ((!indention) || (column > indent) || ((column == indent) && (!whitespace))) {
      writeLineBreak(null);
    }
    
    if (column < indent) {
      whitespace = true;
      char[] data = new char[indent - column];
      for (int i = 0; i < data.length; i++) {
        data[i] = ' ';
      }
      column = indent;
      stream.write(data);
    }
  }
  
  private void writeLineBreak(String data) throws IOException {
    whitespace = true;
    indention = true;
    column = 0;
    if (data == null) {
      stream.write(bestLineBreak);
    } else {
      stream.write(data);
    }
  }
  
  void writeVersionDirective(String versionText) throws IOException {
    stream.write("%YAML ");
    stream.write(versionText);
    writeLineBreak(null);
  }
  
  void writeTagDirective(String handleText, String prefixText)
    throws IOException
  {
    stream.write("%TAG ");
    stream.write(handleText);
    stream.write(SPACE);
    stream.write(prefixText);
    writeLineBreak(null);
  }
  
  private void writeSingleQuoted(String text, boolean split) throws IOException
  {
    writeIndicator("'", true, false, false);
    boolean spaces = false;
    boolean breaks = false;
    int start = 0;int end = 0;
    
    while (end <= text.length()) {
      char ch = '\000';
      if (end < text.length()) {
        ch = text.charAt(end);
      }
      if (spaces) {
        if ((ch == 0) || (ch != ' ')) {
          if ((start + 1 == end) && (column > bestWidth) && (split) && (start != 0) && (end != text.length()))
          {
            writeIndent();
          } else {
            int len = end - start;
            column += len;
            stream.write(text, start, len);
          }
          start = end;
        }
      } else if (breaks) {
        if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
          if (text.charAt(start) == '\n') {
            writeLineBreak(null);
          }
          String data = text.substring(start, end);
          for (char br : data.toCharArray()) {
            if (br == '\n') {
              writeLineBreak(null);
            } else {
              writeLineBreak(String.valueOf(br));
            }
          }
          writeIndent();
          start = end;
        }
      }
      else if ((Constant.LINEBR.has(ch, "\000 '")) && 
        (start < end)) {
        int len = end - start;
        column += len;
        stream.write(text, start, len);
        start = end;
      }
      

      if (ch == '\'') {
        column += 2;
        stream.write("''");
        start = end + 1;
      }
      if (ch != 0) {
        spaces = ch == ' ';
        breaks = Constant.LINEBR.has(ch);
      }
      end++;
    }
    writeIndicator("'", false, false, false);
  }
  
  private void writeDoubleQuoted(String text, boolean split) throws IOException {
    writeIndicator("\"", true, false, false);
    int start = 0;
    int end = 0;
    while (end <= text.length()) {
      Character ch = null;
      if (end < text.length()) {
        ch = Character.valueOf(text.charAt(end));
      }
      if ((ch == null) || ("\"\\  ﻿".indexOf(ch.charValue()) != -1) || (' ' > ch.charValue()) || (ch.charValue() > '~'))
      {
        if (start < end) {
          int len = end - start;
          column += len;
          stream.write(text, start, len);
          start = end;
        }
        if (ch != null) { String data;
          String data;
          if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
            data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch); } else { String data;
            if ((!allowUnicode) || (!StreamReader.isPrintable(ch.charValue())))
            {
              String data;
              if (ch.charValue() <= 'ÿ') {
                String s = "0" + Integer.toString(ch.charValue(), 16);
                data = "\\x" + s.substring(s.length() - 2); } else { String data;
                if ((ch.charValue() >= 55296) && (ch.charValue() <= 56319)) { String data;
                  if (end + 1 < text.length()) {
                    Character ch2 = Character.valueOf(text.charAt(++end));
                    String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
                    data = "\\U" + s.substring(s.length() - 8);
                  } else {
                    String s = "000" + Integer.toString(ch.charValue(), 16);
                    data = "\\u" + s.substring(s.length() - 4);
                  }
                } else {
                  String s = "000" + Integer.toString(ch.charValue(), 16);
                  data = "\\u" + s.substring(s.length() - 4);
                }
              }
            } else { data = String.valueOf(ch);
            } }
          column += data.length();
          stream.write(data);
          start = end + 1;
        }
      }
      if ((0 < end) && (end < text.length() - 1) && ((ch.charValue() == ' ') || (start >= end)) && (column + (end - start) > bestWidth) && (split)) {
        String data;
        String data;
        if (start >= end) {
          data = "\\";
        } else {
          data = text.substring(start, end) + "\\";
        }
        if (start < end) {
          start = end;
        }
        column += data.length();
        stream.write(data);
        writeIndent();
        whitespace = false;
        indention = false;
        if (text.charAt(start) == ' ') {
          data = "\\";
          column += data.length();
          stream.write(data);
        }
      }
      end++;
    }
    writeIndicator("\"", false, false, false);
  }
  
  private String determineBlockHints(String text) {
    StringBuilder hints = new StringBuilder();
    if (Constant.LINEBR.has(text.charAt(0), " ")) {
      hints.append(bestIndent);
    }
    char ch1 = text.charAt(text.length() - 1);
    if (Constant.LINEBR.hasNo(ch1)) {
      hints.append("-");
    } else if ((text.length() == 1) || (Constant.LINEBR.has(text.charAt(text.length() - 2)))) {
      hints.append("+");
    }
    return hints.toString();
  }
  
  void writeFolded(String text, boolean split) throws IOException {
    String hints = determineBlockHints(text);
    writeIndicator(">" + hints, true, false, false);
    if ((hints.length() > 0) && (hints.charAt(hints.length() - 1) == '+')) {
      openEnded = true;
    }
    writeLineBreak(null);
    boolean leadingSpace = true;
    boolean spaces = false;
    boolean breaks = true;
    int start = 0;int end = 0;
    while (end <= text.length()) {
      char ch = '\000';
      if (end < text.length()) {
        ch = text.charAt(end);
      }
      if (breaks) {
        if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
          if ((!leadingSpace) && (ch != 0) && (ch != ' ') && (text.charAt(start) == '\n')) {
            writeLineBreak(null);
          }
          leadingSpace = ch == ' ';
          String data = text.substring(start, end);
          for (char br : data.toCharArray()) {
            if (br == '\n') {
              writeLineBreak(null);
            } else {
              writeLineBreak(String.valueOf(br));
            }
          }
          if (ch != 0) {
            writeIndent();
          }
          start = end;
        }
      } else if (spaces) {
        if (ch != ' ') {
          if ((start + 1 == end) && (column > bestWidth) && (split)) {
            writeIndent();
          } else {
            int len = end - start;
            column += len;
            stream.write(text, start, len);
          }
          start = end;
        }
      }
      else if (Constant.LINEBR.has(ch, "\000 ")) {
        int len = end - start;
        column += len;
        stream.write(text, start, len);
        if (ch == 0) {
          writeLineBreak(null);
        }
        start = end;
      }
      
      if (ch != 0) {
        breaks = Constant.LINEBR.has(ch);
        spaces = ch == ' ';
      }
      end++;
    }
  }
  
  void writeLiteral(String text) throws IOException {
    String hints = determineBlockHints(text);
    writeIndicator("|" + hints, true, false, false);
    if ((hints.length() > 0) && (hints.charAt(hints.length() - 1) == '+')) {
      openEnded = true;
    }
    writeLineBreak(null);
    boolean breaks = true;
    int start = 0;int end = 0;
    while (end <= text.length()) {
      char ch = '\000';
      if (end < text.length()) {
        ch = text.charAt(end);
      }
      if (breaks) {
        if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
          String data = text.substring(start, end);
          for (char br : data.toCharArray()) {
            if (br == '\n') {
              writeLineBreak(null);
            } else {
              writeLineBreak(String.valueOf(br));
            }
          }
          if (ch != 0) {
            writeIndent();
          }
          start = end;
        }
      }
      else if ((ch == 0) || (Constant.LINEBR.has(ch))) {
        stream.write(text, start, end - start);
        if (ch == 0) {
          writeLineBreak(null);
        }
        start = end;
      }
      
      if (ch != 0) {
        breaks = Constant.LINEBR.has(ch);
      }
      end++;
    }
  }
  
  void writePlain(String text, boolean split) throws IOException {
    if (rootContext) {
      openEnded = true;
    }
    if (text.length() == 0) {
      return;
    }
    if (!whitespace) {
      column += 1;
      stream.write(SPACE);
    }
    whitespace = false;
    indention = false;
    boolean spaces = false;
    boolean breaks = false;
    int start = 0;int end = 0;
    while (end <= text.length()) {
      char ch = '\000';
      if (end < text.length()) {
        ch = text.charAt(end);
      }
      if (spaces) {
        if (ch != ' ') {
          if ((start + 1 == end) && (column > bestWidth) && (split)) {
            writeIndent();
            whitespace = false;
            indention = false;
          } else {
            int len = end - start;
            column += len;
            stream.write(text, start, len);
          }
          start = end;
        }
      } else if (breaks) {
        if (Constant.LINEBR.hasNo(ch)) {
          if (text.charAt(start) == '\n') {
            writeLineBreak(null);
          }
          String data = text.substring(start, end);
          for (char br : data.toCharArray()) {
            if (br == '\n') {
              writeLineBreak(null);
            } else {
              writeLineBreak(String.valueOf(br));
            }
          }
          writeIndent();
          whitespace = false;
          indention = false;
          start = end;
        }
      }
      else if ((ch == 0) || (Constant.LINEBR.has(ch))) {
        int len = end - start;
        column += len;
        stream.write(text, start, len);
        start = end;
      }
      
      if (ch != 0) {
        spaces = ch == ' ';
        breaks = Constant.LINEBR.has(ch);
      }
      end++;
    }
  }
  
  void writeStreamStart() {}
}
