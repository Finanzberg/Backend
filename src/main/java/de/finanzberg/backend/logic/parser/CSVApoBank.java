package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.BankStatementCategory;
import de.finanzberg.backend.db.BankStatementName;
import de.finanzberg.backend.util.ShitSplit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVApoBank extends AbstractParser {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    String bankName = "ApoBank";

    public CSVApoBank(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public List<BankStatement> parseStatements(String csv) {
        List<BankStatement> bankStatements = new ArrayList<>();
        List<String> csvLine = new ArrayList<>(List.of(csv.split("\n")));
        csvLine.remove(0);
        for (String line : csvLine) {
            String[] values = ShitSplit.splitOutsideApostrophes(line, ';');
            double withdrawal = 0;
            double deposit = 0;
            if (values[4].startsWith("-")) {
                withdrawal = Double.parseDouble(values[4]);
            } else {
                deposit = Double.parseDouble(values[4]);
            }

            try {
                bankStatements.add(new BankStatement(
                        finanzberg,
                        Integer.parseInt(values[2]),
                        bankName,
                        DATE_FORMAT.parse(values[1]).toInstant(),
                        values[3],
                        withdrawal,
                        deposit,
                        Double.parseDouble(values[5]),
                        BankStatementName.analyseName(values[3]).toString(),
                        BankStatementCategory.analyseCategory(values[3])
                ));
            } catch (ParseException exception) {
                throw new RuntimeException(exception);
            }

        }
        return bankStatements;
    }
}
