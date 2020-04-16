package com.nyx.fineme.helper;

import org.json.JSONException;



public abstract class PostAction {
    public  abstract void whenFinished(String status ,String response) throws JSONException;
}
