package com.yzlm.cyl.cfragment.Communication.CommProtocol.shenzhen;

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
import static com.yzlm.cyl.cfragment.Global.strComponent;
import static com.yzlm.cyl.clibrary.Util.FileUtil.readString;

public class shenzhen {
    public static String baotou = "%01$RD";
    public static String[] zfgs;  //组分名称
    public static String C; //指定组分测量值
    public static String crcjyzfc; //拼接校验字符串
    public static String CLZ;//测量值
    public static String Pjhfwzml; //返回完整命令
    public static String[] yzcs; //从配置文件读取的因子参数

    public static void shenzhenxy(final Communication port, int sCom, byte[] rs) {

        String zhdx = bytes_String16(rs).toUpperCase();
        //16进制转ASCII
        String ASCII = convertStringToHex(zhdx);

        if (ASCII.equals("%01#RDD0152101525**0D")) { //接收数据格式正确
            // 获取主界面所有组分名称
            String xzf = "";
            if (strComponent.get(1).length > 0) {
                for (String item : strComponent.get(1)) {
                    xzf += item + ",";
                }
            }
            zfgs = xzf.split(",");
            //判断主界面有几个组分，获取主界面组分名称
            //通过组分名称返回查询的测量参数
            for (String zf : zfgs) {
                String[] fhcs = getC(zf).split(",");
                String clz = fhcs[0];
                String rq = fhcs[1].substring(2, fhcs[1].length());
                String jqsj = rq.replace("-", "").replace(":", "").replace(" ", ""); //返回时间
                //判断测量值保留小数后面的几位；

                //从配置文件获取因子参数
                if (yzcs == null) {
                    String fileContent;
                    fileContent = findFile("Csoft/Protocol/shenzhen/", "shenzhen");
                    if (fileContent != null) {
                        yzcs = fileContent.replace("\t", "").replace(" ", "").replace("\r\n", "").replace("\n", "").split("#");
                    }
                }

                //根据因子编码从配置文件寻找对应的因子参数
                for (String a : yzcs) {
                    String[] c = a.split(",");
                    if (zf == c[0] || zf.equals(c[0])) {
                        String clzcs = clz.replace(".", "-");
                        String[] fgclz = clzcs.split("-");
                        String adc = fgclz[1].substring(0, Integer.parseInt(c[1]));
                        CLZ = fgclz[0] + adc;
                        break;
                    }
                }
                if (CLZ.length() == 1) {
                    String a = "0000000" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 2) {
                    String a = "000000" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 3) {
                    String a = "00000" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 4) {
                    String a = "0000" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 5) {
                    String a = "000" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 6) {
                    String a = "00" + CLZ;
                    CLZ = a;
                } else if (CLZ.length() == 7) {
                    String a = "0" + CLZ;
                    CLZ = a;
                }
                crcjyzfc = baotou + jqsj + CLZ;

                //crc异或校验
                String sljz = convertHexToString(crcjyzfc);
                byte b[] = hexStringToByteArray(sljz);
                String crcjyz = getXORCheck(b);//crc校验值

                //拼接命令；
                Pjhfwzml = crcjyzfc + crcjyz + "0D";
                //发送命令
                sjsc(port, sCom, Pjhfwzml);
            }
        }
    }

    /**
     * 查询组分测量值
     *
     * @param cmpName
     * @return
     */
    public static String getC(String cmpName) {
        try {
            History history = new History(context);
            List<Map> hisMap = history.getLastHistoryFlowData(cmpName, context.getString(R.string.ZY));
            if (getPublicConfigData("CAL_DATA_UPDATE").equals("true")) {
                hisMap = getLastHistoryFlowData(cmpName, null);
            }
            if (hisMap.size() > 0) {
                C = (hisMap.get(0).get("C").toString());
                C += ",";
                C += (hisMap.get(0).get("time").toString());
            } else {
                C = "0.0000000,0000-00-00 00:00:00";
            }
        } catch (Exception e) {
            C = "0.0000000,0000-00-00 00:00:00";
        }
        return C;
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
            SendManager.SendCmd("IO" + "_" + (!IOBoardUsed ? "深圳协议_1_0" : "深圳协议_09_" + (sCom == 3 ? "08" : (sCom == 1 ? "07" : "09"))), port, 1, 1000, ml.getBytes());
        }
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
                BufferedWriter bw = new BufferedWriter(new FileWriter(dirPath + path + "shenzhen.txt"));
                bw.write("CODcr,1#" + "\r\n" + "NH3N,2#" + "\r\n" + "TP,3#");
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
     * 16进制转ASCII
     *
     * @param hex
     * @return
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
     * crc异或校验
     *
     * @param b
     * @return
     */
    private static String getXORCheck(byte[] b) {

        char x = 0;
        for (int i = 0; i < b.length; i++)
            x ^= b[i];

        return String.format("%x", (int) x);
    }

    /**
     * 16进制转byte
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
