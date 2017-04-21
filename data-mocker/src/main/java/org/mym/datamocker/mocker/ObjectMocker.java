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
import org.mym.datamocker.rule.AffectType;
import org.mym.datamocker.rule.ConstraintVerb;
import org.mym.datamocker.rule.Rule;
import org.mym.datamocker.rule.annotation.CharSet;
import org.mym.datamocker.rule.annotation.FixedLength;
import org.mym.datamocker.rule.annotation.LessThan;
import org.mym.datamocker.rule.annotation.MatchRegex;
import org.mym.datamocker.rule.annotation.MaxLength;
import org.mym.datamocker.rule.annotation.MinLength;
import org.mym.datamocker.rule.annotation.MoreThan;
import org.mym.datamocker.rule.annotation.RecursiveMock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Mocker implementation for POJO.
 * Created by Muyangmin on Apr 05, 2017.
 */
public class ObjectMocker<T> implements IMocker<T> {

    private Boolean mIsRecursive;

    private Class<? extends Annotation> mAndroidSupportNullableAnnotation;

    /**
     * Used to generate concrete value of fields.
     */
    private DataMocker mDataMocker = new DataMocker();

    public ObjectMocker() {
        try {
            Class clazz = Class.forName("android.support.annotation.Nullable");
            if (clazz.isAnnotation()) {
                //noinspection unchecked
                mAndroidSupportNullableAnnotation = clazz;
            }
        } catch (ClassNotFoundException ignored) {
            //This exception means that app runs on non-android platform, ignore it
        }
    }

    @Override
    public void applyRules(List<Rule> rules) {
        for (Rule rule : rules) {
            switch (rule.constraint) {
                case RECURSIVE:
                    RuleChecker.checkRuleTypeSafeOrThrow(Boolean.class, rule);
                    mIsRecursive = ((Boolean) rule.args);
                    break;
            }
        }
    }

    @Override
    public void clearRules() {
        mIsRecursive = null;
    }

    @Override
    public T mock(Object... args) {
        //noinspection unchecked
        Class<T> clazz = ((Class<T>) args[0]);

        try {
            //Get Constructor
            Objects.requireNonNull(clazz);
            //Flag still not set explicitly
            if (mIsRecursive == null) {
                Annotation annotation = clazz.getAnnotation(RecursiveMock.class);
                if (annotation != null) {
                    mIsRecursive = true;
                }
            }

            Constructor<T> constructor = clazz.getConstructor();
            if (constructor == null) {
                throw new IllegalArgumentException("To use data mocker, your pojo class must have" +
                        " an empty constructor.");
            }

            //Create instance
            T instance = constructor.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            //Traverse fields and assign values
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();

                //Void type is ignored; it has no actual meaning
                if (fieldType.equals(Void.class) || fieldType.equals(void.class)) {
                    continue;
                }

                //DANGEROUS: these rules would be applied on this field; should clear it before
                // next field
                List<Rule> list = parseRulesOnField(field);
                if (list != null && !list.isEmpty()) {
                    mDataMocker.addRules(list);
                }

                boolean isThisFieldMocked = false;
                //Try mock String first
                if (fieldType.equals(String.class)) {
                    if (list == null || list.isEmpty()) {
                        //If this is a String field and no annotation presented, set a default
                        // length
                        mDataMocker.addRule(new Rule(ConstraintVerb.MAX_LENGTH, 32));
                    }
                    //Hacky argument: just use args in rule, not actual 0.
                    String value = mDataMocker.mockString();
                    assignValueToField(field, instance, value);
                    isThisFieldMocked = true;
                }

                //If not succeed, try primitive types
                if (!isThisFieldMocked) {
                    isThisFieldMocked = mockingPrimitiveField(instance, field, fieldType);
                }

                mDataMocker.clearRules();

                //Still not succeed, try recursive mock (if allowed)
                if (!isThisFieldMocked) {
                    if (mIsRecursive) {
                        Object recursiveMockObj = mDataMocker.mockObject(fieldType);
                        assignValueToField(field, instance, recursiveMockObj);
                    }
                }
            }
            return instance;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    private boolean mockingPrimitiveField(T instance, Field field, Class<?> fieldType) {
        boolean fieldIsMocked = true;
        if (fieldType.equals(boolean.class)) {
            assignValueToField(field, instance, mDataMocker.mockBoolean());
        } else if (fieldType.equals(Boolean.class)) {
            assignValueToField(field, instance, mDataMocker.mockBoolean());
        }
        //For int fields
        else if (fieldType.equals(byte.class)) {
            assignValueToField(field, instance, ((byte) mDataMocker.mockInt()));
        } else if (fieldType.equals(Byte.class)) {
            assignValueToField(field, instance, ((byte) mDataMocker.mockInt()));
        } else if (fieldType.equals(short.class)) {
            assignValueToField(field, instance, ((short) mDataMocker.mockInt()));
        } else if (fieldType.equals(Short.class)) {
            assignValueToField(field, instance, ((short) mDataMocker.mockInt()));
        } else if (fieldType.equals(int.class)) {
            assignValueToField(field, instance, mDataMocker.mockInt());
        } else if (fieldType.equals(Integer.class)) {
            assignValueToField(field, instance, mDataMocker.mockInt());
        } else if (fieldType.equals(long.class)) {
            assignValueToField(field, instance, ((long) mDataMocker.mockInt()));
        } else if (fieldType.equals(Long.class)) {
            assignValueToField(field, instance, ((long) mDataMocker.mockInt()));
        }
        //For double fields
        else if (fieldType.equals(float.class)) {
            assignValueToField(field, instance, ((float) mDataMocker.mockDouble()));
        } else if (fieldType.equals(Float.class)) {
            assignValueToField(field, instance, ((float) mDataMocker.mockDouble()));
        } else if (fieldType.equals(double.class)) {
            assignValueToField(field, instance, mDataMocker.mockDouble());
        } else if (fieldType.equals(Double.class)) {
            assignValueToField(field, instance, mDataMocker.mockDouble());
        }
        //Void Type is ignored
        //If fieldType not match any above, clear rules.
        else {
            fieldIsMocked = false;
            mDataMocker.clearRules();
        }
        return fieldIsMocked;
    }

    private List<Rule> parseRulesOnField(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        List<Rule> rules = new ArrayList<>();
        for (Annotation ano : annotations) {
            AffectType affectType = ano.annotationType().getAnnotation(AffectType.class);
            if (affectType == null) {
                continue;
            }
            List<Class> list = Arrays.asList(affectType.types());

            //This field is annotated but its type not match, just ignore it
            //For example: imagine a int field annotated by a @FixedLength
            if (list.isEmpty() || !list.contains(field.getType())) {
                continue;
            }

            //parse rule and add to list
            //Tip: here in a loop, so ano just means only one rule
            //For int/double field rules
            if (ano instanceof LessThan) {
                rules.add(new Rule(ConstraintVerb.LESS_THAN, ((int) ((LessThan) ano).value())));
            } else if (ano instanceof MoreThan) {
                rules.add(new Rule(ConstraintVerb.MORE_THAN, ((int) ((MoreThan) ano).value())));
            }
            //For String field rules
            else if (ano instanceof MaxLength) {
                rules.add(new Rule(ConstraintVerb.MAX_LENGTH, ((MaxLength) ano).value()));
            } else if (ano instanceof MinLength) {
                rules.add(new Rule(ConstraintVerb.MIN_LENGTH, ((MinLength) ano).value()));
            } else if (ano instanceof FixedLength) {
                rules.add(new Rule(ConstraintVerb.FIXED_LENGTH, true));
                rules.add(new Rule(ConstraintVerb.MAX_LENGTH, ((FixedLength) ano).value()));
            } else //noinspection ConstantConditions
                if (ano instanceof MatchRegex) {
                    rules.add(new Rule(ConstraintVerb.MATCH_REGEX, ((MatchRegex) ano).value()));
                }

        }

        //For Android: Support android-annotation @Nullable
        //Fields are non-null by default, so @NonNull is no need to determine
        if (mAndroidSupportNullableAnnotation != null
                && field.isAnnotationPresent(mAndroidSupportNullableAnnotation)) {
            rules.add(new Rule(ConstraintVerb.NULLABLE, true));
        }
        return rules;
    }

    private <FieldType> void assignValueToField(Field field, T instance, FieldType value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
