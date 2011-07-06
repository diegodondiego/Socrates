/*
 *
 */
package oloboguara.socrates.core.tests.validating;

import oloboguara.socrates.core.exception.EntityValidationException;
import oloboguara.socrates.core.validation.EntityValidator;
import oloboguara.socrates.core.validation.rules.ValidationRules;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for {@link EntityValidator}.
 * 
 * @author Diego Martins
 * @version 1.0
 * @date 17/09/2010 18:35:25
 * 
 */
@RunWith(JUnit4.class)
public class EntityValidatorTest {

	/**
	 * Tests a simple validation.
	 * 
	 * @throws EntityValidationException
	 */
	@Test
	public void testBasicValidation() throws EntityValidationException {

		EntityTest test = new EntityTest("nem valida!", "valida!", 1);

		EntityValidator.NOTHING_NULL.validate(test);

		EntityValidator.FIELD_VALIDATION_WITH_ANNOTATION.validate(test);
	}

	/**
	 * Tests a simple null field validation.
	 * 
	 * @throws EntityValidationException
	 */
	@Test
	public void testNullProblemValidation() throws EntityValidationException {

		EntityTest test = new EntityTest("nem valida!", null, 1);

		try {
			EntityValidator.NOTHING_NULL.validate(test);
			Assert.fail("validates object not nullable with null fields: " + test.toString());
		} catch (IllegalArgumentException e) {
			System.out.println("error: " + e.getMessage());
		}
	}

	/**
	 * Tests a simple validation js expression (!= null).
	 * 
	 * @throws EntityValidationException
	 */
	@Test
	public void testAnnotationWithNullValidation() throws EntityValidationException {

		EntityTest test = new EntityTest("nem valida!", null, 1);

		try {
			EntityValidator.FIELD_VALIDATION_WITH_ANNOTATION.validate(test);
			Assert.fail("validates object not nullable with null fields: " + test.toString());
		} catch (IllegalArgumentException e) {
			System.out.println("error: " + e.getMessage());
		}
	}

	/**
	 * Tests a simple validation js expression for a int.
	 * 
	 * @throws EntityValidationException
	 */
	@Test
	public void testAnnotationWithWrongValidationForIntValue() throws EntityValidationException {

		EntityTest test = new EntityTest("nem valida!", "validado", 5);

		try {
			EntityValidator.FIELD_VALIDATION_WITH_ANNOTATION.validate(test);
			Assert.fail("validates object not nullable with null fields: " + test.toString());
		} catch (IllegalArgumentException e) {
			System.out.println("error: " + e.getMessage());
		}
	}

	/**
	 * Tests a simple validation js expression for a class with a nullable
	 * field.
	 * 
	 * @throws EntityValidationException
	 */
	@Test
	public void testAnnotationWithNullableField() throws EntityValidationException {

		EntityTest test = new EntityTest(null, "validado", 4);

		try {
			EntityValidator.FIELD_VALIDATION_WITH_ANNOTATION.validate(test);
		} catch (IllegalArgumentException e) {
			Assert.fail("validates object with nullable field as a nullable field: " + test.toString());
			System.out.println("error: " + e.getMessage());
		}
	}

	/**
	 * Used for the tests!
	 * 
	 * @author Diego Martins
	 * @version 1.0
	 * @date 17/09/2010 18:38:41
	 * 
	 */
	private class EntityTest {

		public String strFieldNotValidated;

		@ValidationRules(jsEvaluateExpression = "!= null")
		public String strFieldValidated;

		@ValidationRules(jsEvaluateExpression = "* 5 <= 20")
		public int intValidated;

		/**
		 * @param strFieldNotValidated
		 * @param strFieldValidated
		 */
		private EntityTest(String strFieldNotValidated, String strFieldValidated, int intValidated) {
			this.strFieldNotValidated = strFieldNotValidated;
			this.strFieldValidated = strFieldValidated;
			this.intValidated = intValidated;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "EntityTest [intValidated=" + intValidated + ", strFieldNotValidated=" + strFieldNotValidated
					+ ", strFieldValidated=" + strFieldValidated + "]";
		}

	}

}
