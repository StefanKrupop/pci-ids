/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.exodusproject.pciid.service;

import java.util.Objects;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 */
public class ArgumentValidator {

	/**
	 * Test @c arg to be non-blank as defined by the
	 * {@link StringUtils#isBlank(java.lang.CharSequence) StringUtils.isBlank()} method. If @c arg is blank, an
	 * IllegalArgumentException will be raised. The exception message will be constructed as follows:
	 * <p>
	 * <tt>msg + ": " + arg</tt>
	 *
	 * @param arg Argument to test
	 * @param msg Message used to construct the exception message
	 *
	 * @throws IllegalArgumentException if specified argument is blank
	 */
	public static void requireNonBlank(String arg, String msg) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException(msg + ": " + arg);
		}
	}

	/**
	 * Test the length @c arg against the provided length using the specified compare operator. If the result of the
	 * comparison is false, an IllegalArgumentException will be raised. The exception message will be constructed as
	 * follows:
	 * <p>
	 * <tt>"String length violation (" + name + "): string(" + arg.length() + ") " + comp.getOperator() + " " + len</tt>
	 *
	 * @param arg String to test
	 * @param len Value to be used as reference in comparison
	 * @param comp Comparison operator to be used
	 * @param name Name to be referenced in exception message
	 */
	public static void requireStringLength(String arg, int len, NumberCompare comp, String name) {
		ArgumentValidator.requireNonNull(arg, "String to test");

		final boolean result;

		switch (comp) {
			case EQUAL:
				result = (arg.length() == len);
				break;
			case GREATER:
				result = (arg.length() > len);
				break;
			case GREATER_EQUAL:
				result = (arg.length() >= len);
				break;
			case LESS:
				result = (arg.length() < len);
				break;
			case LESS_EQUAL:
				result = (arg.length() <= len);
				break;
			default:
				throw new IllegalArgumentException("Unknown value of NumberCompare: " + comp);
		}

		if (!result) {
			String msg = "String length violation (" + name + "): string(" + arg.length() + ") " + comp.getOperator() + " " + len;
			throw new IllegalArgumentException(msg);
		}
	}

	/**
	 * Test @c arg to be non-null as defined by the {@link Objects#isNull(java.lang.Object) Objects.isNull()} method. If
	 * @c arg is null, an IllegalArgumentException will be raised. The exception message will be constructed as follows:
	 * <p>
	 * <tt>"NULL: " + msg</tt>
	 *
	 * @param arg Argument to test
	 * @param msg Message used to construct the exception message
	 *
	 * @throws IllegalArgumentException if specified argument is null
	 */
	public static void requireNonNull(Object arg, String msg) {
		if (Objects.isNull(arg)) {
			throw new IllegalArgumentException("NULL: " + msg);
		}
	}

	/**
	 * Private constructor, static only class
	 */
	private ArgumentValidator() {

	}

	public enum NumberCompare {
		EQUAL("=="),
		GREATER(">"),
		GREATER_EQUAL(">="),
		LESS("<"),
		LESS_EQUAL("<=");

		/**
		 * String representation of operator.
		 */
		@Getter
		private final String operator;

		private NumberCompare(String op) {
			this.operator = op;
		}
	}
}
