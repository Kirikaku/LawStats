package com.unihh.lawstats.backend.repositories;

public class SearchFormatter {

    public String[] formatString(String string){
        String editedString = string;
        //Delete all dots
        editedString = editedString.replace(".", "");

        //To Lowercase
        editedString = editedString.toLowerCase();

        //No whitespaces
        return editedString.split(" ");
    }
}
