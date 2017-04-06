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
package org.mym.datamocker.rule;

/**
 * Define constraint rules.
 * Created by Muyangmin on 3/27/17.
 */
public enum ConstraintVerb {
    /**
     * Rule description: the final mock value must be a specified value.
     * If value not pass in(e.g. a null argument), this rule will be ignored.
     */
    @AffectType(types = {Boolean.class, Integer.class, Double.class})
    EXPLICIT_VALUE,

    @AffectType(types = {Integer.class, Double.class})
    LESS_THAN,

    @AffectType(types = {Integer.class, Double.class})
    MORE_THAN,

    @AffectType(types = {String.class})
    MAX_LENGTH,

    @AffectType(types = {String.class})
    MIN_LENGTH,

    @AffectType(types = {String.class})
    CHAR_ENUM,

    @AffectType(types = {String.class})
    NULLABLE,
}
