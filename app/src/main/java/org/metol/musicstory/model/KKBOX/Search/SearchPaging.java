package org.metol.musicstory.model.KKBOX.Search;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class SearchPaging {
    private String limit;

    private String previous;

    private String next;

    private String offset;

    public String getLimit ()
    {
        return limit;
    }

    public void setLimit (String limit)
    {
        this.limit = limit;
    }

    public String getPrevious ()
    {
        return previous;
    }

    public void setPrevious (String previous)
    {
        this.previous = previous;
    }

    public String getNext ()
    {
        return next;
    }

    public void setNext (String next)
    {
        this.next = next;
    }

    public String getOffset ()
    {
        return offset;
    }

    public void setOffset (String offset)
    {
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [limit = "+limit+", previous = "+previous+", next = "+next+", offset = "+offset+"]";
    }
}
