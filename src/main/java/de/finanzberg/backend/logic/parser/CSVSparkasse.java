package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.BankStatementCategory;

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
        /*
        // TODO: 3 Mar 2024 add csv value places
        List<String> csvLine = new ArrayList<>(List.of(csv.split("\n")));
        csvLine.remove(1);
        for (String line : csvLine) {
            String[] values = line.split(";");
            double withdrawal = 0;
            double deposit = 0;
            if (values[x].startsWith("-")){
                withdrawal = Double.parseDouble(values[x]);
            }else{
                deposit = Double.parseDouble(values[x]);
            }
            // TODO: 3 Mar 2024 make analysedName
            String analysedName = "";

            try {
                bankStatements.add(new BankStatement(
                        finanzberg,
                        Integer.parseInt(values[x]),
                        bankName,
                        DATE_FORMAT.parse(values[x]).toInstant(),
                        values[x],
                        withdrawal,
                        deposit,
                        Double.parseDouble(values[x]),
                        analysedName,
                        BankStatementCategory.analyseCategory(values[x])
                ));
            } catch (ParseException exception) {
                throw new RuntimeException(exception);
            }

        }
        */
        return bankStatements;
    }


}
