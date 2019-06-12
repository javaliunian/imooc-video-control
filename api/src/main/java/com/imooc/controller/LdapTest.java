package com.imooc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * @author daile.sun
 * @date 2019/1/3
 */
public class LdapTest {
    private static LdapContext ctx;

    @SuppressWarnings(value = "unchecked")
    public static DirContext getCtx() {
//        if (ctx != null ) {
//            return ctx;
//        }
        String account = "shinan.chen@hand-china.com"; //binddn
        String password = "b42a95cc5ceccf6a843d7f858b6d4f09";    //bindpwd
        String root = "ou=employee,dc=hand-china,dc=com"; // root
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ac.hand-china.com/" + root);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, "mail="+account );
//        env.put(Context.SECURITY_CREDENTIALS, password);
        try {
            // 链接ldap
            ctx = new InitialLdapContext(new Hashtable<>(env),null);
            System.out.println("认证成功");
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("认证失败");
        } catch (Exception e) {
            System.out.println("认证出错：");
            e.printStackTrace();
        }
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            NamingEnumeration<SearchResult> searchResultNamingEnumeration = ctx.search("","mail=jiatong.li@hand-china.com",searchControls);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return ctx;
    }

    public static void closeCtx(){
        try {
            ctx.close();
        } catch (NamingException ex) {
        }
    }



    public static void main(String[] args) {
        getCtx();
    }


}
