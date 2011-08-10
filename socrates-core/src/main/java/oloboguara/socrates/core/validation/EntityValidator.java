/*
 *
 */
package oloboguara.socrates.core.validation;

import static java.lang.String.format;

import java.lang.reflect.Field;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import oloboguara.socrates.core.exception.EntityValidationException;
import oloboguara.socrates.core.validation.rules.ValidationRules;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Enum to validates entities.
 * 
 * @author Diego Martins
 * @version 1.0
 * @date 17/09/2010 16:06:31
 * 
 */
public enum EntityValidator {

	/**
	 * This rule validates if ALL fields are not null.
	 */
	NOTHING_NULL {

		/*
		 * (non-Javadoc)
		 * 
		 * @see oloboguara.socrates.core.validation.EntityValidator#validate(java.lang.Object)
		 */
		@Override
		public EntityValidator validate(final Object entity) throws EntityValidationException {

			final String entitySimpleName = firstValidation(entity);

			for (final Field field : entity.getClass().getDeclaredFields()) {
				final String fieldName = field.getName();
				field.setAccessible(true);
				try {
					if (field.get(entity) == null) {
						throw new IllegalArgumentException(format("The field [%s] of the object [%s] can't be null!", fieldName,
								entitySimpleName));
					}
				} catch (final IllegalAccessException e) {
					final String message = format("Error while accessing the field [%] of the entity [%]: %s", fieldName, entitySimpleName);
					LOG.error(message, e);
					throw new EntityValidationException(message, e);
				}
			}

			return this;
		}
	},
	/**
	 * This rules uses the validation passed as annotation parameter.
	 */
	FIELD_VALIDATION_WITH_ANNOTATION {

		/*
		 * (non-Javadoc)
		 * 
		 * @see oloboguara.socrates.core.validation.EntityValidator#validate(java.lang.Object)
		 */
		@Override
		public EntityValidator validate(final Object entity) throws EntityValidationException {

			final String entitySimpleName = firstValidation(entity);

			for (final Field field : entity.getClass().getDeclaredFields()) {

				field.setAccessible(true);

				final ValidationRules annotation = field.getAnnotation(ValidationRules.class);
				if (annotation != null) {

					final String fieldName = field.getName();
					final String jsEvalExpr = annotation.jsEvaluateExpression();
					if (StringUtils.isNotEmpty(jsEvalExpr)) {

						evaluateFieldWithJSExpression(entity, entitySimpleName, field, fieldName, jsEvalExpr);

					} else {

						LOG.warn(String.format("The validation for the field [%s] of the entity [%s] has been done, "
								+ "but without configuration of validation expression", fieldName, entitySimpleName));
					}
				}

			}
			return this;
		}
	};

	private static final String CONST_NAME_FOR_BINDING = "value";
	private static final String PATTERN_FOR_JS_EVAL_EXPR = "value %s";
	private static final String JS_SCRIPT_ENGINE_NAME = "js";
	private static final Logger LOG = Logger.getLogger(EntityValidator.class);

	/**
	 * This method validates the <code>Entity</code> using the rules above.
	 * 
	 * @param entity
	 * @return
	 */
	public abstract EntityValidator validate(Object entity) throws EntityValidationException;

	/**
	 * Makes the evaluation of a validation maded in javascript language.
	 * 
	 * @param entity
	 * @param entitySimpleName
	 * @param field
	 * @param fieldName
	 * @param jsEvalExpr
	 * @throws EntityValidationException
	 */
	private static void evaluateFieldWithJSExpression(final Object entity, final String entitySimpleName, final Field field,
			final String fieldName, final String jsEvalExpr) throws EntityValidationException {

		final ScriptEngine engine = new ScriptEngineManager().getEngineByName(JS_SCRIPT_ENGINE_NAME);

		final String eval = format(PATTERN_FOR_JS_EVAL_EXPR, jsEvalExpr);
		final Object fieldValueInObject = getFieldValueInObject(entity, field);
		engine.put(CONST_NAME_FOR_BINDING, fieldValueInObject == null ? null : fieldValueInObject.toString());
		try {
			if (!(Boolean) engine.eval(eval)) {
				throw new IllegalArgumentException(format("The field [%s] of the object [%s] is not valid! "
						+ "The field must obey the rule [%s] with the value: [%s]", fieldName, entitySimpleName, eval, fieldValueInObject));
			}
		} catch (final ScriptException e) {
			final String errMsg = format("Error while evaluating the expression [%] (where value = '%s') for the field [%s]"
					+ " of the entity [%]: %s", eval, fieldValueInObject, fieldName, entitySimpleName);
			LOG.error(errMsg, e);
			throw new EntityValidationException(errMsg, e);
		}
	}

	/**
	 * @param entity
	 * @param entitySimpleName
	 * @param field
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws EntityValidationException
	 */
	private static Object getFieldValueInObject(final Object entity, final Field field) throws EntityValidationException {

		try {
			return field.get(entity);
		} catch (final IllegalAccessException e) {
			final String errMsg = format("Error while accessing the field [%s] of the entity [%s]: %s", field.getName(), entity
					.getClass().getSimpleName(), e.getMessage());
			LOG.error(errMsg, e);
			throw new EntityValidationException(errMsg, e);
		}
	}

	/**
	 * @param entity
	 * @return
	 */
	private static String firstValidation(final Object entity) {
		if (entity == null) {
			throw new IllegalArgumentException("The object to be validated is null!");
		}

		return entity.getClass().getSimpleName();
	}

}
