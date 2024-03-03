package de.finanzberg.backend.logic.parser;

import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.BankStatementCategory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CAMTv8Sparkasse extends AbstractParser{

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    String bankName = "Sparkasse";

    public CAMTv8Sparkasse(Finanzberg finanzberg) {
        super(finanzberg);
    }


    @Override
    public List<BankStatement> parseStatements(String csv) {
        List<BankStatement> bankStatements = new ArrayList<>();
        try {
            // XML-String in ein InputStream umwandeln
            ByteArrayInputStream inputStream = new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8));

            // DocumentBuilder erstellen
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML-Dokument erstellen
            Document document = builder.parse(inputStream);

            // Elemente extrahieren
            NodeList recordList = document.getElementsByTagName("BkToCstmrStmt");

            for (int i = 0; i < recordList.getLength(); i++) {
                Node recordNode = recordList.item(i);

                if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element recordElement = (Element) recordNode;

                    // Hier können Sie die gewünschten Informationen extrahieren
                    String withdrawalDeposit = recordElement.getElementsByTagName("Dpst").item(0).getTextContent();
                    String balance = recordElement.getElementsByTagName("Bal").item(0).getTextContent();
                    String description = recordElement.getElementsByTagName("NtryDtls").item(0).getTextContent();
                    String date = recordElement.getElementsByTagName("Dt").item(0).getTextContent();
                    String bankInternalId = recordElement.getElementsByTagName("BkTxCd").item(0).getTextContent();
                    String analysedName = "";


                    double withdrawal = 0;
                    double deposit = 0;
                    if (withdrawalDeposit.startsWith("-")) {
                        withdrawal = Double.parseDouble(withdrawalDeposit);
                    } else {
                        deposit = Double.parseDouble(withdrawalDeposit);
                    }

                    try {
                        bankStatements.add(new BankStatement(
                                finanzberg,
                                Integer.parseInt(bankInternalId),
                                bankName,
                                DATE_FORMAT.parse(date).toInstant(),
                                description,
                                withdrawal,
                                deposit,
                                Double.parseDouble(balance),
                                analysedName,
                                BankStatementCategory.analyseCategory(description)
                        ));
                    } catch (ParseException exception) {
                        throw new RuntimeException(exception);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bankStatements;
    }
    
}
