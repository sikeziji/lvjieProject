package com.yzlm.cyl.cfragment.CalcHelper;

/**
 * Created by WL on 2017/4/19.
 */

public class DMul implements DOperator {
    public Object calculate(Object op_num1, Object op_num2)
            throws DataInvalidException {
        try {
            double x = Double.parseDouble(op_num1.toString());// 取被乘数
            double y = Double.parseDouble(op_num2.toString());// 取乘数
            double result = x * y;
            return new Double(result);
        } catch (Exception e) {
            throw new DataInvalidException("您输入的字符串表达式，不符合格式要求，"
                    + "导致无法计算，请检测您的输入表达式");
        }// end try-catch
    }// end calculate
}
