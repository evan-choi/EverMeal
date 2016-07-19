package com.example.devel.evermeal.ResProvider;

import com.example.devel.evermeal.Utils.PrefHolder;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProviderManager
{
    public static PrefHolder Holder;
    public static List<ProviderInfo> Items = new ArrayList<>();

    public static void SetPrefHolder(PrefHolder holder)
    {
        ProviderManager.Holder = holder;
    }

    public static void Add(ProviderInfo info)
    {
        Items.add(info);
    }

    public static void Remove(ProviderInfo info)
    {
        Items.remove(info);
    }

    public static void Load()
    {
        Items.clear();

        Set<String> pv_ns = Holder.getValues("Prov_Name", null);
        Set<String> pv_tk = Holder.getValues("Prov_Token", null);
        Set<String> pv_ts = Holder.getValues("Prov_Types", null);

        if (pv_ns != null && pv_tk != null && pv_ns.size() == pv_tk.size())
        {
            Iterator<String> it_tk = pv_tk.iterator();
            Dictionary<String, String> d_ns = toDict(pv_ns);
            Dictionary<String, String> d_ts = toDict(pv_ts);

            while (it_tk.hasNext())
            {
                String token = it_tk.next();
                String name = d_ns.get(token.replace("=", ""));
                String t = d_ts.get(token.replace("=", ""));

                Items.add(
                        new ProviderInfo(
                                name,
                                token,
                                Integer.valueOf(t)));
            }
        }
    }

    private static Dictionary<String, String> toDict(Set<String> set)
    {
        Dictionary<String, String> result = new Hashtable<>();

        for (String v : set)
        {
            String[] d = v.split("_", 2);

            result.put(d[0], d[1]);
        }

        return result;
    }

    public static void Commit()
    {
        HashSet<String> setName = new HashSet<>();
        HashSet<String> setToken = new HashSet<>();
        HashSet<String> setType = new HashSet<>();

        for (ProviderInfo pi : Items)
        {
            String header = pi.Token.replace("=", "") + "_";

            setName.add(header + pi.Name);
            setToken.add(pi.Token);
            setType.add(header + String.valueOf(pi.Type));
        }

        Holder.setValues("Prov_Name", setName);
        Holder.setValues("Prov_Token", setToken);
        Holder.setValues("Prov_Types", setType);
    }

    public static boolean HasProvider(ProviderInfo pi)
    {
        for (ProviderInfo item : Items)
            if (item.Token.contentEquals(pi.Token))
                return true;

        return false;
    }
}