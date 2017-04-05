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

import org.mym.datamocker.rule.Rule;

/**
 * Mocker implementation of `double` type.
 *
 * Created by Muyangmin on Apr 05, 2017.
 */
public class DoubleMocker extends AbsRandomMocker<Double> {

    private Double mMin;
    private Double mMax;

    @Override
    protected boolean applyRule(Rule rule) {
        if (super.applyRule(rule)) {
            return true;
        }
        boolean ruleApplied = false;
        switch (rule.constraint) {
            case LESS_THAN:
                checkRuleTypeSafeOrThrow(Double.class, rule);
                mMax = ((Double) rule.args);
                ruleApplied = true;
                break;
            case MORE_THAN:
                checkRuleTypeSafeOrThrow(Double.class, rule);
                mMin = ((Double) rule.args);
                ruleApplied = true;
                break;
        }
        return ruleApplied;
    }

    @Override
    protected Double mockWithRule() {
        if (mMax != null && mMin != null) {
            return mMin + (mMax - mMin) * mRandom.nextDouble();
        }
        if (mMax != null) {
            return mMax * mRandom.nextDouble();
        }
        if (mMin != null) {
            return mMin + mRandom.nextDouble();
        }
        return mRandom.nextDouble();
    }
}
