package de.finanzberg.backend.db;

public enum BankStatementName {

    //BASIC_NEEDS
    Aldi,
    Edeka,
    Norma,
    Rewe,
    Spar,
    Tesco,
    Lidl,
    Netto,
    Kaufland,
    Penny,
    Hofer,
    tegut,

    //MOBILITY
    DB,
    RMV,
    Mobilität,
    Mobil,
    Airline,



    SHOPPING,
    HEALTH,
    FREETIME,
    RENT,
    INSURANCE,
    PERSONAL_PAYMENT,
    SUBSCRIPTIONS,
    OTHER,

    // INCOME
    SALARY,
    UNEMPLOYMENT_BENEFIT,
    CHILD_BENEFIT;

    public static BankStatementName analyseName(String description){
        //// TODO: 3 Mar 2024 finnish 
        if (description.contains("Aldi")) return BankStatementName.Aldi;
        if (description.contains("Edeka")) return BankStatementName.Edeka;
        if (description.contains("Norma")) return BankStatementName.Norma;
        if (description.contains("Rewe")) return BankStatementName.Rewe;
        if (description.contains("Spar")) return BankStatementName.Spar;
        if (description.contains("Tesco")) return BankStatementName.Tesco;
        if (description.contains("Lidl")) return BankStatementName.Lidl;
        if (description.contains("Netto")) return BankStatementName.Netto;
        if (description.contains("Kaufland")) return BankStatementName.Kaufland;
        if (description.contains("Penny")) return BankStatementName.Penny;
        if (description.contains("Hofer")) return BankStatementName.Hofer;
        if (description.contains("tegut")) return BankStatementName.tegut;


        if (description.contains("Gehalt")) return BankStatementName.SALARY;
        if (description.contains("Lohn")) return BankStatementName.SALARY;
        if (description.contains("Entgelt")) return BankStatementName.SALARY;
        if (description.contains("Einkommen")) return BankStatementName.SALARY;
        if (description.contains("Aufwandsenschädigung")) return BankStatementName.SALARY;
        if (description.contains("Besoldung")) return BankStatementName.SALARY;
        if (description.contains("Bezug")) return BankStatementName.SALARY;
        if (description.contains("Bezüge")) return BankStatementName.SALARY;
        if (description.contains("Gage")) return BankStatementName.SALARY;
        if (description.contains("Gratifikation")) return BankStatementName.SALARY;
        if (description.contains("Heuer")) return BankStatementName.SALARY;
        if (description.contains("Honorar")) return BankStatementName.SALARY;
        if (description.contains("Löhnung")) return BankStatementName.SALARY;
        if (description.contains("Salair")) return BankStatementName.SALARY;
        if (description.contains("Salär")) return BankStatementName.SALARY;

        if (description.contains("Arbeitslosengeld")) return BankStatementName.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Arbeitslosen")) return BankStatementName.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Stempel")) return BankStatementName.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Stütze")) return BankStatementName.UNEMPLOYMENT_BENEFIT;
        if (description.contains("ALG")) return BankStatementName.UNEMPLOYMENT_BENEFIT;

        if (description.contains("Kindergeld")) return BankStatementName.CHILD_BENEFIT;
        if (description.contains("Kinder")) return BankStatementName.CHILD_BENEFIT;
        if (description.contains("Familienbeihilfe")) return BankStatementName.CHILD_BENEFIT;


        if (description.contains("DB")) return BankStatementName.DB;
        if (description.contains("RMV")) return BankStatementName.RMV;
        if (description.contains("Mobilität")) return BankStatementName.Mobilität;
        if (description.contains("Mobil")) return BankStatementName.Mobil;
        if (description.contains("Airline")) return BankStatementName.Airline;

        if (description.contains("H&M")) return BankStatementName.SHOPPING;
        if (description.contains("AliExpress")) return BankStatementName.SHOPPING;
        if (description.contains("AmazonShopping")) return BankStatementName.SHOPPING;
        if (description.contains("SHEIN")) return BankStatementName.SHOPPING;
        if (description.contains("Primark")) return BankStatementName.SHOPPING;
        if (description.contains("Peek")) return BankStatementName.SHOPPING;


        if (description.contains("Apotheke")) return BankStatementName.HEALTH;

        if (description.contains("Lochmühle")) return BankStatementName.FREETIME;
        if (description.contains("Starbucks")) return BankStatementName.FREETIME;

        if (description.contains("Miete")) return BankStatementName.RENT;
        if (description.contains("Bestand")) return BankStatementName.RENT;

        if (description.contains("Versicherung")) return BankStatementName.INSURANCE;
        if (description.contains("Rente")) return BankStatementName.INSURANCE;

        if (description.contains("PayPal")) return BankStatementName.PERSONAL_PAYMENT;
        if (description.contains("David")) return BankStatementName.PERSONAL_PAYMENT;
        if (description.contains("Jan")) return BankStatementName.PERSONAL_PAYMENT;

        if (description.contains("Netflix")) return BankStatementName.SUBSCRIPTIONS;
        if (description.contains("AmazonPrime")) return BankStatementName.SUBSCRIPTIONS;
        if (description.contains("Disney")) return BankStatementName.SUBSCRIPTIONS;
        if (description.contains("Spotify")) return BankStatementName.SUBSCRIPTIONS;
        if (description.contains("Duolingo")) return BankStatementName.SUBSCRIPTIONS;

        if (description.contains("PayPal (Europe) S.a.r.l. et. Cie, S.C.A.")) return BankStatementName.OTHER;
        if (description.contains("Sparkasse")) return BankStatementName.OTHER;
        if (description.contains("Bank")) return BankStatementName.OTHER;





        return null;
    }
}
