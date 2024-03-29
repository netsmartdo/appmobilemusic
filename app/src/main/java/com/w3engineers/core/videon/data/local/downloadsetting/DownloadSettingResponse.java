package com.w3engineers.core.videon.data.local.downloadsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownloadSettingResponse {

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private DownloadSetting mDownloadSetting;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DownloadSetting getDownloadSetting() {
        return mDownloadSetting;
    }

    public void setDownloadSetting(DownloadSetting downloadSetting) {
        mDownloadSetting = downloadSetting;
    }
}
