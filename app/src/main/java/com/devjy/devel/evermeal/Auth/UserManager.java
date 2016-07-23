package com.devjy.devel.evermeal.Auth;

import com.devjy.devel.evermeal.Utils.PrefHolder;

/**
 * Created by devel on 2016-07-14.
 */
public class UserManager
{
    public static UserInfo UserInfo;

    public static PrefHolder Holder;

    public static void SetUserInfo(UserInfo userInfo)
    {
        UserManager.UserInfo = userInfo;

        assert Holder != null;

        Holder.BeginTransaction();
        Holder.setValue("id", userInfo.ID);
        Holder.setValue("name", userInfo.Name);
        Holder.setValue("email", userInfo.Email);
        Holder.setValue("gender", userInfo.Gender.toString());
        Holder.EndTransaction();
    }

    public static void SetPrefHolder(PrefHolder holder)
    {
        UserManager.Holder = holder;
    }

    public static boolean HasUserData()
    {
        if (Holder != null)
            return Holder.getValue("id", "").length() > 0;
        return false;
    }

    public static boolean IsLoggedIn()
    {
        return UserManager.UserInfo != null;
    }

    public static void LoadUserData()
    {
        SetUserInfo(new UserInfo(
                Holder.getValue("id", ""),
                Holder.getValue("name", ""),
                Holder.getValue("email", ""),
                Gender.fromString(Holder.getValue("gender", "-1"))
        ));
    }
}