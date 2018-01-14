package org.metol.musicstory.model;

/**
 * Created by GeneHsieh on 2017/4/24.
 */

public class BroadCastEvent {
    public enum BroadCastType {
        SEARCH_ACTIVITY_SHOW_SNACK_BAR,
        MY_STORY_ACTIVITY_SHOW_SNACK_BAR,
        MAIN_ACTIVITY_REFRESH_AFTER_LOGIN_OR_LOGOUT,
        FINISH
    }

    private Object mData;
    private BroadCastType mEventType;

    public BroadCastEvent(BroadCastType broadCastType, Object data) {
        this.mEventType = broadCastType;
        this.mData = data;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object mData) {
        this.mData = mData;
    }

    public BroadCastType getEventType() {
        return mEventType;
    }

    public void setEventType(BroadCastType mEventType) {
        this.mEventType = mEventType;
    }
}
