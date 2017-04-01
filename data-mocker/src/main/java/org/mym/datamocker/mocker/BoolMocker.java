package org.mym.datamocker.mocker;

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
 * <p>
 *     Mocker implementation of boolean type.
 * <p>
 * Created by Muyangmin on 3/27/17.
 */
public class BoolMocker extends AbsRandomMocker<Boolean> {

    @Override
    public Boolean mock() {
        if (mExplicitValue != null) {
            return mExplicitValue;
        }
        return mRandom.nextBoolean();
    }

    @Override
    protected boolean applyRule(Rule rule) {
        //Only explicit value needs to be proceed
        return super.handleExplicitValue(rule);
    }
}
