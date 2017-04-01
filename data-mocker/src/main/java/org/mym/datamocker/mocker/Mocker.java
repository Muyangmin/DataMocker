package org.mym.datamocker.mocker;

import org.mym.datamocker.rule.Rule;

import java.util.List;

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
 * TODO what is this class?
 * <p>
 * Created by Muyangmin on 3/27/17.
 */
public interface Mocker<T> {

    void applyRules(List<Rule> ruleList);

    T mock();
}
