package org.metol.musicstory.model.KKBOX.Search;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class Search {
    private SearchSummary summary;

    private SearchTracks tracks;

    private SearchArtists artists;

    private SearchAlbums albums;

    private SearchPaging paging;

    public SearchSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (SearchSummary summary)
    {
        this.summary = summary;
    }

    public SearchTracks getTracks ()
    {
        return tracks;
    }

    public void setTracks (SearchTracks tracks)
    {
        this.tracks = tracks;
    }

    public SearchArtists getArtists ()
    {
        return artists;
    }

    public void setArtists (SearchArtists artists)
    {
        this.artists = artists;
    }

    public SearchAlbums getAlbums ()
    {
        return albums;
    }

    public void setAlbums (SearchAlbums albums)
    {
        this.albums = albums;
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
        return "ClassPojo [summary = "+summary+", tracks = "+tracks+", paging = "+paging+"]";
    }
}
