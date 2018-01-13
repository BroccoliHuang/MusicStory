package org.metol.musicstory.model;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

public class Member {
    //如果不get沒有被使用過Firestore就不會新增該欄位
    public ArrayList<Object> pleaseAddGetForFirebaseBug(){
        ArrayList<Object> al = new ArrayList();
        al.add(getUid());
        al.add(getName());
        al.add(getGender());
        al.add(getEmail());
        al.add(getBirthDate());
        al.add(getEdm());
        al.add(getStatus());
        return al;
    }

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