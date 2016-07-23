package com.devjy.devel.evermeal.ResProvider;


public class ProviderInfo
{
    public Boolean IsSurface = false;
    public Boolean ShowContent = false;

    public String Name;
    public String Token;
    public int Type;

    public ProviderInfo(Boolean isSurface)
    {
        this.IsSurface = isSurface;
    }

    public ProviderInfo(String name, String token, int type)
    {
        this.Name = name;
        this.Token = token;
        this.Type = type;
    }

    @Override
    public String toString()
    {
        return this.Name;
    }
}
