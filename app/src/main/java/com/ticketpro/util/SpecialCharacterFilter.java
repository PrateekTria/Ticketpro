package com.ticketpro.util;

import android.text.InputFilter;
import android.text.Spanned;

public class SpecialCharacterFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Regular expression that matches special characters
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);

            // If the character is not alphanumeric and is not a space, reject it
            if (!Character.isLetterOrDigit(c) && c != ' ') {
                return ""; // Reject this character
            }
        }

        // If no invalid characters were found, allow the input
        return null; // Allow normal characters
    }
}
