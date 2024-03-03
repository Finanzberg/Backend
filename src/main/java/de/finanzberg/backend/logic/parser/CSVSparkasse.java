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

public class CSVSparkasse extends AbstractParser{

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    String bankName = "Sparkasse";

    public CSVSparkasse(Finanzberg finanzberg) {
        super(finanzberg);
    }


    @Override
    public List<BankStatement> parseStatements(String csv) {
        List<BankStatement> bankStatements = new ArrayList<>();
        List<String> csvLine = new ArrayList<>(List.of(csv.split("\n")));
        csvLine.remove(1);
        for (String line : csvLine) {
            String[] values = line.split(";");
            for (String value : values) {
                value = value.substring(1, value.length() - 1);
            }
            double withdrawal = 0;
            double deposit = 0;
            if (values[9].startsWith("-")){
                withdrawal = Double.parseDouble(values[9]);
            }else{
                deposit = Double.parseDouble(values[9]);
            }
            try {
                bankStatements.add(new BankStatement(
                        finanzberg,
                        -1,
                        bankName,
                        DATE_FORMAT.parse(values[3]).toInstant(),
                        values[4],
                        withdrawal,
                        deposit,
                        Double.parseDouble(values[4]),
                        BankStatementName.analyseName(values[4]).toString(),
                        BankStatementCategory.analyseCategory(values[4])
                ));
            } catch (ParseException exception) {
                throw new RuntimeException(exception);
            }

        }
        return bankStatements;
    }


}
