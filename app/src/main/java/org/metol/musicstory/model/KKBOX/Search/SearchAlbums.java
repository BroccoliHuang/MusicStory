package org.metol.musicstory.model.KKBOX.Search;

import org.metol.musicstory.model.KKBOX.Albums;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/7.
 */

public class SearchAlbums {
    private SearchSummary summary;

    private ArrayList<Albums> data;

    private SearchPaging paging;

    public SearchSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (SearchSummary summary)
    {
        this.summary = summary;
    }

    public ArrayList<Albums> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Albums> data)
    {
        this.data = data;
    }

    public SearchPaging getPaging ()
    {
        return paging;
    }

    public void setPaging (SearchPaging paging)
    {
        this.paging = paging;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+summary+", data = "+data+", paging = "+paging+"]";
    }
}
