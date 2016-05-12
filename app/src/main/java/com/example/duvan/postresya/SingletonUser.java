package com.example.duvan.postresya;

/**
 * Created by duvan on 12/05/16.
 */
public class SingletonUser {

    private static  SingletonUser instance=null;
    private String user;



    private String password;
    public SingletonUser(){

    }

    public static SingletonUser getInstance(){
        if(instance==null){
            instance=new SingletonUser();
        }
        return instance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
