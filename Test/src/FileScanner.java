import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileScanner{
    public void CSV(String path){
        String csvFile = path;
        String line;
        String cvsSplitBy = ";";
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        List<Map<String, String>> transactions = new ArrayList<>();
        Map<String, Integer> headerMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            // Lese die Header-Zeile und speichere die Position der Spalten
            String[] headers = br.readLine().split(cvsSplitBy);
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i], i);
            }

            while ((line = br.readLine()) != null) {

                String[] data = line.split(cvsSplitBy);

                Date buchungsdatum = dateFormat.parse(data[headerMap.get("Buchungsdatum")]);
                Date valutadatum = dateFormat.parse(data[headerMap.get("Valutadatum")]);
                String auftragsnummer = data[headerMap.get("Auftragsnummer")];
                String text = data[headerMap.get("Text")];
                double betrag = Double.parseDouble(data[headerMap.get("Betrag (EUR)")].replace(",", "."));
                double saldo = Double.parseDouble(data[headerMap.get("Saldo (EUR)")].replace(",", "."));

                Map<String, String> transaction = new HashMap<>();
                transaction.put("Buchungsdatum", dateFormat.format(buchungsdatum));
                transaction.put("Valutadatum", dateFormat.format(valutadatum));
                transaction.put("Auftragsnummer", auftragsnummer);
                transaction.put("Text", text);
                transaction.put("Betrag (EUR)", String.valueOf(betrag));
                transaction.put("Saldo (EUR)", String.valueOf(saldo));

                transactions.add(transaction);
            }

            for (Map<String, String> transaction : transactions) {
                System.out.println(transaction);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

}