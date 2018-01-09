package org.metol.musicstory.model;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class Member {
    private String address;
    private String birthDate;
    private Integer edm;
    private String email;
    private String fbId;
    private String fbName;
    private String nickname;
    private String phone;
    private String sex;
    private Integer status;

    public Member(){

    }

    public Member(String address, String birthDate, Integer edm, String email, String fbId, String fbName, String nickname, String phone, String sex, Integer status) {
        this.address = address;
        this.birthDate = birthDate;
        this.edm = edm;
        this.email = email;
        this.fbId = fbId;
        this.fbName = fbName;
        this.nickname = nickname;
        this.phone = phone;
        this.sex = sex;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birth_date) {
        this.birthDate = birth_date;
    }

    public Integer getEdm() {
        return edm;
    }

    public void setEdm(Integer edm) {
        this.edm = edm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fb_id) {
        this.fbId = fb_id;
    }

    public String getFbName() {
        return fbName;
    }

    public void setFbName(String fb_name) {
        this.fbName = fb_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nick_name) {
        this.nickname = nick_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}