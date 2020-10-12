package com.yzlm.cyl.cfragment.DBConvert;

import com.yzlm.cyl.clibrary.Util.DataUtil;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yzlm.cyl.clibrary.Util.DataUtil.copybyte;

/**
 * Created by zwj on 2017/6/30.
 */

@SuppressWarnings("Annotator")
public class MDBConvert {

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static byte[] shortToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * 字节转换为浮点
     *
     * @param b 字节（至少4个字节）
     * @param
     * @return
     */
    public static float byteArrayTofloat(byte[] b) {

        return Float.intBitsToFloat(byteArrayToInt(b));
    }

    /**
     * 将一个浮点数转换为字节数组（4个字节），b[0]存储高位字符，大端
     *
     * @param f 浮点数
     * @return 代表浮点数的字节数组
     */
    public static byte[] floatToBytesBigs(float f) {
        return intToBytes(Float.floatToIntBits(f));
    }

    /**
     * 将一个整数转换位字节数组(4个字节)，b[0]存储高位字符，大端
     *
     * @param i 整数
     * @return 代表整数的字节数组
     */
    public static byte[] intToBytes(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i >>> 24);
        b[1] = (byte) (i >>> 16);
        b[2] = (byte) (i >>> 8);
        b[3] = (byte) i;
        return b;
    }

    public static byte[] bytesReverseTwoByte(byte[] bts) {
        byte[] b = new byte[bts.length];

        for (int i = 0; i < bts.length; i += 4) {
            byte[] b1 = new byte[2];
            byte[] b2 = new byte[2];
            byte[] b3 = new byte[4];
            b1[0] = bts[i];
            b1[1] = bts[i + 1];

            b2[0] = bts[i + 2];
            b2[1] = bts[i + 3];

            b3 = copybyte(b2, b1);
            System.arraycopy(b3, 0, b, i, b3.length);
        }
        return b;
    }

    public static byte[] bytesReverse(byte[] array) {

        if (array != null) {
            int i = 0;

            for(int j = array.length - 1; j > i; ++i) {
                byte tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                --j;
            }

        }
        return array;
    }


    /**
     * 数字与大小写字母混编字符串
     *
     * @param size
     * @return
     */
    public static String getNumLargeSmallLetter(int size) {
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            if (random.nextInt(2) % 2 == 0) {//字母
                if (random.nextInt(2) % 2 == 0) {
                    buffer.append((char) (random.nextInt(27) + 'A'));
                } else {
                    buffer.append((char) (random.nextInt(27) + 'a'));
                }
            } else {//数字
                buffer.append(random.nextInt(10));
            }
        }
        return buffer.toString();
    }

    // 不足多少位补'0'
    public static String ZeroPadding(String iCode, int num) {
        String result;
        result = String.valueOf(iCode);
        while(result.length() < num){
            result = ('0' + result);
        }
        return result;
    }


    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     *
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum, int endNum) {
        if (endNum > startNum) {
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }

    public static int getSystemDateYear() {

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getSystemDateMonth() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        return month;
    }

    public static int getSystemDateDay() {

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     *
     * @return 星期日:1    星期一:2    星期二:3    星期三:4    星期四:5    星期五:6    星期六:7
     */
    public static int getSystemDateDayOfWeek() {

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getSystemDateDayOfMonth() {

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getSystemDateHour() {

        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getSystemDateMin() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        return minute;
    }

    public static int getSystemDateSec() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH) + 1;//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        return cal.get(Calendar.SECOND);
    }

    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     *
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1])  //交换两数位置
                {
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
    }

    /***
     * 判断字符串是否有数字
     * */

    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
    }

    /**
     * 截取数字  【读取字符串中第一个连续的字符串，不包含后面不连续的数字】
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * long 转换为 字节字符串
     */
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * long 转换为 指定字节长度字符串
     */
    public static byte[] ToBytes(long x, int num) {
        ByteBuffer buffer = ByteBuffer.allocate(num);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static void reverseTwobytes(byte[] array) {
        if (array != null) {
            for (int k = 0; k < array.length / 4; k++) {
                byte[] tmp = new byte[2];
                tmp[0] = array[array.length - (k + 1) * 2];
                tmp[1] = array[array.length - (k + 1) * 2 + 1];
                array[array.length - (k + 1) * 2] = array[k * 2];
                array[array.length - (k + 1) * 2 + 1] = array[k * 2 + 1];
                array[k * 2] = tmp[0];
                array[k * 2 + 1] = tmp[1];
            }
        }
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param
     * @return HexString
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /*
     字符串转 hex
     ascii  hex
    * ***/
    public static String convertStringToHex(String str) {

        char[] chars = str.toCharArray();

        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }

        return hex.toString();
    }


    /*
    hex 转 字符串 ascii
    * **/
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] toBytes(String data, String typeData, int dataLen) {
        byte[] arrayOfByte;
        switch (typeData) {
            case "FLOAT"://不动
                arrayOfByte = floatToBytesBigs(Float.parseFloat(data));
                return arrayOfByte;
            case "FLOAT2"://半个
                arrayOfByte = floatToBytesBigs(Float.parseFloat(data));
                // 大端
                arrayOfByte = bytesReverseTwoByte(arrayOfByte);
                return arrayOfByte;
            case "FLOAT3"://全反
                arrayOfByte = floatToBytesBigs(Float.parseFloat(data));
                DataUtil.reverse(arrayOfByte);
                return arrayOfByte;
            case "WORD":
            case "DATE":
                arrayOfByte = shortToByteArray(Integer.parseInt(data)/*Short.parseShort(data)*/);
                return arrayOfByte;
            case "DWORD":
                arrayOfByte = intToBytes(Integer.parseInt(data));
                // 大端
                arrayOfByte = bytesReverseTwoByte(arrayOfByte);
                return arrayOfByte;
            case "LONG":
                arrayOfByte = longToBytes(Long.parseLong(data));
                return arrayOfByte;
            case "CHAR[]":
                arrayOfByte = charsToBytes(data, dataLen * 2);
                return arrayOfByte;
            case "BYTE[]":
                arrayOfByte = HexString2Bytes(data);
                return arrayOfByte;
            default: {
                arrayOfByte = new byte[dataLen * 2];
                return arrayOfByte;
            }
        }
    }


    public static String BytesToData(byte[] rs, int sBit, String typeData, int dataLen, boolean littleEndian) {
        String sData = null;
        switch (typeData) {
            case "WORD":
                sData = String.valueOf((((short) rs[sBit] & 0xff) << 8) | (rs[sBit + 1] & 0xff));//获取寄存器起始地址
                break;
            case "FLOAT":
                byte[] arrayOfByte1 = new byte[4];
                System.arraycopy(rs, sBit, arrayOfByte1, 0, 4);
                if (littleEndian) {
                    arrayOfByte1 = bytesReverseTwoByte(arrayOfByte1);
                }
                sData = String.valueOf(byteArrayTofloat(arrayOfByte1));
                break;
            case "FLOAT2":
                byte[] arrayOfByte4 = new byte[4];
                System.arraycopy(rs, sBit, arrayOfByte4, 0, 4);
                if (littleEndian) {

                }
                sData = String.valueOf(byteArrayTofloat(arrayOfByte4));
                break;
            case "FLOAT3":
                byte[] arrayOfByte5 = new byte[4];
                System.arraycopy(rs, sBit, arrayOfByte5, 0, 4);
                if (littleEndian) {
                    DataUtil.reverse(arrayOfByte5);
                }
                sData = String.valueOf(byteArrayTofloat(arrayOfByte5));
                break;
            case "DWORD":
                byte[] arrayOfByte = new byte[4];
                System.arraycopy(rs, sBit, arrayOfByte, 0, 4);
                if (littleEndian) {
                    arrayOfByte = bytesReverseTwoByte(arrayOfByte);
                }
                sData = String.valueOf(byteArrayToInt(arrayOfByte));
                break;
            case "CHAR[]":
                byte[] arrayOfByte2 = new byte[dataLen * 2];
                System.arraycopy(rs, sBit, arrayOfByte2, 0, dataLen * 2);

                String sHex = bytesToHexString(arrayOfByte2);
                sData = convertHexToString(getASSCIString(sHex));
                break;
        }
        return sData;
    }


    /**
     * 去除字符串中连续两个00的情况再获取ASSCIS码    4E 00 00 00 00 // N
     *
     * @param str
     * @return
     */
    public static String getASSCIString(String str) {

        String sR = "";
        int iStart = 0;
        int iEnd = 2;
        for (int i = 0; i < str.length(); i += 2) {
            String s = str.substring(iStart, iEnd);
            if (!s.equals("00")) {
                sR += s;
            }
            iStart = iEnd;
            iEnd += 2;
        }
        return sR;
    }

    /**
     * 找出未出现的最小正整数
     *
     * @param
     * @param
     * @date 2016-10-7
     * @author shaobn
     */
    public static int findArrayMex(int[] A, int n) {

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (A[i] > 0 && A[i] <= n) {

                res[A[i] - 1] = 1;
            }
        }
        for (int i = 0; i < n; i++) {
            if (res[i] == 0) {
                return i + 1;
            }
        }
        return n + 1;
    }

    /*
     * ascii字符串转 hex 数组
     * "N"  0x4e
     * ***/
    public static byte[] charsToBytes(String strChars, int byteLen) {
        byte[] bs2 = new byte[byteLen];
        String[] cs = new String[strChars.length()];
        for (int i = 0; i < strChars.length(); i++) {
            cs[i] = String.valueOf(strChars.charAt(i));
        }
        for (int i = 0; i < strChars.length(); i++) {
            bs2[i] = (byte) Integer.parseInt(convertStringToHex(cs[i]), 16);
        }
        return bs2;
    }


    /**
     * 不能全是相同的数字或者字母(如:000000、111111、aaaaaa)
     *
     * @param numOrStr()>0
     * @return 全部相同返回true
     */
    public static boolean equalStr(String numOrStr) {
        boolean flag = true;
        char str = numOrStr.charAt(0);
        for (int i = 0; i < numOrStr.length(); i++) {
            if (str != numOrStr.charAt(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    // 仅仅包含字母和数字
    private static String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
    /**
     * 判断字符串是否仅含有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        return str.matches(LETTER_DIGIT_REGEX);
    }


    /*
     * 针对历史数据格式的时间字符串提取 Year 值
     * **/
    public static int getHistoryTimeStringByYear(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return year;
    }

    /*
     * 针对历史数据格式的时间字符串提取 month 值
     * **/
    public static int getHistoryTimeStringByMonth(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return month;
    }

    /*
     * 针对历史数据格式的时间字符串提取 day 值
     * **/
    public static int getHistoryTimeStringByDay(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return day;
    }


    /*
     * 针对历史数据格式的时间字符串提取 小时 值
     * **/
    public static int getHistoryTimeStringByHour(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return hour;
    }


    /*
     * 针对历史数据格式的时间字符串提取 分钟 值
     * **/
    public static int getHistoryTimeStringByMin(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return min;
    }

    /*
     * 针对历史数据格式的时间字符串提取 分钟 值
     * **/
    public static int getHistoryTimeStringBySec(String strHisFormatTimeStr) {
        int year = -1, month = -1, day = -1, hour = -1, min = -1, sec = -1;
        if (strHisFormatTimeStr != null && (!strHisFormatTimeStr.equals(""))) {
            strHisFormatTimeStr = strHisFormatTimeStr.replace(" ", "-");
            String[] time = strHisFormatTimeStr.split("[:-]");
            year = Integer.valueOf(time[0]);
            month = Integer.valueOf(time[1]);
            day = Integer.valueOf(time[2]);
            hour = Integer.valueOf(time[3]);
            min = Integer.valueOf(time[4]);
            sec = Integer.valueOf(time[5]);
        }
        return sec;
    }


    //判断一个字符是否都为数字
    @SuppressWarnings("Annotator")
    public static boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    /** 获取当前时间前一个小时的时间
     * @return
     */
    public static String getBeforeOneHourToNowDate( ){

        Calendar calendar = Calendar.getInstance();

        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(calendar.getTime());
    }

    /**
     * 把两个String[]合并为一个
     * @param a
     * @param b
     * @return
     */
    public static String[] concat(String[] a, String[] b) {
        String[] c= new String[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
	
	 public static String getSetFormatTime(String year, String month, String day, String hour, String minute, String second) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        try {
            temp = new Timestamp(format.parse(temp).getTime()).toString();
            if (temp.contains(".")) {
                temp = temp.substring(0, temp.indexOf("."));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

}
