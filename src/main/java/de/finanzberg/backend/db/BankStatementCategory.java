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

    public static BankStatementCategory analyseCategory(String description){
        if (description.contains("Gehalt")) return BankStatementCategory.SALARY;
        if (description.contains("Lohn")) return BankStatementCategory.SALARY;
        if (description.contains("Entgelt")) return BankStatementCategory.SALARY;
        if (description.contains("Einkommen")) return BankStatementCategory.SALARY;
        return null;
    }



    
}
