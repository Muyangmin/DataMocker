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
import org.mym.datamocker.rule.Rule;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

/**
 * Mocker implementation for Strings.
 * Created by Muyangmin on Apr 06, 2017.
 */
public class StringMocker implements IMocker<String> {

    //------------------- DEFAULT REGEX --------------------
    @SuppressWarnings("WeakerAccess")
    public static final String DEFAULT_REGEX = "[0-9a-zA-Z]{%d}";
    //------------------- DEFAULT REGEX --------------------

    private Integer mMaxLength;
    private Integer mMinLength;

    private boolean mNullable;
    private boolean mFixedLength;

    /**
     * Regex limitation of results.
     * based on {@link dk.brics.automaton.Automaton}.
     */
    private String mRegex;

    private Random mRandom = new Random();

    @Override
    public void applyRules(List<Rule> rules) {
        for (Rule rule : rules) {
            switch (rule.constraint) {
                case MAX_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Integer.class, rule);
                    int maxLength = ((Integer) rule.args);
                    if (maxLength < 0) {
                        throw new IllegalArgumentException("length must be non-negative!");
                    }
                    mMaxLength = maxLength;
                    break;
                case MIN_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Integer.class, rule);
                    int minLength = ((Integer) rule.args);
                    if (minLength < 0) {
                        throw new IllegalArgumentException("length must be non-negative!");
                    }
                    mMinLength = minLength;
                    break;
                case MATCH_REGEX:
                    RuleChecker.checkRuleTypeSafeOrThrow(String.class, rule);
                    String regex = ((String) rule.args);
                    if (RuleChecker.isSafeRegexOrThrow(regex)) {
                        mRegex = regex;
                    }
                    break;
                case NULLABLE:
                    RuleChecker.checkRuleTypeSafeOrThrow(Boolean.class, rule);
                    mNullable = ((Boolean) rule.args);
                    break;
                case FIXED_LENGTH:
                    RuleChecker.checkRuleTypeSafeOrThrow(Boolean.class, rule);
                    mFixedLength = ((Boolean) rule.args);
                    break;
            }
        }
    }

    @Override
    public void clearRules() {
        mMaxLength = null;
        mMinLength = null;
        mNullable = false;
        mFixedLength = false;
        mRegex = null;
    }

    @Override
    public String mock(Object... args) {
        DataMocker mocker = new DataMocker();
        //Fast Bingo!
        if (mNullable && mocker.mockBoolean()) {
            return null;
        }

        final String regex;
        if (mRegex != null) {
            regex = mRegex;
        } else {
            //Determine string length
            if (mMaxLength == null) {
                mMaxLength = 32;
            }
            if (mMinLength == null) {
                mMinLength = 0;
            }
            int length;
            if (mFixedLength) {
                length = mMaxLength;
            } else {
                length = getRandomInt(mMinLength, mMaxLength, mRandom);
            }

            //Generate a regex using alphabetic chars
            regex = String.format(Locale.US, DEFAULT_REGEX, length);
        }

        //Generate Char sequences
        Automaton automaton = new RegExp(regex).toAutomaton();
        StringBuilder builder = new StringBuilder();
        generate(builder, automaton.getInitialState());
        return builder.toString();
    }

    private void generate(StringBuilder builder, State state) {
        List<Transition> transitions = state.getSortedTransitions(true);
        if (transitions.size() == 0) {
            //Here state.isAccept must be true
//            assert state.isAccept();
            return;
        }
        int nroptions = state.isAccept() ? transitions.size() : transitions.size() - 1;
        int option = getRandomInt(0, nroptions, mRandom);
        if (state.isAccept() && option == 0) {          // 0 is considered stop
            return;
        }
        // Moving on to next transition
        Transition transition = transitions.get(option - (state.isAccept() ? 1 : 0));
        appendChoice(builder, transition);
        generate(builder, transition.getDest());
    }

    private void appendChoice(StringBuilder builder, Transition transition) {
        char c = (char) getRandomInt(transition.getMin(), transition.getMax(), mRandom);
        builder.append(c);
    }

    private int getRandomInt(int min, int max, Random random) {
        return IntMocker.getRandomInt(min, max, random);
    }
}
