package org.junit.internal.runners.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.TestClass;

















public class RuleMemberValidator
{
  public static final RuleMemberValidator CLASS_RULE_VALIDATOR = classRuleValidatorBuilder().withValidator(new DeclaringClassMustBePublic(null)).withValidator(new MemberMustBeStatic(null)).withValidator(new MemberMustBePublic(null)).withValidator(new FieldMustBeATestRule(null)).build();
  








  public static final RuleMemberValidator RULE_VALIDATOR = testRuleValidatorBuilder().withValidator(new MemberMustBeNonStaticOrAlsoClassRule(null)).withValidator(new MemberMustBePublic(null)).withValidator(new FieldMustBeARule(null)).build();
  







  public static final RuleMemberValidator CLASS_RULE_METHOD_VALIDATOR = classRuleValidatorBuilder().forMethods().withValidator(new DeclaringClassMustBePublic(null)).withValidator(new MemberMustBeStatic(null)).withValidator(new MemberMustBePublic(null)).withValidator(new MethodMustBeATestRule(null)).build();
  










  public static final RuleMemberValidator RULE_METHOD_VALIDATOR = testRuleValidatorBuilder().forMethods().withValidator(new MemberMustBeNonStaticOrAlsoClassRule(null)).withValidator(new MemberMustBePublic(null)).withValidator(new MethodMustBeARule(null)).build();
  

  private final Class<? extends Annotation> annotation;
  

  private final boolean methods;
  
  private final List<RuleValidator> validatorStrategies;
  

  RuleMemberValidator(Builder builder)
  {
    annotation = annotation;
    methods = methods;
    validatorStrategies = validators;
  }
  






  public void validate(TestClass target, List<Throwable> errors)
  {
    List<? extends FrameworkMember<?>> members = methods ? target.getAnnotatedMethods(annotation) : target.getAnnotatedFields(annotation);
    

    for (FrameworkMember<?> each : members) {
      validateMember(each, errors);
    }
  }
  
  private void validateMember(FrameworkMember<?> member, List<Throwable> errors) {
    for (RuleValidator strategy : validatorStrategies) {
      strategy.validate(member, annotation, errors);
    }
  }
  
  private static Builder classRuleValidatorBuilder() {
    return new Builder(ClassRule.class, null);
  }
  
  private static Builder testRuleValidatorBuilder() {
    return new Builder(Rule.class, null);
  }
  
  private static class Builder {
    private final Class<? extends Annotation> annotation;
    private boolean methods;
    private final List<RuleMemberValidator.RuleValidator> validators;
    
    private Builder(Class<? extends Annotation> annotation) {
      this.annotation = annotation;
      methods = false;
      validators = new ArrayList();
    }
    
    Builder forMethods() {
      methods = true;
      return this;
    }
    
    Builder withValidator(RuleMemberValidator.RuleValidator validator) {
      validators.add(validator);
      return this;
    }
    
    RuleMemberValidator build() {
      return new RuleMemberValidator(this);
    }
  }
  
  private static boolean isRuleType(FrameworkMember<?> member) {
    return (isMethodRule(member)) || (isTestRule(member));
  }
  
  private static boolean isTestRule(FrameworkMember<?> member) {
    return TestRule.class.isAssignableFrom(member.getType());
  }
  
  private static boolean isMethodRule(FrameworkMember<?> member) {
    return MethodRule.class.isAssignableFrom(member.getType());
  }
  



  static abstract interface RuleValidator
  {
    public abstract void validate(FrameworkMember<?> paramFrameworkMember, Class<? extends Annotation> paramClass, List<Throwable> paramList);
  }
  


  private static final class MemberMustBeNonStaticOrAlsoClassRule
    implements RuleMemberValidator.RuleValidator
  {
    private MemberMustBeNonStaticOrAlsoClassRule() {}
    


    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      boolean isMethodRuleMember = RuleMemberValidator.isMethodRule(member);
      boolean isClassRuleAnnotated = member.getAnnotation(ClassRule.class) != null;
      





      if ((member.isStatic()) && ((isMethodRuleMember) || (!isClassRuleAnnotated))) { String message;
        String message;
        if (RuleMemberValidator.isMethodRule(member)) {
          message = "must not be static.";
        } else {
          message = "must not be static or it must be annotated with @ClassRule.";
        }
        errors.add(new ValidationError(member, annotation, message));
      }
    }
  }
  
  private static final class MemberMustBeStatic implements RuleMemberValidator.RuleValidator
  {
    private MemberMustBeStatic() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
      if (!member.isStatic()) {
        errors.add(new ValidationError(member, annotation, "must be static."));
      }
    }
  }
  
  private static final class DeclaringClassMustBePublic implements RuleMemberValidator.RuleValidator
  {
    private DeclaringClassMustBePublic() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      if (!isDeclaringClassPublic(member)) {
        errors.add(new ValidationError(member, annotation, "must be declared in a public class."));
      }
    }
    
    private boolean isDeclaringClassPublic(FrameworkMember<?> member)
    {
      return Modifier.isPublic(member.getDeclaringClass().getModifiers());
    }
  }
  
  private static final class MemberMustBePublic implements RuleMemberValidator.RuleValidator
  {
    private MemberMustBePublic() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
      if (!member.isPublic()) {
        errors.add(new ValidationError(member, annotation, "must be public."));
      }
    }
  }
  
  private static final class FieldMustBeARule implements RuleMemberValidator.RuleValidator
  {
    private FieldMustBeARule() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      if (!RuleMemberValidator.isRuleType(member)) {
        errors.add(new ValidationError(member, annotation, "must implement MethodRule or TestRule."));
      }
    }
  }
  
  private static final class MethodMustBeARule
    implements RuleMemberValidator.RuleValidator
  {
    private MethodMustBeARule() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      if (!RuleMemberValidator.isRuleType(member)) {
        errors.add(new ValidationError(member, annotation, "must return an implementation of MethodRule or TestRule."));
      }
    }
  }
  
  private static final class MethodMustBeATestRule
    implements RuleMemberValidator.RuleValidator
  {
    private MethodMustBeATestRule() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      if (!RuleMemberValidator.isTestRule(member)) {
        errors.add(new ValidationError(member, annotation, "must return an implementation of TestRule."));
      }
    }
  }
  

  private static final class FieldMustBeATestRule
    implements RuleMemberValidator.RuleValidator
  {
    private FieldMustBeATestRule() {}
    
    public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors)
    {
      if (!RuleMemberValidator.isTestRule(member)) {
        errors.add(new ValidationError(member, annotation, "must implement TestRule."));
      }
    }
  }
}
