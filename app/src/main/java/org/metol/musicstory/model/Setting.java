package org.metol.musicstory.model;

import android.support.annotation.Keep;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

@Keep
public class Setting {
    public String version;
    public boolean forceUpdate;
    public String announcementTitle;
    public String announcementContent;

    public Setting(){

    }

    public Setting(String version, boolean forceUpdate, String announcementTitle, String announcementContent) {
        this.version = version;
        this.forceUpdate = forceUpdate;
        this.announcementTitle = announcementTitle;
        this.announcementContent = announcementContent;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }
}