package org.metol.musicstory.model.KKBOX.Search;

import android.support.annotation.Keep;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

@Keep
public class SearchSummary {
    public String total;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+"]";
    }

}
