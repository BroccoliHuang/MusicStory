package org.metol.musicstory.model.KKBOX.Search;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class SearchSummary {
    private String total;

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
