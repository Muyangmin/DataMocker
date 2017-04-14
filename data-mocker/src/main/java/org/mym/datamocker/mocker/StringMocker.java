/*
 * Copyright (C) 2017 Muyangmin
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
package org.mym.datamocker.mocker;

import org.mym.datamocker.DataMocker;
import org.mym.datamocker.rule.Rule;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Mocker implementation for Strings.
 * Created by Muyangmin on Apr 06, 2017.
 */
public class StringMocker implements IMocker<String> {

    //------------------- DEFAULT CHARSETS --------------------
    @SuppressWarnings("WeakerAccess")
    public static final String CHARSET_ALPHA_LOWERCASE = "qwertyuiopasdfghjklzxcvbnm";
    @SuppressWarnings("WeakerAccess")
    public static final String CHARSET_ALPHA_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String CHARSET_NUMERIC = "0123456789";
    public static final String CHARSET_LETTERS = CHARSET_ALPHA_LOWERCASE
            + CHARSET_ALPHA_UPPERCASE;
    public static final String CHARSET_ALPHA_NUMERIC = CHARSET_ALPHA_LOWERCASE
            + CHARSET_ALPHA_UPPERCASE + CHARSET_NUMERIC;
    //------------------- DEFAULT CHARSETS --------------------

    private Integer mMaxLength;
    private Integer mMinLength;

    private boolean mNullable;
    private boolean mFixedLength;

    /**
     * Enumeration of available chars.
     * This is very useful to make sure that generated string is formatted as expected.
     * E.g, you can put "0123456789" so that result string must be numbers.
     */
    private String mCharSet = CHARSET_ALPHA_NUMERIC;

    private Random mCharGenerator = new Random();

    @Override
    public void applyRules(List<Rule> rules) {
        for (Rule rule : rules) {
            switch (rule.constraint) {
                case MAX_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Integer.class, rule);
                    int maxLength = ((Integer) rule.args);
                    if (maxLength < 0) {
                        throw new IllegalArgumentException("length must be non-negative!");
                    }
                    mMaxLength = maxLength;
                    break;
                case MIN_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Integer.class, rule);
                    int minLength = ((Integer) rule.args);
                    if (minLength < 0) {
                        throw new IllegalArgumentException("length must be non-negative!");
                    }
                    mMinLength = minLength;
                    break;
                case CHAR_ENUM:
                    RuleChecker.checkRuleTypeSafeOrThrow(String.class, rule);
                    mCharSet = ((String) rule.args);
                    break;
                case NULLABLE:
                    RuleChecker.checkRuleTypeSafeOrThrow(Boolean.class, rule);
                    mNullable = ((Boolean) rule.args);
                    break;
                case FIXED_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Boolean.class, rule);
                    mFixedLength = ((Boolean) rule.args);
                    break;
            }
        }
    }

    @Override
    public void clearRules() {
        mMaxLength = null;
        mMinLength = null;
        mNullable = false;
        mFixedLength = false;
        mCharSet = CHARSET_ALPHA_NUMERIC;
    }

    @Override
    public String mock(Object... args) {
        DataMocker mocker = new DataMocker();
        //Fast Bingo!
        if (mNullable && mocker.mockBoolean()) {
            return null;
        }

        Objects.requireNonNull(mMaxLength);
        Objects.requireNonNull(mCharSet);

        if (mMinLength == null){
            mMinLength = 0;
        }

        //Determine string length
        int length;
        if (mFixedLength) {
            length = mMaxLength;
        } else {
            length = mocker.mockInt(mMinLength, mMaxLength);
        }

        //Generate Char sequences
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int x = mCharGenerator.nextInt(mCharSet.length());
            chars[i] = mCharSet.charAt(x);
        }
        return new String(chars);
    }
}
