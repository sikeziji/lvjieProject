package com.yzlm.cyl.cfragment.Communication.Component.AppError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WL on 2017/4/8.
 */

public class ErrorTable {
    //<错误,error>
    private Map<String, AppError> ErrorMap = new HashMap<>();

    public void Add(String errorCode, AppError appError) {
        ErrorMap.put(errorCode, appError);
    }

    public AppError getErrorObj(String errorCode) {
        return ErrorMap.get(errorCode);
    }
}
