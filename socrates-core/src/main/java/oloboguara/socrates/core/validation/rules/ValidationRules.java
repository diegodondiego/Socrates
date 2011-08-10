/*
 *
 */
package oloboguara.socrates.core.validation.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This contain a bunch of rules for validate a entity field. You can create a rule for validation using javascript language (easier e
 * fast). <b>Valuation expressions using other languages is going to be implemented after.</b>
 * 
 * Ex.:
 * 
 * You create a class and use this annotation on a field of that class passing this {@link String} as
 * {@link ValidationRules#jsEvaluateExpression()}: " != null". This string will be evaluated with the value of a field.
 * 
 * <b>REMEMBER</b>:
 * 
 * This evaluation expressions shoud return boolean!
 * 
 * @author Diego Martins
 * @version 1.0
 * @date 17/09/2010 16:49:55
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidationRules {

	String jsEvaluateExpression() default "";

}
