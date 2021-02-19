package com.banmo.sweethomeclient.pojo;

import java.util.Date;

public class Report {
    private Integer reportid;

    private Integer userid;

    private String reason;

    private Date reportcreatetime;

    private Integer result;

    private Date resulttime;

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getReportcreatetime() {
        return reportcreatetime;
    }

    public void setReportcreatetime(Date reportcreatetime) {
        this.reportcreatetime = reportcreatetime;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getResulttime() {
        return resulttime;
    }

    public void setResulttime(Date resulttime) {
        this.resulttime = resulttime;
    }
}