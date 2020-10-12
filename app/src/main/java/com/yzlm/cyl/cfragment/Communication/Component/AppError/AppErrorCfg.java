package com.yzlm.cyl.cfragment.Communication.Component.AppError;


import java.util.ArrayList;
import java.util.List;

/**
 * *******************************************
 * 项目名称：water
 *
 * @Author zwj
 * 创建时间：2019/6/19   15:44
 * 用途   报错配置，若配置文件配置了相同已文件为准
 * *******************************************
 **/
public class AppErrorCfg {

    private List<AppError> NoFileCfgError = new ArrayList<>();

    public AppErrorCfg() {

        NoFileCfgError.add(new AppError(541, "未检测到液体", "未检测到液体", "信息", false, false, true));

        NoFileCfgError.add(new AppError(570, "测量超量程", "超量程一测量范围", "信息", false, false, true));
        NoFileCfgError.add(new AppError(571, "测量超量程", "超量程二测量范围", "信息", false, false, true));
        NoFileCfgError.add(new AppError(572, "测量超量程", "超量程三测量范围", "信息", false, false, true));

        NoFileCfgError.add(new AppError(586, "清空历史记录", "清空历史记录", "其他", false, false, true));
        NoFileCfgError.add(new AppError(587, "清空校准记录", "清空校准记录", "其他", false, false, true));
        NoFileCfgError.add(new AppError(588, "清空报警记录", "清空报警记录", "其他", false, false, true));
        NoFileCfgError.add(new AppError(589, "更新配置文件", "更新配置文件", "其他", false, false, true));

        NoFileCfgError.add(new AppError(592, "更新测控板", "更新测控板", "其他", false, false, true));
        NoFileCfgError.add(new AppError(593, "变更配置文件", "变更配置文件", "其他", false, false, true));
        NoFileCfgError.add(new AppError(594, "体积校准", "体积校准", "运维", false, false, true));
        NoFileCfgError.add(new AppError(595, "新增测量点", "新增测量点", "运维", false, false, true));

        NoFileCfgError.add(new AppError(615, "参比干扰", "参比干扰", "警告", false, false, true));

        NoFileCfgError.add(new AppError(618, "定点计划重启HMI", "定点计划重启HMI", "其他", false, false, true));

        NoFileCfgError.add(new AppError(619, "删除测量点", "删除测量点", "运维", false, false, true));

        NoFileCfgError.add(new AppError(622, "加标回收", "加标回收", "运维", false, false, true));
        NoFileCfgError.add(new AppError(666, "菌液已损坏", "菌液已损坏", "警告", false, false, true));//2020年8月19日11:48:18
    }

    public List<AppError> getNoFileCfgError() {
        return NoFileCfgError;
    }
}

