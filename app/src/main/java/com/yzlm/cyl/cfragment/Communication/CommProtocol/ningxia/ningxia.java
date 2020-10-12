package com.yzlm.cyl.cfragment.Communication.CommProtocol.ningxia;

import com.yzlm.cyl.cfragment.Communication.Communication;
import com.yzlm.cyl.cfragment.Communication.Thread.SendManager;
import com.yzlm.cyl.cfragment.Config.Component.CfgTool.History;
import com.yzlm.cyl.cfragment.Global;
import com.yzlm.cyl.cfragment.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.History.getLastHistoryFlowData;
import static com.yzlm.cyl.cfragment.Config.Component.CfgTool.PublicConfig.getPublicConfigData;
import static com.yzlm.cyl.cfragment.Global.IOBoardUsed;
import static com.yzlm.cyl.cfragment.Global.context;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;

public class ningxia {
    public static long gs; //因子个数
    public static float C; //指定组分测量值
    public static String yzbmfhzl = ""; //返回因子
    public static String fhwzml; //完整命令
    public static String crcjyml;//crc命令
    public static float test; //因子编码测量值
    public static String[] yzcs;//因子参数

    public static void xieyiml(final Communication port, int sCom, byte[] rs) {
        try {
            String mlgs;
            String zhdx;
            mlgs = bytes_String16(rs);
            zhdx = mlgs.toUpperCase();
            //16进制转ASCII
            String jqwz = zhdx.substring(4, zhdx.length() - 4);
            String ace = "02" + convertStringToHex(jqwz) + "03";
            xy(ace, port, sCom); //转换的命令
        } catch (Exception e) {

        }
    }

    public static void xy(String newml, Communication port, int sCom) {
        try {
            if (panduan(newml)) {

                long jqsl = gs * 8; //因子命令长度
                String yzbm = newml.substring(10, (int) (10 + jqsl));//截取到的ASCII因子编码

                //把因子编码分割成数组保存
                String[] yzbmcf = new String[(int) gs];
                for (int a = 0; a < gs; a++) {
                    if (a == 0) {
                        yzbmcf[a] = yzbm.substring(0, 8);
                    } else {
                        yzbmcf[a] = yzbm.substring(a * 8, (a + 1) * 8);
                    }
                }
                //从配置文件读取因子参数
                if (yzcs == null) {
                    String fileContent;
                    fileContent = findFile("Csoft/Protocol/ningxia/", "ningxia");
                    if (fileContent != null) {
                        yzcs = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                    }
                }

                //根据命令判断因子名称
                for (int i = 0; i < yzbmcf.length; i++) {
                    //把因子编码转换成16进制
                    String mc = convertStringToHex(yzbmcf[i]);
                    //根据因子编码从配置文件寻找对应的因子参数
                    for (String a : yzcs) {
                        String[] c = a.split(",");
                        if (mc == c[1] || mc.equals(c[1])) {
                            yzbmfhzl += yzbmpz(c[0], Integer.parseInt(c[2]));
                            break;
                        }
                    }
                }
                //拼接crc校验字符串
                crcjyml = "3030" + newml.substring(6, 10) + yzbm + yzbmfhzl;

                int a = CRC_XModem(toBytes(convertStringToHex(crcjyml))); //crc
                String test = Integer.toHexString(a);
                //ASCII转换成十六进制
                fhwzml = "02" + crcjyml + convertHexToString(test) + "03";
                yzbmfhzl = "";
                sjsc(port, sCom, fhwzml);
            } else {
                //数据格式错误
            }
        } catch (Exception e) {

        }
    }

    /**
     * 返回的因子ASCII编码
     *
     * @param cmpName
     * @param zhi
     */
    public static String yzbmpz(String cmpName, int zhi) {
        String strHex = "";
        try {
            test = getC(cmpName);
            float a = test * zhi;
            int z = (int) Math.floor(a); //取整
            strHex = Integer.toHexString(z);//转换成16进制；
            //补零
            if (strHex.length() == 1) {
                strHex = "000" + strHex;
            } else if (strHex.length() == 2) {
                strHex = "00" + strHex;
            } else if (strHex.length() == 3) {
                strHex = "0" + strHex;
            }

        } catch (Exception e) {

        }
        return convertHexToString(strHex); //16进制转换成ASCII;
    }

    /**
     * 判断接收命令的长度
     *
     * @param ml
     */
    public static boolean panduan(String ml) {
        boolean tf = false;
        String a = ml.substring(6, 10);//判断因子个数
        //ASCII转换成16字符串
        String yzgs = convertStringToHex(a);
        //转换成10进制的因子个数
        gs = Long.parseLong(yzgs, 16);

        //命令长度固定个数
        long GD = 20;
        //命令总长度
        long zgs = gs * 8 + GD;
        if (ml.length() == zgs) {
            tf = true;
        }
        return tf;
    }


    /**
     * 16进制转ASCII
     *
     * @param hex
     */
    public static String convertStringToHex(String hex) {


        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }

    /**
     * ASCII转16进制
     *
     * @param str
     * @return
     */
    public static String convertHexToString(String str) {

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString();
    }


    /**
     * 查询组分测量值
     *
     * @param cmpName
     * @return
     */
    public static float getC(String cmpName) {
        try {
            History history = new History(context);
            List<Map> hisMap = history.getLastHistoryFlowData(cmpName, context.getString(R.string.ZY));
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                hisMap = getLastHistoryFlowData(cmpName, null);
            }
            if (hisMap.size() > 0) {
                C = Float.parseFloat(hisMap.get(0).get("C").toString());
            } else {
                C = 0;
            }
        } catch (Exception e) {
            C = 0;
        }
        return C;
    }


    /**
     * crc16校验(CCITT)
     *
     * @param bytes
     * @return
     */
    public static int CRC_XModem(byte[] bytes) {
        int crc = 0x00;          // initial value
        int polynomial = 0x1021;
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    /**
     * 找路径文件
     *
     * @param path
     * @param name
     * @return
     */
    public static String findFile(String path, String name) {

        if (Global.SdcardPath == null && Global.extSdcardPath == null) {
            return null;
        }
        String dirPath;
        if (Global.SdcardPath != null) {
            dirPath = Global.SdcardPath;
        } else {
            dirPath = Global.extSdcardPath;
        }
        String pathFileName;
        pathFileName = dirPath + path + name + ".txt";
        File dbFile = new File(dirPath + path);
        //判断文件路径是否存在；
        if (!dbFile.exists()) {
            //创建文件
            dbFile.mkdirs();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + path + "ningxia.txt"));
                bw.write("CODcr,210B,100#" + "\r\n" + "NH3N,213C,100#" + "\r\n" + "TP,2165,100#" + "\r\n" + "TN,213B,1#" + "\r\n" + "As,2117,1000#" + "\r\n" + "Hg,2119,1000#" + "\r\n" + "CN,211A,1000#" + "\r\n" + "CrVI,211B,1000#" + "\r\n" + "Pb,211C,1000#" + "\r\n" + "Tni,211D,1000#" + "\r\n" + "TZn,211E,1000#" + "\r\n" + "TCu,211F,1000#" + "\r\n" + "TCr,2121,1000#" + "\r\n" + "CODmn,213A,1#");
                bw.close();//一定要关闭文件
            } catch (Exception e) {

            }
        }
        if (dbFile.exists()) {
            return readString(pathFileName, "GBK");
        } else {
            return null;
        }
    }

    /**
     * byte 转 16进制
     *
     * @param b
     * @return
     */
    public static String bytes_String16(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(String.format("%02x", b[i]));
        }
        return sb.toString();
    }

    /**
     * 16 进制字符串转byte
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * 发送指令
     *
     * @param port
     * @param sCom
     * @param ml
     */
    public static void sjsc(Communication port, int sCom, String ml) {
        if (!ml.equals("")) {
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "宁夏协议_1_0" : "宁夏协议_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, ml.getBytes());
        }
    }
}