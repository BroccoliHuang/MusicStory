package org.metol.musicstory.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class MusicStory implements Parcelable {
    //如果不get沒有被使用過Firestore就不會新增該欄位
    public ArrayList<Object> pleaseAddGetForFirebaseBug(){
        ArrayList<Object> al = new ArrayList();
        al.add(getArtistId());
        al.add(getArtistName());
        al.add(getAlbumId());
        al.add(getAlbumName());
        al.add(getSongId());
        al.add(getSongName());
        al.add(getCoverUrl());
        al.add(getStoryTitle());
        al.add(getStoryContent());
        al.add(getUid());
        al.add(getName());
        al.add(getStoryDate());
        al.add(getCreateTime());
        al.add(getLocation());
        al.add(getLongitude());
        al.add(getLatitude());
        al.add(getTag());
        return al;
    }

    private String artistId;
    private String artistName;
    private String albumId;
    private String albumName;
    private String songId;
    private String songName;
    private String coverUrl;
    private String storyTitle;
    private String storyContent;
    private String uid;
    private String name;
    private String storyDate;
    private String createTime;
    private String location;
    private String longitude;//經度
    private String latitude;//緯度
    private ArrayList<String> tag = new ArrayList();

    public MusicStory(){

    }

    public MusicStory(String artistId, String artistName, String albumId, String albumName, String songId, String songName, String coverUrl, String storyTitle, String storyContent, String uid, String name, String storyDate, String createTime, String location, String longitude, String latitude, ArrayList<String> tag){
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.songId = songId;
        this.songName = songName;
        this.coverUrl = coverUrl;
        this.storyTitle = storyTitle;
        this.storyContent = storyContent;
        this.uid = uid;
        this.name = name;
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
        coverUrl = parcel.readString();
        storyTitle = parcel.readString();
        storyContent = parcel.readString();
        uid = parcel.readString();
        name = parcel.readString();
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
        dest.writeString(coverUrl);
        dest.writeString(storyTitle);
        dest.writeString(storyContent);
        dest.writeString(uid);
        dest.writeString(name);
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
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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