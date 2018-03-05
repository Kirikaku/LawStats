package com.unihh.lawstats.core.mapping;

import java.text.ParseException;

public class NoDocketnumberFoundException extends ParseException {

    private static final long serialVersionUID = 2703218443322436634L;

    NoDocketnumberFoundException(String message) {
        super(message, 0);
    }
}
