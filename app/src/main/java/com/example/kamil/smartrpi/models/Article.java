package com.example.kamil.smartrpi.models;

import java.util.Random;

public class Article {
    private String mTitle;
    private String mContent;

    // statyczne tablice, na podstawie których zostaną uzupełnione obiekty artykułów
    private static String[] sTitles = {"Temperature sensor #1", "Camera #1"};

    private static String[] sContents = {"26.233", " "};

    public Article() {
        Random random = new Random();

        // ustawiamy losowy tytuł i treść artykułu
        mTitle = sTitles[random.nextInt(sTitles.length)];
        mContent = sContents[random.nextInt(sContents.length)];
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }
}