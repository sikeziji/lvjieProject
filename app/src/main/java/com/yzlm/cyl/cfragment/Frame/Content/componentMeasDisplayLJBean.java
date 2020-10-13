package com.yzlm.cyl.cfragment.Frame.Content;

import com.yzlm.cyl.cfragment.Communication.Component.AppAction.ActionTable;

/**
 * MessageBean 传输参数用到的Bean
 */
public class componentMeasDisplayLJBean {
    private String component;
    private ActionTable at;
    private String status;

    public componentMeasDisplayLJBean(String component, ActionTable at, String status) {
        this.component = component;
        this.at = at;
        this.status = status;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public ActionTable getAt() {
        return at;
    }

    public void setAt(ActionTable at) {
        this.at = at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
