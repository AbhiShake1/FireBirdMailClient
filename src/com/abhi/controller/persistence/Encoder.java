package com.abhi.controller.persistence;

import java.util.Base64;

public class Encoder {

    private final Base64.Encoder encoder = Base64.getEncoder();
    private final Base64.Decoder decoder = Base64.getDecoder();

    public String encode(String text){
        try{
            return encoder.encodeToString(text.getBytes());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String decode(String text){
        try{
            return new String(decoder.decode(text.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
