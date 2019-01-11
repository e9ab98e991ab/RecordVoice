package com.e9ab98e991ab.voice;


public interface RecordListener {
    void onComplete(String path);
    void onCancel();
}
