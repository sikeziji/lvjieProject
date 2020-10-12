package com.yzlm.cyl.cfragment.Content.Component.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zwj on 2017/7/10.
 */

public class MeaParTable  {
    public Map<String,MeaParCfg> MeaParMap = new HashMap<>();

    public void Add(String name,MeaParCfg cfg){
        MeaParMap.put(name,cfg);
    }
    public MeaParCfg Get(String name){

        return MeaParMap.get(name);
    }
}
