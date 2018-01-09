package org.metol.musicstory.util;

import org.metol.musicstory.model.KKBOX.Albums;
import org.metol.musicstory.model.KKBOX.Artists;
import org.metol.musicstory.model.KKBOX.Search.Search;
import org.metol.musicstory.model.KKBOX.Tracks;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public interface ApiService {
    @GET
    Call<Search> getKKBOXSearch(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Tracks> getKKBOXTracks(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Albums> getKKBOXAlbums(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Artists> getKKBOXArtists(@Url String url, @QueryMap Map<String, String> params);
}
