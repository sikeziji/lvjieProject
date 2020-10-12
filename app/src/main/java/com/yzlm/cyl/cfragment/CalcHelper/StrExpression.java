package com.yzlm.cyl.cfragment.CalcHelper;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by WL on 2017/4/19.
 */

@SuppressWarnings("unchecked")
public class StrExpression {
    private double result = 0;
    private Stack SourceStack = new Stack();//字符串表达式计算堆栈
    private Stack TempStack = new Stack();
    //为了便于操作，用2个hash表来记录操作符信息（这个程序运行效率不是问题）
    private static Hashtable op_level = new Hashtable();//操作符信息(操作符号，操作符级别);
    private static Hashtable op_class = new Hashtable();//操作符信息(操作符号，计算类);

    public StrExpression() {
        //注册标准标准操作符号
        //第一级别运算符号注册
        registryOp("n!", 1, "com.yzlm.cyl.cfragment.CalcHelper.SN");
        //注册阶乘运算
        registryOp("dao", 1, "com.yzlm.cyl.cfragment.CalcHelper.SDao");
        //注册倒数运算
        registryOp("sin", 1, "com.yzlm.cyl.cfragment.CalcHelper.SSin");
        //注册正玄运算
        registryOp("cos", 1, "com.yzlm.cyl.cfragment.CalcHelper.SCos");
        //注册余玄运算
        registryOp("tan", 1, "com.yzlm.cyl.cfragment.CalcHelper.STan");
        //注册正切运算
        registryOp("sqrt", 1, "com.yzlm.cyl.cfragment.CalcHelper.SSqrt");
        //注册开方运算
        registryOp("log", 1, "com.yzlm.cyl.cfragment.CalcHelper.SLog");
        //注册对数运算
        registryOp("ln", 1, "com.yzlm.cyl.cfragment.CalcHelper.SLn");
        //注册e为底的对数运算
        registryOp("exp", 1, "com.yzlm.cyl.cfragment.CalcHelper.SExp");
        //注册指数运算
        registryOp("pin", 1, "com.yzlm.cyl.cfragment.CalcHelper.SX2");
        //注册平方运算
        registryOp("li", 1, "com.yzlm.cyl.cfragment.CalcHelper.SX3");
        //注册立方运算
        //第二级别运算符号注册
        registryOp("^", 2, "com.yzlm.cyl.cfragment.CalcHelper.DPow");
        //注册立方运算
        registryOp("*", 2, "com.yzlm.cyl.cfragment.CalcHelper.DMul");
        //注册乘法运算
        registryOp("/", 2, "com.yzlm.cyl.cfragment.CalcHelper.DDiv");
        //注册除法运算
        registryOp("%", 2, "com.yzlm.cyl.cfragment.CalcHelper.DMod");
        //注册求余数运算
        //第三级别运算符号注册
        registryOp("+", 3, "com.yzlm.cyl.cfragment.CalcHelper.DAdd");
        //注册加法运算
        registryOp("-", 3, "com.yzlm.cyl.cfragment.CalcHelper.DDec");
        //注册减法运算

        registryOp("lg", 1, "com.yzlm.cyl.cfragment.CalcHelper.SLg");
    }

    /**
     * @description: 注册操作符号信息
     * 说明： 所以有的运算符都必须注册.
     */
    public static void registryOp(String strOp, int level, String strClass) {
        op_level.put(strOp, new Integer(level));
        op_class.put(strOp, strClass);
    }

    /**
     * 获取某操作符的运算级别
     * @return 操作符级别
     */
    private int getLevel(String strOp) {
        String strLevel = op_level.get(strOp).toString();
        if (strLevel == null)
            return 0;
        else
            return Integer.parseInt(strLevel);
    }

    /**
     *判断给定字符串是否是已注册的操作符号
     * @return 如果是已经注册的操作符号，者返回true,否则返回false。
     */
    private boolean isOp(String strOp) {
        return op_level.containsKey(strOp);
    }
    /**
     * 获取某操作符的运算类
     * @return 操作符的运算类
     */
    private String getClass(String strOp) {
        return (String) op_class.get(strOp);
    }

    /**
     *将字符串表达式分解成运算符号和运算数，并将分解后的字符串保存到堆栈中
     * @return 运算数和运算符的集合。
     */
    private Stack splitStr(String strExpression) throws DataInvalidException {
        //----------------------------------------------
        HashSet hs = new HashSet();
        hs.addAll(op_level.keySet());
        hs.add("(");
        hs.add(")");
        //运算符号集合.
        //------------------------------------------------
        Iterator i = hs.iterator();//用来遍历运算符
        String strOp;
        StringBuilder temp = new StringBuilder(strExpression);//为了提高运算效率，引入该变量

        //----------------------------------------------------------------------------------
        //表答式最前面容许出现‘-’号（负号）,可以出现多个负，但没什么意义，所以目前程序
        //只许出现一个负号..
        String strHead = "";//用来保存”-”号
        while (true) {//
            if (temp.charAt(0) == '-') {
                temp.deleteCharAt(0);
                strHead += "-";
            } else
                break;
        }
        //----------------------------------------------------------------------------------
        if (temp.toString().trim().length()== 0)//只有负号的表达式
            throw new DataInvalidException("您输入的字符串表达式，不符合格式要求，"
                    + "导致无法计算，请检测您的输入表达式");
        while (i.hasNext()) {
            int intInsertPos = 0;//'#'的插入点.
            int intIndex = 0;//搜索位置
            int intLen = 0;//操作符的长度
            int intDiff = 0;//记录位置差
            strOp = i.next().toString();//取出一个运算符
            intLen = strOp.length();
            strExpression = temp.toString();//temp的值会发生变化，每次插入符号前
            //要保证strExpression与temp值相同
            while (true) {
                intIndex = strExpression.indexOf(strOp,intIndex);
                if (intIndex == -1) // 没有找到.
                    break;
                intInsertPos = intIndex + intDiff;//设置插入位置
                if (intIndex == 0) {
                    temp.insert(intInsertPos + intLen, '#');//运算符号后面插入“#”
                    intDiff++;
                } else {
                    if (strOp.equals("-")){
                        String preIndex=strExpression.substring(intIndex-1,intIndex);
                        boolean isFlag=false;
                        for(Iterator it=hs.iterator();it.hasNext();)
                        {
                            if (preIndex.equals(it.next())){
                                isFlag=true;
                                break;
                            }else {
                                if (preIndex.equals("#")){
                                    String preIndex1=strExpression.substring(intIndex-2,intIndex-1);
                                    Iterator it1=hs.iterator();
                                    while (it.hasNext()) {
                                        isFlag=true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (!isFlag){
                            temp.insert(intInsertPos, '#');//运算符号前插入“#”
                            temp.insert(intInsertPos + intLen + 1, '#');//运算符号后面插入“#”
                            intDiff += 2;
                        }
                    }else {
                        temp.insert(intInsertPos, '#');//运算符号前插入“#”
                        temp.insert(intInsertPos + intLen + 1, '#');//运算符号后面插入“#”
                        intDiff += 2;
                    }
                }
                intIndex += intLen;//调整搜索位置.
            }//end while(true);
        }// end while(i .hasNext());
        strExpression = strHead + temp.toString();
        // 创建个StringTokenizer对象用来提取表达式中的运算符号.
        Stack result = new Stack();
        StringTokenizer tool = new StringTokenizer(strExpression, "#");
        while (tool.hasMoreElements())
            result.push(tool.nextElement());
        return result;
    }

    /**
     *@decription: 获取表达式的值.
     *@param value : 待计算的字符串表达式
     *@return : 字符串表达式的值.
     */
    public double getValue(String value) throws DataInvalidException {
        int intIndexLeft;//最后一个“（”在字符串value 中的位置
        int intIndexRight;//第一个");"在字符串value中的位置.

        intIndexLeft = value.lastIndexOf("(");//取得左括号的索引
        if (intIndexLeft == -1) {//value中不存在括号.
            try {
                result = parseValue(value);//如果当value不是个表达式时，会触发异常
            } catch (DataInvalidException die) {
                throw die;
            }//end try -catch
        }// end if
        else {
            intIndexRight = value.indexOf(")", intIndexLeft);//获取与左括号相匹配的右括号。
            //将表达式分成 左 中 右三串
            String strLeft = value.substring(0, intIndexLeft);//取左串
            String strTemp = value.substring(intIndexLeft + 1, intIndexRight);//取中串
            double dblTemp;
            try {
                dblTemp = parseValue(strTemp);//计算中串的值.
            } catch (DataInvalidException die){
                throw die;
            }
            String strMid = new Double(dblTemp).toString();//得到新的中串
            if (strMid.contains("E")){
                strMid= (new BigDecimal(strMid).toPlainString());
            }

            String strRight = value.substring(intIndexRight + 1);//获取右串
            value = strLeft + strMid + strRight;//重新组合字符串表达式 .
            getValue(value);//递归计算.
        } //end else
        return result;
    }//end getValue

    /**
     *@description:计算运算因子的值
     *@param value : 运算因子
     *@return: 运算因子的值
     */
    private  double parseValue(String value)throws DataInvalidException{

        this.SourceStack.clear();
        this.TempStack.clear();
	      /*1:数据合法性检测
	      *如果数据是非法的，则抛出DataInvalidException异常，为了
	       *简化问题，在这不做数据合法性检测.如果您是做正式产品，这步不能省.
	       */

	      /*2：数据分解
	       *将字符串的表达式分解成运算符号，和运算数的集合体,并将这些数据压入SourceStack堆栈中.
	       *如果输入串是"32+33*14-1”那么分解后将会是"32","+","33"*","14","-","1"这个样子。
	      *
	      *分解方法：
	       *为了便于说明问题，假设输入字符串是-32+2*4+3-1
	       *我门的任务就是将“-32+2*4+3-1“分解成多个字符串: “-32”,”+”,”2”,”*”,”4”,”+”,+”3”,”-“,”1”.
	       *兄弟门有没有发现：没个运算都是夹在2个数的中间，要是我们在运算符的前面和后面都插入字符“，”，就可以将数字
	       *和运算符号分开.我门来看看插入符号’,’后字符串的形态： -32，+，2，*，4，*，3,-,1.
	       *分解这类字符串是StringTokenizer的拿手把戏了，只要将分解标志设为”,”就可以搞定了.
	       */
        try{
            SourceStack.addAll(this.splitStr(value));
        }catch (DataInvalidException die){
            throw die;
        }
        //消除第1级别的运算符
        delLevel1();
        //将TempStack中的数据挪到SourceStack中，并清除TempStack中内容。这样做没什么特别
        //的原因，只是为了沿用上版的某些功能.
        SourceStack.addAll((Collection)TempStack.clone());
        TempStack.clear();

        //如果堆栈中只有一个数据则认为是一个合法数据，否则触发异常.
        if (SourceStack.size()== 1){
            try {
                return Double.parseDouble(SourceStack.pop().toString());
            }catch (Exception e){
                throw new DataInvalidException("您输入的字符串表达式，不符合格式要求，"+
                        "导致无法计算，请检测您的输入表达式");
            }
        }
        //第2层处理，消除第2级别的运算符号
        delLevel2();
        //第三层处理，消除第三级别的运算符号
        Collections.reverse(TempStack);
        return delLevel3();//返回基本运算因子的运算结果 .
    }// end StringToValue

    //第1层处理，消除第1级别的运算符号
    private  void delLevel1(){
        //第1层处理，消除第1级别的运算符号
        String strTemp;//用来保存从SourceStack中弹出来的字符串.
        int intSize = SourceStack.size();
        for( int i=0;i< intSize;i++){
            strTemp = (String)SourceStack.pop();
            if (isOp(strTemp)){//如果是运算符号
                int intLevel = this.getLevel(strTemp);//获取运算级别
                if (intLevel==1){//
                    Object objX = TempStack.pop();//取出运算数
                    String strClass = getClass(strTemp);// 获取类包路径
                    try {
                        Class clsCalculate = Class.forName(strClass);
                        SOperator cal =(SOperator)clsCalculate.newInstance();
                        TempStack.push(cal.calculate(objX));
                    }catch(Exception e){
                        e.printStackTrace();
                    }//end try-catch
                }//if (intLevel==1);
                if ( intLevel==3 ||intLevel==2){//将2,3级别运算符号进行压栈处理
                    TempStack.push(strTemp);
                }//end if
            }//end if ( strOperator.indexOf(strTemp);!=-1 ); //如果是运算符号
            if (isOp(strTemp)== false ){//如果是运算数
                TempStack.push(strTemp);//将运算数直接压栈
            }
        }//end for
        //SourceStack.addAll((Collection);s.clone(););;
        return ;
    }

    //第2层处理，消除第2级别的运算符号
    private void delLevel2(){
        //第2层处理，消除第2级别的运算符号
        String strTemp;//用来保存从SourceStack中弹出来的字符串.
        int intSize = SourceStack.size();//堆栈大小
        //循环检测2级运算符，并消除之.
        for( int i=0 ;i< intSize;i++){
            //注意，程序最多的循环次数是intSize,当表达式中出现*，/号时，循环次数会小
            //于intSize次，所以必须加上下面这条语句.
            if (SourceStack.isEmpty())
                break;
            strTemp = SourceStack.pop().toString();
            if (isOp(strTemp)){//如果是运算符号
                int intLevel = this.getLevel(strTemp);//获取运算级别
                if (intLevel==2){
                    Object objX = TempStack.pop();//取出运算数
                    Object objY = SourceStack.pop();//取出运算数
                    String strClass = getClass(strTemp);
                    try {
                        Class clsCalculate = Class.forName(strClass);
                        DOperator cal = (DOperator)clsCalculate.newInstance();
                        TempStack.push(cal.calculate(objX,objY));
                    }catch(Exception e){
                        e.printStackTrace();
                    }//end try-catch
                }//if (intLevel==2);
                if (intLevel==3){//将3级别运算符号进行压栈处理
                    TempStack.push(strTemp);
                }//end if
            }//end if ( strOperator.indexOf(strTemp);!=-1 );//如果是运算符号
            if (isOp(strTemp)== false ){//如果是运算数
                TempStack.push(strTemp);//将运算数直接压栈
            }
        }//end for
    }//end level2

    //第三层处理，消除第三级别的运算符号
    private double delLevel3(){
        //第三层处理，消除第三级别的运算符号
        while(true){
            if (TempStack.size()== 1)//如果TempStack中只剩下一项，那么剩下的这项必定
                return ( (Double)TempStack.pop()).doubleValue();//是计算结果.
            Object objX = TempStack.pop();//取出运算数
            String strOp = TempStack.pop().toString();//获取操作符号
            Object objY = TempStack.pop();//取出运算数
            String strClass = getClass(strOp);
            try {
                Class clsCalculate = Class.forName(strClass);
                DOperator cal = (DOperator)clsCalculate.newInstance();
                TempStack.push(cal.calculate(objX,objY));
            }catch(Exception e){
                e.printStackTrace();
            }
            //end while
        }
    }//end StringToValue
}

