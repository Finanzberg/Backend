package de.finanzberg.backend.logic;

import com.google.gson.JsonArray;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.BankStatementCategory;
import de.finanzberg.backend.db.Budget;
import de.finanzberg.backend.db.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger("DataLogic");
    private final Finanzberg finanzberg;

    public DataLogic(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
    }

    public List<BankStatement> loadStatements(User user) {
        List<BankStatement> statements = new ArrayList<>();
        try (Connection connection = finanzberg.getDBManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM bankStatement WHERE userAccount_email = ?")
        ) {
            preparedStatement.setString(1, user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                statements.add(new BankStatement(
                        finanzberg,
                        resultSet.getInt("bankInternalId"),
                        resultSet.getString("bankname"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC),
                        resultSet.getString("description"),
                        resultSet.getDouble("withdrawal"),
                        resultSet.getDouble("deposit"),
                        resultSet.getDouble("balance"),
                        resultSet.getString("analysedName"),
                        BankStatementCategory.valueOf(resultSet.getString("category")
                        )));
            }
        } catch (Exception e) {
            LOGGER.error("Error while loading statements", e);
        }
        return statements;
    }

    public JsonArray loadStatementsJson(User user) {
        List<BankStatement> statements = loadStatements(user);
        statements.sort(Comparator.comparing(BankStatement::getDate));
        JsonArray array = new JsonArray(statements.size());
        for (BankStatement statement : statements) {
            array.add(statement.toJson());
        }
        return array;
    }

    public List<Budget> loadBudgets(User user) {
        List<Budget> budgets = new ArrayList<>();
        try (Connection connection = finanzberg.getDBManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM budget WHERE userAccount_email = ?")
        ) {
            preparedStatement.setString(1, user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Budget budget = new Budget(
                        this.finanzberg,
                        resultSet.getString("name"),
                        resultSet.getInt("monthlyBalance"),
                        resultSet.getDouble("balance"),
                        resultSet.getDate("startDate").toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC)
                );
                budget.refreshBalance();
                Budget.update(user, finanzberg, budget);
                budgets.add(budget);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while loading budgets", exception);
        }
        return budgets;
    }

    public JsonArray loadBudgetsJson(User user) {
        List<Budget> budgets = loadBudgets(user);
        JsonArray array = new JsonArray(budgets.size());
        for (Budget budget : budgets) {
            array.add(budget.toJson());
        }
        return array;
    }
}
