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

import java.util.Random;

/**
 * Mocker implementation of boolean type.
 * <p>
 * Created by Muyangmin on 4/5/17.
 */
public class IntMocker extends AbsPrimitiveMocker<Integer> {

    private Integer mMin;
    private Integer mMax;

    @Override
    protected boolean applyRule(Rule rule) {
        if (super.applyRule(rule)) {
            return true;
        }
        boolean ruleApplied = false;
        switch (rule.constraint) {
            case LESS_THAN:
                checkRuleTypeSafeOrThrow(Integer.class, rule);
                mMax = ((Integer) rule.args);
                ruleApplied = true;
                break;
            case MORE_THAN:
                checkRuleTypeSafeOrThrow(Integer.class, rule);
                mMin = ((Integer) rule.args);
                ruleApplied = true;
                break;
        }
        return ruleApplied;
    }

    @Override
    protected Integer mockWithRule() {
        //Do not use Math.nextInt(x) directly; that may cause an IllegalArgumentException
        // when param is non-positive.
        if (mMax != null && mMin != null) {
            return getRandomInt(mMin, mMax, mRandom);
        }
        if (mMax != null) {
            return getRandomInt(0, mMax, mRandom);
        }
        if (mMin != null) {
            return getRandomInt(mMin, Integer.MAX_VALUE, mRandom);
        }
        return mRandom.nextInt();
    }

    /*package*/ static int getRandomInt(int min, int max, Random random) {
        int dif = max - min;
        float number = random.nextFloat();              // 0 <= number < 1
        return min + Math.round(number * dif);
    }
}
