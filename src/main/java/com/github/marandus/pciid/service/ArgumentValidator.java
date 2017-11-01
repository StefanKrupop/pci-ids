/* 
 * Copyright 2017 Thomas Rix.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.marandus.pciid.service;

import java.util.Objects;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to validate method arguments.
 *
 * @author Thomas Rix (thomasrix@exodus-project.net)
 * @since 1.0
 */
public class ArgumentValidator {

    /**
     * Test <tt>arg</tt> to be non-blank as defined by the
     * {@link StringUtils#isBlank(java.lang.CharSequence) StringUtils.isBlank()} method. If
     * <tt>arg</tt> is blank, an IllegalArgumentException will be raised. The exception message will
     * be constructed as follows:
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
     * Test the length <tt>arg</tt> against the provided length using the specified compare
     * operator. If the result of the comparison is false, an IllegalArgumentException will be
     * raised. The exception message will be constructed as follows:
     * <p>
     * <tt>"String length violation (" + name + "): string(" + arg.length() + ") " +
     * comp.getOperator() + " " + len</tt>
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
     * Test <tt>arg</tt> to be non-null as defined by the
     * {@link Objects#isNull(java.lang.Object) Objects.isNull()} method. If <tt>arg</tt> is null, an
     * IllegalArgumentException will be raised. The exception message will be constructed as
     * follows:
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
