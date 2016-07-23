package com.devjy.devel.evermeal.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class StringUtils
{
    public static boolean IsCompletionString(String str)
    {
        boolean result = true;

        for (int i = 0; i < str.length(); i++)
        {
            if (!IsValidChar(str.charAt(i)))
            {
                result = false;
                break;
            }
        }

        return result;
    }

    public static boolean IsValidChar(char ch)
    {
        return (44032 <= (int)ch && (int)ch <= 55199) ||
                (97 <= (int)ch && (int)ch <= 122) ||
                (48 <= (int)ch && (int)ch <= 57);
    }

    public static  String getStringFromInputStream(InputStream stream) throws IOException
    {
        int n = 0;
        char[] buffer = new char[1024];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}
