package com.yzlm.cyl.cfragment.Config.Component.ParCfg;

import android.content.Context;

import com.yzlm.cyl.cfragment.Config.Component.CfgTool.Config;
import com.yzlm.cyl.cfragment.Config.MyDBHelper.CompoentHelper.ConfigDBHelper;

/**
 * Created by caoyiliang on 2016/11/15.
 */

public class DoSample extends Config {
    private void InitDoSample() {
        configMap.put("isAuto", "false");
        configMap.put("LXCL", null);
        configMap.put("ZQCL", null);
        configMap.put("ZDJZ", "false");
        configMap.put("ZDQX", "false");
        configMap.put("lxclh", "0");
        configMap.put("lxclm", "2");
        configMap.put("zqclh", null);
        configMap.put("zdjzt", "0");
        configMap.put("zdjzh", "0");
        configMap.put("zdqxh", "1");
        configMap.put("C0", "0");
        configMap.put("C1", "0");
        configMap.put("C2", "0");
        configMap.put("C3", "0");
        configMap.put("C4", "0");
        configMap.put("C5", "0");
        configMap.put("zdqxTimes", "0");        //自动清洗次数
        configMap.put("zdqxTimer", "0");        //自动清洗间隔(连续多少秒处于等待状态，才去执行自动清洗)
        configMap.put("zdzyTimer", "0");
        configMap.put("ycjzFlag", "false");
        configMap.put("zdjzOnceFlag", "false");

        configMap.put("runningMode", "3");          // 1 在线 2 离线 3 维护
        configMap.put("meaMode", "4");              // 1 连续测量模式  2、周期测量模式  3、定点模式  4、受控模式  、手动模式
        configMap.put("doJobRunningMode", "");      // 设备测量的时候记录当前数据运行的模式

        configMap.put("zqclFlow", "");               // 周期测量启动流程， null 则默认启动 做样

        // 校准可选择再启动量程
        configMap.put("PreCalSelectRange", "");     // 手动校准前使用量程
        configMap.put("CalSelectRange", "");        // 手动校准的时候可以选择校准后量程

        configMap.put("KDHCManualRangeSwitch", "close");     // 跨度核查执行手动量程启动开关
        configMap.put("KDHCManualRange", "1");               // 跨度核查执行量程

        configMap.put("calibrationFlag", "false");           //在校准中是否开启校准失败功能
        configMap.put("errorShieldTable", "0");                  //报错屏蔽设置表 这里面放的是报错号,里面有什么报错号,就说明屏蔽什么报错号

        configMap.put("ZDCX", "false");     // 高指自动冲洗，（做样之后测量值超过设定值，执行一次冲洗，优先级在校准之后）
        configMap.put("autoWashLimitValue", "8");
        configMap.put("autoWashDoOnceFlag", "false");   //执行一次冲洗

        configMap.put("interferenceFlag", "false");           //是否开启干扰判断功能

        configMap.put("autoCalFlowFlag", "false");           //是否自动启动校准标志

        configMap.put("ZNXJ", "false");     // 智能消解开关

        configMap.put("historyTimeFlag", "0");     // 历史记录时间   0：启动时间  1：采样时间

        configMap.put("readHistoryDataFlag", "true");     // 读取一次数据，用于提前显示吸光度不存储，待空闲之后读取数据进行存储
        configMap.put("nowHistoryDataAEn", "");     // 当前测量值的吸光度（在比色测量的时候显示）

        configMap.put("jzRange", "1");          // 1 主量程 2 量程1

        configMap.put("EnOrAIsShow", "1");     //  0:都不显示  1:显示光强能量  2:显示吸光度

        configMap.put("SWDX_D", "");//生物毒性 D值
    }


    public DoSample(Context context, String point) {
        configDBHelper = ConfigDBHelper.getInstance(context);
        this.point = point;
        InitDoSample();
        syncDB();
    }
}
