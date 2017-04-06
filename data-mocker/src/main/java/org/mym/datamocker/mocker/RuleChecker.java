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
 * Created by Muyangmin on Apr 06, 2017.
 */
/*package*/ final class RuleChecker {
    public static <C1> void checkRuleTypeSafeOrThrow(Class<C1> expected, Rule rule) {
        if (!expected.isAssignableFrom(rule.args.getClass())) {
            throw new IllegalArgumentException("Rule cannot be applied because type mismatch: "
                    + "expected " + expected + ", actual " + rule.args.getClass());
        }
    }
}
