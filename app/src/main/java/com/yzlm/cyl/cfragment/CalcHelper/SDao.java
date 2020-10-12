package com.yzlm.cyl.cfragment.CalcHelper;

/**
 * Created by WL on 2017/4/19.
 */

public class SDao implements SOperator {
    public Object calculate(Object op_num) throws DataInvalidException {
        try {
            double x = Double.parseDouble(op_num.toString());// 取被除数
            double result = 1 / x;
            return new Double(result);
        } catch (Exception e) {
            throw new DataInvalidException("您输入的字符串表达式，不符合格式要求，"
                    + "导致无法计算，请检测您的输入表达式");
        }// end try-catch
    }// end calculate
}
