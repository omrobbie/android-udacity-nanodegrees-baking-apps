package com.omrobbie.bakingapp;

import org.junit.Test;

import com.omrobbie.bakingapp.util.ContentTypeUtils;

import static org.junit.Assert.assertEquals;

public class ContentTypeTest {
    @Test
    public void content_isCorrect() throws Exception {
        assertEquals(true, ContentTypeUtils.isVideo("video/mp4"));
        assertEquals(true, ContentTypeUtils.isImage("image/jpeg"));
    }
}
