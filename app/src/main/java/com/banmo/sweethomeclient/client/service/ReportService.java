package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.pojo.Report;
import com.banmo.sweethomeclient.tool.GsonTools;
import com.banmo.sweethomeclient.tool.OkHttpTools;

import java.io.IOException;

public class ReportService {

    public static void postReport(Report report) {
        try {
            OkHttpTools.post("report", GsonTools.getGson().toJson(report));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
