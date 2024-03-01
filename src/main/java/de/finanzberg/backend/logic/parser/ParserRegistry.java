package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;

import java.util.HashMap;
import java.util.Map;

public class ParserRegistry {

    private final Map<String, AbstractParser> parsers = new HashMap<>();

    public ParserRegistry(Finanzberg finanzberg) {
        parsers.put("CSV.ApoBank", new CSVApoBank(finanzberg));
    }

    public AbstractParser get(String type) {
        return parsers.get(type);
    }
}
