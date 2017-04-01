package org.mym.datamocker;

import org.mym.datamocker.rule.ConstraintVerb;
import org.mym.datamocker.rule.Rule;

/**
 * Copyright 2017 Muyangmin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 *
 * Entry class of data mocker library.
 *
 * Created by Muyangmin on 3/24/17.
 */
public class DataMocker {

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
        return this;
    }

    public boolean mockBool() {
        //TODO
        return false;
    }

    public int mockInt() {
        //TODO
        return 0;
    }

    public double mockDouble(){
        //TODO
        return 0D;
    }

    public interface Logger {
        void log(String msg);
    }
}
