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

import org.mym.datamocker.mocker.AbsRandomMocker;
import org.mym.datamocker.mocker.BooleanMocker;
import org.mym.datamocker.mocker.DoubleMocker;
import org.mym.datamocker.mocker.IntMocker;
import org.mym.datamocker.rule.ConstraintVerb;
import org.mym.datamocker.rule.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Entry class of data mocker library.
 *
 * Created by Muyangmin on 3/24/17.
 */
@SuppressWarnings("WeakerAccess")
public class DataMocker {

    private BooleanMocker mBoolMocker = new BooleanMocker();
    private IntMocker mIntMocker = new IntMocker();
    private DoubleMocker mDoubleMocker = new DoubleMocker();

    private List<Rule> mRuleList = new ArrayList<>();

    private static Logger sLogger = new Logger() {
        @Override
        public void log(String msg) {
            System.out.println(msg);
        }
    };

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public static void log(String msg){
        if (sLogger != null) {
            sLogger.log(msg);
        }
    }

    public DataMocker addRule(String field, ConstraintVerb constraintVerb, Object arg) {
        return addRule(new Rule(field, null, constraintVerb, arg));
    }

    public DataMocker addRule(Class clz, ConstraintVerb constraintVerb, Object arg) {
        return addRule(new Rule(null, clz, constraintVerb, arg));
    }

    public DataMocker addRule(Rule rule) {
        mRuleList.add(rule);
        return this;
    }

    public boolean mockBoolean() {
        return invokeMocker(mBoolMocker);
    }

    public int mockInt() {
        return invokeMocker(mIntMocker);
    }

    public int mockInt(int min, int max){
        addRule(((String) null), ConstraintVerb.LESS_THAN, max);
        addRule(((String) null), ConstraintVerb.MORE_THAN, min);
        return invokeMocker(mIntMocker);
    }

    public double mockDouble() {
        return invokeMocker(mDoubleMocker);
    }

    public double mockDouble(double min, double max) {
        addRule(((String) null), ConstraintVerb.LESS_THAN, max);
        addRule(((String) null), ConstraintVerb.MORE_THAN, min);
        return invokeMocker(mDoubleMocker);
    }

    private <T> T invokeMocker(AbsRandomMocker<T> mocker) {
        //Clear rules applied to mocker before
        mocker.clearRules();
        mocker.applyRules(mRuleList);
        T result = mocker.mock();

        //Clear saved rules
        mRuleList.clear();

        return result;
    }

    public interface Logger {
        void log(String msg);
    }
}
