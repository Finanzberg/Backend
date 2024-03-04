package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.BankStatementCategory;
import de.finanzberg.backend.db.BankStatementName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVSparkasse extends AbstractParser {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    String bankName = "Sparkasse";

    public CSVSparkasse(Finanzberg finanzberg) {
        super(finanzberg);
    }


    @Override
    public List<BankStatement> parseStatements(String csv) {
        List<BankStatement> bankStatements = new ArrayList<>();
        List<String> csvLine = new ArrayList<>(List.of(csv.split("\n")));
        csvLine.remove(0);
        for (String line : csvLine) {
            String[] values = line.split(";");
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].substring(1, values[i].length() - 1);
            }
            double withdrawal = 0;
            double deposit = 0;
            values[8] = values[8].replace(",", ".");
            if (values[8].startsWith("-")) {
                withdrawal = Double.parseDouble(values[8]);
            } else {
                deposit = Double.parseDouble(values[8]);
            }
            try {
                bankStatements.add(new BankStatement(
                        finanzberg,
                        -1,
                        bankName,
                        DATE_FORMAT.parse(values[2]).toInstant(),
                        values[3],
                        withdrawal,
                        deposit,
                        0,
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
