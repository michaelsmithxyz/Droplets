package com.s0lder.droplets;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class Messages {
    private static HashMap<String, MessageFormat> mapping;
    
    private Messages() {}
    
    public static String _(String message, Object ... array) {
        try {
            MessageFormat msg = mapping.get(message);
            if(msg == null) {
                return "The message format was not found!";
            }
            return msg.format(array);
        } catch(NullPointerException e) {
            return "Message have not been initialized!";
        }
    }
    
    public static String _(String message) {
        return _(message, (Object) null);
    }
    
    public static void initialize(String locale) {
        Enumeration en;
        MessageFormat format;
        Properties messageProperties = new Properties();
        InputStream in = Messages.class.getResourceAsStream("/res/" + locale + ".properties");
        mapping = new HashMap<String, MessageFormat>();
        try {
            messageProperties.load(in);
        } catch( IOException e) {
            DropletsPlugin.log.warning("The selected locale mapping could not be loaded!");
            return;
        } catch(NullPointerException e) {
            DropletsPlugin.log.warning("The selected locale mapping could not be loaded!");
            return;
        }
        en = messageProperties.propertyNames();
        while(en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String property = messageProperties.getProperty(key);
            format = new MessageFormat(property.replaceAll("&", "\u00a7").replaceAll("`", ""));
            mapping.put(key, format);
        }
    }
    
}
