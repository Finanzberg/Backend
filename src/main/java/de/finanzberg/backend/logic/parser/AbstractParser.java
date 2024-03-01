package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;

import java.util.List;

public abstract class AbstractParser {

    protected final Finanzberg finanzberg;

    public AbstractParser(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
    }

    public abstract List<BankStatement> parseStatements(String csv);
}
