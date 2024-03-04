package de.finanzberg.backend.util;

import java.util.ArrayList;
import java.util.List;

public class ShitSplit {

    public static String[] splitOutsideApostrophes(String s, char delimiter) {
        List<String> result = new ArrayList<>();
        boolean inApostrophes = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                inApostrophes = !inApostrophes;
            }
            if (c == delimiter && !inApostrophes) {
                result.add(builder.toString());
                builder = new StringBuilder();
            } else {
                builder.append(c);
            }
        }
        if (!s.isEmpty()) {
            result.add(builder.toString());
        }
        return result.toArray(new String[0]);
    }
}
