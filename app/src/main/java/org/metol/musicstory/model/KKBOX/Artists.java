package org.metol.musicstory.model.KKBOX;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Artists {
    private String id;

    private String name;

    private ArrayList<Images> images;

    private String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public ArrayList<Images> getImages ()
    {
        return images;
    }

    public void setImages (ArrayList<Images> images)
    {
        this.images = images;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", name = "+name+", images = "+images+", url = "+url+"]";
    }

}
