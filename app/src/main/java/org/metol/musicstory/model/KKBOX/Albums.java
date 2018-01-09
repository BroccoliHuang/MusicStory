package org.metol.musicstory.model.KKBOX;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Albums {
    private String id;

    private String explicitness;

    private String name;

    private String release_date;

    private ArrayList<Images> images;

    private ArrayList<String> available_territories;

    private Artists artist;

    private String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getExplicitness ()
    {
        return explicitness;
    }

    public void setExplicitness (String explicitness)
    {
        this.explicitness = explicitness;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getRelease_date ()
    {
        return release_date;
    }

    public void setRelease_date (String release_date)
    {
        this.release_date = release_date;
    }

    public ArrayList<Images> getImages ()
    {
        return images;
    }

    public void setImages (ArrayList<Images> images)
    {
        this.images = images;
    }

    public ArrayList<String> getAvailable_territories ()
    {
        return available_territories;
    }

    public void setAvailable_territories (ArrayList<String> available_territories)
    {
        this.available_territories = available_territories;
    }

    public Artists getArtist()
    {
        return artist;
    }

    public void setArtist(Artists artist)
    {
        this.artist = artist;
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
        return "ClassPojo [id = "+id+", explicitness = "+explicitness+", name = "+name+", release_date = "+release_date+", images = "+images+", available_territories = "+available_territories+", artist = "+ artist +", url = "+url+"]";
    }

}
