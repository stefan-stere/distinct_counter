package distinctcounter.Db;

import distinctcounter.Tools.CommonDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Db {

	public static int defaultTimeout = 3600;
	
	private static Db CURRENT_CONNECTION = null;
	
    private static ConnectionSettings connSettings = null;


	public static String escapeString(String value) {
		//return StringEscapeUtils.escapeJava(value);
		return value;
	}
	protected static int _sleepTime = 30;
	protected static int _numTries = 5;
	public Connection jdbcConnection;
	protected int _fetchTries = 0;

	public static void setCurrentConnection(ConnectionSettings connection) throws Exception{
		if(Db.CURRENT_CONNECTION == null){
			Db.CURRENT_CONNECTION = new Db(connection);
            Db.connSettings = connection;
		}
	}
	
	public static Db getCurrentConnection(){
		return Db.CURRENT_CONNECTION;
	}
	
	public static Db getCurrentConnectionNew() throws Exception{
		return new Db(Db.connSettings);
	}
	
	public static void setInitialWaitTime(int seconds) {
		_sleepTime = seconds;
	}

	public static void setTriesNum(int tries) {
		_numTries = tries;
	}

	public int setTimeout(int Seconds) throws Exception {
		return exec("SET wait_timeout = " + Seconds);
	}

	private Db(ConnectionSettings connSet) throws Exception {
		String connectionString = "jdbc:mysql://" + connSet.host
				+ ":" + connSet.port
				+ "/" + connSet.database
				+ "?autoReconnect=true";

		Class.forName("com.mysql.jdbc.Driver");
		jdbcConnection = DriverManager.getConnection(connectionString,
				connSet.username,
				connSet.password);
		setTimeout(defaultTimeout);
		
	}
	

	public ResultSet fetchAll(String query) throws SQLException {
		return jdbcConnection.createStatement().executeQuery(query);
	}
    
	public List<Object> fetchCol(String query) throws SQLException {
        List<Object> col = new ArrayList<Object>();
		ResultSet result = fetchAll(query);
        while (result.next()) {
			col.add(result.getObject(1));
		}
        
        return col;
	}

	public <K, V> HashMap<K, V> fetchPairs(String query) throws Exception {
		HashMap<K, V> pairs = null;

		ResultSet result = fetchAll(query);
		pairs = new HashMap<K, V>();
		while (result.next()) {
			pairs.put((K) result.getObject(1), (V) result.getObject(2));
		}

		return pairs;
	}

	public <V> V fetchOne(String query) throws Exception {

		ResultSet res = fetchAll(query);
		if (res.next()) {
			return (V) res.getObject(1);
		}

		return null;
	}

	public int exec(String query) throws Exception {
		Statement st = jdbcConnection.createStatement();
		return st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
	}

	public int getLastInsertId() throws Exception {
		Statement st = jdbcConnection.createStatement();
		ResultSet rs = st.getGeneratedKeys();
		if (rs.next()) {
			return rs.getInt(1);
		}

		return 0;
	}
}
