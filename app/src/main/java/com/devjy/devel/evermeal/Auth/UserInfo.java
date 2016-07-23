package com.devjy.devel.evermeal.Auth;

/**
 * Created by devel on 2016-07-14.
 */
public class UserInfo
{
    public String ID;
    public String Name;
    public String Email;
    public Gender Gender;

    public UserInfo(String id, String name, String email, Gender gender)
    {
        this.ID = id;
        this.Name = name;
        this.Gender = gender;
        this.Email = email;
    }
}