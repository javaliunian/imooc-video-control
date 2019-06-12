package com.imooc.utils;

/**
 * @author daile.sun
 * @date 2019/1/7
 */

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier
{
    public MyHostnameVerifier()
    {

    }

    @Override
    public boolean verify(String hostname, SSLSession session)
    {
        return true;
    }
}

