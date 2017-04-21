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

import org.mym.datamocker.rule.annotation.CharSet;
import org.mym.datamocker.rule.annotation.FixedLength;
import org.mym.datamocker.rule.annotation.LessThan;
import org.mym.datamocker.rule.annotation.MatchRegex;
import org.mym.datamocker.rule.annotation.MaxLength;
import org.mym.datamocker.rule.annotation.MoreThan;
import org.mym.datamocker.rule.annotation.RecursiveMock;

/**
 * Sample data class.
 * You can define any field type to fix testing.
 * Created by Muyangmin on Apr 06, 2017.
 */
//This is just a demo class used for reflection, so just ignore all weaker access warning
@SuppressWarnings("WeakerAccess")
@RecursiveMock
public class User {
    @FixedLength(5)
    @MatchRegex("[^0-9]+")
    String userName;

    byte aByte;

    Byte bByte;

    short aShort;

    Integer anInteger;

    @MoreThan(18)
    @LessThan(26)
    public int age;

    double scoreRate;

    Double balance;

    @MoreThan(38)
    boolean isVip;

    @MaxLength(10)
    @CharSet("京津沪渝辽吉黑云贵川湘鄂赣疆蒙藏")
    String location;

    @MatchRegex("[甲乙丙丁午己庚辛壬癸]{3,5}")
    private String favGirl;

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", aByte=" + aByte +
                ", bByte=" + bByte +
                ", aShort=" + aShort +
                ", anInteger=" + anInteger +
                ", age=" + age +
                ", scoreRate=" + scoreRate +
                ", balance=" + balance +
                ", isVip=" + isVip +
                ", location='" + location + '\'' +
                ", favGirl='" + favGirl + '\'' +
                '}';
    }
}
