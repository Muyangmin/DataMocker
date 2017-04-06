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
import org.mym.datamocker.rule.ConstraintVerb;
import org.mym.datamocker.rule.Rule;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Base class for mocker implementations, currently depends on java.util.Random class.
 *
 * Created by Muyangmin on 3/27/17.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbsPrimitiveMocker<T> implements IMocker<T> {

    protected Random mRandom = new Random();
    protected T mExplicitValue;

    protected abstract T mockWithRule();

    /**
     * @param rule rule to be applied; not null.
     * @return true if the rule is accepted by mocker implementation; false otherwise.
     */
    protected boolean applyRule(Rule rule) {
        return handleExplicitValue(rule);
    }

    @Override
    public void clearRules() {
        if (mExplicitValue != null) {
            mExplicitValue = null;
        }
    }

    @Override
    public final void applyRules(List<Rule> ruleList) {
        for (Rule rule : ruleList) {
            if (rule == null) {
                continue;
            }
            applyRule(rule);
        }
    }

    @Override
    public final T mock(Object... args) {
        T result;
        if (mExplicitValue != null) {
            result = mExplicitValue;
        } else {
            result = mockWithRule();
        }
        return result;
    }

    /**
     * A handy method for rule {@link ConstraintVerb#EXPLICIT_VALUE}.
     *
     * @param rule not null
     * @return true if the rule is accepted; false otherwise.
     */
    protected final boolean handleExplicitValue(Rule rule) {
        if (rule.constraint == ConstraintVerb.EXPLICIT_VALUE) {
            Objects.requireNonNull(rule.args);
            if (mExplicitValue.getClass().isAssignableFrom(rule.args.getClass())) {
                //noinspection unchecked
                mExplicitValue = ((T) rule.args);
                return true;
            } else {
                throw new IllegalArgumentException("Explicit value must be assignable to field!");
            }
        }
        return false;
    }

    protected final <C1> void checkRuleTypeSafeOrThrow(Class<C1> expected, Rule rule) {
        RuleChecker.checkRuleTypeSafeOrThrow(expected, rule);
    }

}
