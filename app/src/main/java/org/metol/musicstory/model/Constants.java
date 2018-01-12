package org.metol.musicstory.model;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class Constants {
    //======================= Http Status Code ==========================
    /**
     * Numeric status code, 200: OK
     */
    public static final int HTTP_OK = 200;
    /**
     * Numeric status code, 404: Not found
     */
    public static final int HTTP_NOT_FOUND = 404;
    /**
     * Numeric status code, 500: Internal error
     */
    public static final int HTTP_SERVER_ERROR = 500;

    /**
     * Numeric status code, 501: Not implemented
     */
    public static final int HTTP_NOT_IMPLEMENTED = 501;



    public static final int CATEGORY_ALL                        = 0;
    public static final int CATEGORY_SHEET                      = 1;

    public static final int RECYCLER_VIEW_TYPE_ITEM             = 0;
    public static final int RECYCLER_VIEW_TYPE_LOADING          = 1;
    public static final int RECYCLER_VIEW_TYPE_CATEGORY         = 2;
    public static final int RECYCLER_VIEW_TYPE_SEARCH_KEYWORD   = 3;

    public static final int RECYCLER_VIEW_TYPE_MY_STORY         = 0;

    public static final String ARGUMENTS_CATEGORY               = "ARGUMENTS_CATEGORY";
    public static final String ARGUMENTS_MUSICSTORY             = "ARGUMENTS_MUSICSTORY";
    public static final String ARGUMENTS_KEYWORD                = "ARGUMENTS_KEYWORD";
    public static final String ARGUMENTS_TYPE                   = "ARGUMENTS_TYPE";
    public static final String ARGUMENTS_STORY_ID               = "ARGUMENTS_STORY_ID";
    public static final String ARGUMENTS_ANNOUNCEMENT_TITLE     = "ARGUMENTS_ANNOUNCEMENT_TITLE";
    public static final String ARGUMENTS_ANNOUNCEMENT_CONTENT   = "ARGUMENTS_ANNOUNCEMENT_CONTENT";
}
