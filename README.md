# DataMocker
一个专注于构造任意数据的开发工具库，方便快速开发和测试。
Java和Android开发环境均可使用。

## 依赖文档
```Groovy
compile 'org.mym.datamocker:data-mocker:0.1.0'
```

## 用法展示
```Java
DataMocker mocker = new DataMocker();

//基础类型
boolean randomCond = mocker.newBoolean();

int a = mocker.newInt();
int bounded = mocker.newInt(10, 20);

double x = mocker.newDouble();
double y = mocker.newDouble(3d, 3.14);

// ------- 大招预警 ---------

@RecursiveMock
public class User {
    @FixedLength(5)
    @MatchRegex("[^0-9]+")
    String userName;

    private byte aByte;

    protected Byte bByte;

    public short aShort;

    /*package*/ Integer anInteger;

    @MoreThan(18)
    @LessThan(26)
    public int age;

    double scoreRate;

    Double balance;

    @MoreThan(38)
    boolean isVip;

    @MaxLength(10)
    @MatchRegex("[京津沪渝辽吉黑云贵川湘鄂赣疆蒙藏]")
    String location;

    @MatchRegex("[甲乙丙丁午己庚辛壬癸]{3,5}")
    private String favGirl;
}


User user = mocker.mockObject(User.class);

//User another = mocker.addRule("age", LESS_THAN, 100)
//    .addRule("gender", EXPLICIT_VALUE, 1)
//    .addRule("id", MORE_THAN, 10000000)
//    .mockObject(User.class);
```

关于上述代码中出现的`@FixedLength`,`@MatchRegex`等注解的含义，以及目前所有支持的注解，请[查看可用的约束注解](https://github.com/Muyangmin/DataMocker/tree/master/data-mocker/src/main/java/org/mym/datamocker/rule/annotation).

## 为什么需要这个库？
在回答这个问题之前，考虑一下可能使用假数据的场景：
* 开发过程中需要测试一些随机数据，或者边界条件
* API尚未开发完成，只有格式而不能调通
* 服务器故障，但又需要调试客户端逻辑
* 想开发一个完整的APP练手，又不想被API的版本演进束缚手脚

比如有这么一个函数:
```Java
public int parseAndSum(String a, String b) {
    //这只是一个example，请不要吐槽为什么没有做非空、格式和长度等保护
    return Integer.parseInt(a) + Integer.parseInt(b);
}
```

如果现在要对这个函数做测试，当然可以直接写死数据：
```Java
String a = "12";
String b = "3";
parseAndSum(a, b);
```
但是，这种写法有一些固有的弊端：
#### 一次只能测试一组数据
> 如果要测试另一组数据，需要修改代码并且重新编译运行。**~这对于大型项目是非常耗时的，谁用谁知道~**
#### 假数据测试的有效性依赖于开发人员的经验和细致程度
> 比如很可能只测试了正常情况而遗漏一些边界条件。
#### 对代码的侵入性极强
> 这样的测试代码基本是不需要提交到VCS的，在测试通过之后必须再次删除代码。而如果存在多处假数据（比如API故障或未开发完但需要调试客户端），这个问题会很头疼。

使用**DataMocker**，这些问题可以更优雅地解决：
* 一次编写可以测试N组数据，只需要执行对应业务路径即可测试，**不需要重新编译和打包**。
* 随机数据可以保证数据更**贴近真实情况**，并且有概率**触发边界条件**（当然可以通过约束来使其更贴近边界条件）
* **代码侵入性低并且有提交到VCS的价值**，只需要将数据流从mocker切换到API即可
```Java
//int result = parseAndSum(mocker.a, mocker.b);
int result = parseAndSum(api.a, api.b);
```

## 基本赋值策略
* **Boolean变量** : `true`或`false`，随机取值
* **Integer变量** : 随机取值
* **Double变量** : 随机取值
* **Java对象字段** : 默认对基本类型(Primitive types)随机取值，引用类型全部置空。

## 约束规则
在很多场景下随机数据可能并不符合业务场景，这时候需要使用约束规则来指定。
* **Explicit**: 显式指定赋值，适合需要强制指定某个字段值的情况（但是，如果传入的参数不匹配字段类型，为避免逻辑混淆，将直接报错）
* **LessThan**: 约定生成的值需要小于某个值（只适合int和double类型）
* **MoreThan**: 约定生成的值需要大于某个值（只适合int和double类型）
* **Recursive**: 默认情况下引用类型的字段将被置空，指定该选项将使指定字段递归赋值

#### Licence
```
   Copyright 2017 Muyangmin

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```