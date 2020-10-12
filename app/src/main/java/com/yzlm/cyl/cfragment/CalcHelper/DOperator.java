package com.yzlm.cyl.cfragment.CalcHelper;

/**
 * Created by WL on 2017/4/19.
 * 说明： 用可以通过定义SOperatro/DOperator的实现类，并调用StringExpression
 * 中的操作符注册方法： registryOp进行注册，可以在字符串表达式中出现自定义
 * 操作符号. 自定义操作符号必须满足以下条件：
 * 1： 必须是标准的操作符号
 * 2： 不能包括这些符号 0,1,2,3,4,5,6,7,8,9,.,E,PI
 */

interface DOperator {
    Object calculate(Object op_num1, Object op_num2) throws DataInvalidException;
}
