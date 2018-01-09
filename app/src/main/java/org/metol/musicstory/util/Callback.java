package org.metol.musicstory.util;

import org.json.JSONObject;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Callback {
    public interface OkHttp {
        void onSuccess(JSONObject jsonObject);
        void onFailed(String return_code, String return_description);
        void onNoData();
    }

    public interface LoadMore{
        void onLoadMore(Object object);
    }

    public interface API{
        void onSuccess(Object object);
        void onFailed(String reason);
    }
}
