package org.metol.musicstory.model;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class Setting {
    private String version;
    private boolean force_update;
    private String announcement_title;
    private String announcement_content;

    public Setting(){

    }

    public Setting(String version, boolean force_update, String announcement_title, String announcement_content) {
        this.version = version;
        this.force_update = force_update;
        this.announcement_title = announcement_title;
        this.announcement_content = announcement_content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isForce_update() {
        return force_update;
    }

    public void setForce_update(boolean force_update) {
        this.force_update = force_update;
    }

    public String getAnnouncement_title() {
        return announcement_title;
    }

    public void setAnnouncement_title(String announcement_title) {
        this.announcement_title = announcement_title;
    }

    public String getAnnouncement_content() {
        return announcement_content;
    }

    public void setAnnouncement_content(String announcement_content) {
        this.announcement_content = announcement_content;
    }
}