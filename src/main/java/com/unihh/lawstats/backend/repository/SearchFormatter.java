package com.unihh.lawstats.backend.repository;

/**
 * This class formatted the search params to the repository
 */
public class SearchFormatter {

    /**
     * This method format the given string to a string array and split the string by whitespace
     */
    public String[] formatString(String string) {
        String editedString = string;
        //Delete all dots
        editedString = editedString.replace(".", "");

        //To Lowercase
        editedString = editedString.toLowerCase();

        //No whitespaces
        return editedString.split(" ");
    }
}
