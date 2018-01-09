package org.metol.musicstory.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class MusicStory implements Parcelable {
    private String artistId;
    private String artistName;
    private String albumId;
    private String albumName;
    private String songId;
    private String songName;
    private String songUrl;
    private String coverUrl;
    private String storyTitle;
    private String storyContent;
    private String fbId;
    private String fbName;
    private String nickname;
    private String storyDate;
    private String createTime;
    private String location;
    private String longitude;//經度
    private String latitude;//緯度
    private ArrayList<String> tag = new ArrayList();

    public MusicStory(){

    }

    public MusicStory(String artistId, String artistName, String albumId, String albumName, String songId, String songName, String coverUrl, String storyTitle, String storyContent, String fbId, String fbName, String nickname, String storyDate, String createTime, String location, String longitude, String latitude, ArrayList<String> tag){
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.songId = songId;
        this.songName = songName;
        setSongUrlBySongId(this.songId);
        this.coverUrl = coverUrl;
        this.storyTitle = storyTitle;
        this.storyContent = storyContent;
        this.fbId = fbId;
        this.fbName = fbName;
        this.nickname = nickname;
        this.storyDate = storyDate;
        this.createTime = createTime;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tag = tag;
    }

    private MusicStory(Parcel parcel) {
        artistId = parcel.readString();
        artistName = parcel.readString();
        albumId = parcel.readString();
        albumName = parcel.readString();
        songId = parcel.readString();
        songName = parcel.readString();
        songUrl = parcel.readString();
        coverUrl = parcel.readString();
        storyTitle = parcel.readString();
        storyContent = parcel.readString();
        fbId = parcel.readString();
        fbName = parcel.readString();
        nickname = parcel.readString();
        storyDate = parcel.readString();
        createTime = parcel.readString();
        location = parcel.readString();
        longitude = parcel.readString();
        latitude = parcel.readString();
        parcel.readStringList(tag);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(albumId);
        dest.writeString(albumName);
        dest.writeString(songId);
        dest.writeString(songName);
        dest.writeString(songUrl);
        dest.writeString(coverUrl);
        dest.writeString(storyTitle);
        dest.writeString(storyContent);
        dest.writeString(fbId);
        dest.writeString(fbName);
        dest.writeString(nickname);
        dest.writeString(storyDate);
        dest.writeString(createTime);
        dest.writeString(location);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeStringList(tag);
    }

    public static final Parcelable.Creator<MusicStory> CREATOR = new Parcelable.Creator<MusicStory>() {
        public MusicStory createFromParcel(Parcel parcel) {
            return new MusicStory(parcel);
        }

        public MusicStory[] newArray(int size) {
            return new MusicStory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
        setSongUrlBySongId(this.songId);
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return TextUtils.isEmpty(this.songUrl)?"https://event.kkbox.com/content/song/"+songId : this.songUrl;
    }

    public void setSongUrlBySongId(String songId) {
        this.songUrl = "https://event.kkbox.com/content/song/"+songId;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(String storyDate) {
        this.storyDate = storyDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }
}