package osa.ora.customer.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import osa.ora.DBConnection;
import osa.ora.customer.exception.JsonMessage;
import osa.ora.beans.Accounts;

public class CustomerAccountsPersistence {

	private Connection conn = null;

	public CustomerAccountsPersistence() {
	}
	private Connection getConnection(){
		conn = DBConnection.getInstance().getConnection();
		return conn;
	}
	public JsonMessage save(Accounts account) {
		String insertTableSQL = "INSERT INTO customer_accounts "
				+ "(customer_id, account_no, type) "
				+ "VALUES(?,?,?)";

		try (PreparedStatement preparedStatement = getConnection()
				.prepareStatement(insertTableSQL)) {

			preparedStatement.setLong(1, account.getId());
			preparedStatement.setString(2, account.getAccountNumber());
			preparedStatement.setString(3, account.getAccountType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			return new JsonMessage("Error", "Customer Account add failed: "
					+ e.getMessage());
		} catch (Exception e) {
			return new JsonMessage("Error", "Customer Account add failed: "
					+ e.getMessage());
		}
		return new JsonMessage("Success", "Customer Account add succeeded...");
	}

	public JsonMessage delete(long id, String account_no) {
		String deleteRowSQL = "DELETE FROM customer_accounts WHERE customer_id=? and account_no=?";
		try (PreparedStatement preparedStatement = getConnection()
				.prepareStatement(deleteRowSQL)) {
			preparedStatement.setLong(1, id);
			preparedStatement.setString(2, account_no);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			return new JsonMessage("Error", "Customer Account delete failed: "
					+ e.getMessage());
		} catch (Exception e) {
			return new JsonMessage("Error", "Customer Account delete failed: "
					+ e.getMessage());
		}
		return new JsonMessage("Success", "Customer Account delete succeeded...");
	}

	public Accounts[] findAll() {
		String queryStr = "SELECT * FROM customer_accounts";
		return this.query(queryStr);
	}

	public Accounts[] findbyId(long id) {
		String queryStr = "SELECT * FROM customer_accounts WHERE customer_id=" + id;
		Accounts accounts[] = this.query(queryStr);
		return accounts;
	}

	public Accounts[] query(String sqlQueryStr) {
		ArrayList<Accounts> cList = new ArrayList<>();
		try (PreparedStatement stmt = getConnection().prepareStatement(sqlQueryStr)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				cList.add(new Accounts(rs.getLong("CUSTOMER_ID"), rs
						.getString("ACCOUNT_NO"), rs.getString("TYPE")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cList.toArray(new Accounts[0]);
	}
}
