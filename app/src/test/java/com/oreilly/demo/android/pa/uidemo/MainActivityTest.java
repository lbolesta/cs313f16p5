package com.oreilly.demo.android.pa.uidemo;

import android.os.Bundle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Patrick on 12/15/2016.
 */
public class MainActivityTest {
    @Test
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     throw new IllegalStateException ("something went wrong");
    }

}