package com.example.rlapp.ui.utility;

import android.util.Patterns;

public class EmailValidator {

    /**
     * Validates an email address.
     *
     * @param email The email address to validate.
     * @return True if the email address is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

