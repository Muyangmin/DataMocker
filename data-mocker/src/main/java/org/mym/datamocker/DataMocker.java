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
package org.mym.datamocker;

import org.mym.datamocker.mocker.BooleanMocker;
import org.mym.datamocker.mocker.DoubleMocker;
import org.mym.datamocker.mocker.IMocker;
import org.mym.datamocker.mocker.IntMocker;
import org.mym.datamocker.mocker.ObjectMocker;
import org.mym.datamocker.mocker.StringMocker;
import org.mym.datamocker.rule.ConstraintVerb;
import org.mym.datamocker.rule.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Entry class of data mocker library.
 * <p>
 * Created by Muyangmin on 3/24/17.
 */
@SuppressWarnings("WeakerAccess")
public class DataMocker {

    private BooleanMocker mBoolMocker = new BooleanMocker();
    private IntMocker mIntMocker = new IntMocker();
    private DoubleMocker mDoubleMocker = new DoubleMocker();
    private StringMocker mStrMocker = new StringMocker();

    private List<Rule> mRuleList = new ArrayList<>();

    public DataMocker addRule(ConstraintVerb constraintVerb, Object arg) {
        return addRule(new Rule(constraintVerb, arg));
    }

    public DataMocker addRule(Rule rule) {
        mRuleList.add(rule);
        return this;
    }

    public DataMocker addRules(Collection<Rule> ruleList) {
        mRuleList.addAll(ruleList);
        return this;
    }

    public void clearRules() {
        mRuleList.clear();
    }

    public boolean mockBoolean() {
        return invokeMocker(mBoolMocker, Boolean.class);
    }

    public int mockInt() {
        return invokeMocker(mIntMocker, Integer.class);
    }

    public int mockInt(int min, int max) {
        addRule(ConstraintVerb.LESS_THAN, max);
        addRule(ConstraintVerb.MORE_THAN, min);
        return invokeMocker(mIntMocker, Integer.class);
    }

    public double mockDouble() {
        return invokeMocker(mDoubleMocker, Double.class);
    }

    public double mockDouble(double min, double max) {
        addRule(ConstraintVerb.LESS_THAN, max);
        addRule(ConstraintVerb.MORE_THAN, min);
        return invokeMocker(mDoubleMocker, Double.class);
    }

    public String mockStringAlphaNumeric(int length) {
        return mockString(length, StringMocker.CHARSET_ALPHA_NUMERIC);
    }

    public String mockStringNumeric(int length) {
        return mockString(length, StringMocker.CHARSET_NUMERIC);
    }

    public String mockStringAlpha(int length) {
        return mockString(length, StringMocker.CHARSET_LETTERS);
    }

    public String mockString(int length, String charset) {
        return mockString(length, 0, false, false, charset);
    }

    public String mockString(int maxLength, int minLength, boolean fixedLength,
                             boolean nullable, String charset) {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(ConstraintVerb.MAX_LENGTH, maxLength));
        rules.add(new Rule(ConstraintVerb.MIN_LENGTH, minLength));
        rules.add(new Rule(ConstraintVerb.FIXED_LENGTH, fixedLength));
        rules.add(new Rule(ConstraintVerb.NULLABLE, nullable));
        if (charset != null) {
            if (charset.isEmpty()) {
                throw new IllegalArgumentException("Cannot generate String from empty charset.");
            }
            addRule(new Rule(ConstraintVerb.CHAR_ENUM, charset));
        }
        return mockString(rules);
    }

    public String mockString(Rule... rules) {
        return mockString(Arrays.asList(rules));
    }

    public String mockString(List<Rule> ruleList) {
        addRules(ruleList);
        return invokeMocker(mStrMocker, String.class);
    }

    public <Obj> Obj mockObject(Class<Obj> clazz) {
        return new ObjectMocker<Obj>().mock(clazz);
    }

    private <T> T invokeMocker(IMocker<T> mocker, Object... args) {
        //Clear rules applied to mocker before
        mocker.clearRules();
        mocker.applyRules(mRuleList);

        T result = mocker.mock(args);

        //Clear saved rules
        clearRules();

        return result;
    }
}
