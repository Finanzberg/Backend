package de.finanzberg.backend.db;

public enum BankStatementCategory {

    // EXPENDITURE
    BASIC_NEEDS,
    MOBILITY,
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

    public static BankStatementCategory analyseCategory(String description) {
        if (description.contains("Gehalt")) return BankStatementCategory.SALARY;
        if (description.contains("Lohn")) return BankStatementCategory.SALARY;
        if (description.contains("Entgelt")) return BankStatementCategory.SALARY;
        if (description.contains("Einkommen")) return BankStatementCategory.SALARY;
        if (description.contains("Aufwandsenschädigung")) return BankStatementCategory.SALARY;
        if (description.contains("Besoldung")) return BankStatementCategory.SALARY;
        if (description.contains("Bezug")) return BankStatementCategory.SALARY;
        if (description.contains("Bezüge")) return BankStatementCategory.SALARY;
        if (description.contains("Gage")) return BankStatementCategory.SALARY;
        if (description.contains("Gratifikation")) return BankStatementCategory.SALARY;
        if (description.contains("Heuer")) return BankStatementCategory.SALARY;
        if (description.contains("Honorar")) return BankStatementCategory.SALARY;
        if (description.contains("Löhnung")) return BankStatementCategory.SALARY;
        if (description.contains("Salair")) return BankStatementCategory.SALARY;
        if (description.contains("Salär")) return BankStatementCategory.SALARY;

        if (description.contains("Arbeitslosengeld")) return BankStatementCategory.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Arbeitslosen")) return BankStatementCategory.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Stempel")) return BankStatementCategory.UNEMPLOYMENT_BENEFIT;
        if (description.contains("Stütze")) return BankStatementCategory.UNEMPLOYMENT_BENEFIT;
        if (description.contains("ALG")) return BankStatementCategory.UNEMPLOYMENT_BENEFIT;

        if (description.contains("Kindergeld")) return BankStatementCategory.CHILD_BENEFIT;
        if (description.contains("Kinder")) return BankStatementCategory.CHILD_BENEFIT;
        if (description.contains("Familienbeihilfe")) return BankStatementCategory.CHILD_BENEFIT;

        if (description.contains("Aldi")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Edeka")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Norma")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Rewe")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Spar")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Tesco")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Lidl")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Netto")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Kaufland")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Penny")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("Hofer")) return BankStatementCategory.BASIC_NEEDS;
        if (description.contains("tegut")) return BankStatementCategory.BASIC_NEEDS;

        if (description.contains("DB")) return BankStatementCategory.MOBILITY;
        if (description.contains("RMV")) return BankStatementCategory.MOBILITY;
        if (description.contains("Mobilität")) return BankStatementCategory.MOBILITY;
        if (description.contains("Mobil")) return BankStatementCategory.MOBILITY;
        if (description.contains("Airline")) return BankStatementCategory.MOBILITY;

        if (description.contains("H&M")) return BankStatementCategory.SHOPPING;
        if (description.contains("AliExpress")) return BankStatementCategory.SHOPPING;
        if (description.contains("AmazonShopping")) return BankStatementCategory.SHOPPING;
        if (description.contains("SHEIN")) return BankStatementCategory.SHOPPING;
        if (description.contains("Primark")) return BankStatementCategory.SHOPPING;
        if (description.contains("Peek")) return BankStatementCategory.SHOPPING;


        if (description.contains("Apotheke")) return BankStatementCategory.HEALTH;

        if (description.contains("Lochmühle")) return BankStatementCategory.FREETIME;
        if (description.contains("Starbucks")) return BankStatementCategory.FREETIME;

        if (description.contains("Miete")) return BankStatementCategory.RENT;
        if (description.contains("Bestand")) return BankStatementCategory.RENT;

        if (description.contains("Versicherung")) return BankStatementCategory.INSURANCE;
        if (description.contains("Rente")) return BankStatementCategory.INSURANCE;

        if (description.contains("PayPal")) return BankStatementCategory.PERSONAL_PAYMENT;
        if (description.contains("David")) return BankStatementCategory.PERSONAL_PAYMENT;
        if (description.contains("Jan")) return BankStatementCategory.PERSONAL_PAYMENT;

        if (description.contains("Netflix")) return BankStatementCategory.SUBSCRIPTIONS;
        if (description.contains("AmazonPrime")) return BankStatementCategory.SUBSCRIPTIONS;
        if (description.contains("Disney")) return BankStatementCategory.SUBSCRIPTIONS;
        if (description.contains("Spotify")) return BankStatementCategory.SUBSCRIPTIONS;
        if (description.contains("Duolingo")) return BankStatementCategory.SUBSCRIPTIONS;

        return BankStatementCategory.OTHER;
    }
}
