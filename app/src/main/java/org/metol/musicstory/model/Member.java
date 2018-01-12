package org.metol.musicstory.model;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class Member {
    private String uid;
    private String name;
    private String gender;
    private String email;
    private String birthDate;
    private Integer edm;
    private Integer status;

    public Member(){

    }

    public Member(String uid, String name, String gender, String email, String birthDate, Integer edm, Integer status) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
        this.edm = edm;
        this.status = status;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}