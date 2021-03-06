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
package org.mym.datamocker.rule.annotation;

import org.mym.datamocker.rule.AffectType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Force the result string to fit a specified regex pattern.
 * Created by Muyangmin on Apr 21, 2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@AffectType(types = {String.class})
public @interface MatchRegex {
    String value();
}
