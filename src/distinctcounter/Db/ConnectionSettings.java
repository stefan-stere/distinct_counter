package distinctcounter.Db;


public class ConnectionSettings {
    public String host;
    public int port;
    public String username;
    public String password;
    public String database;
    
    public ConnectionSettings(String host, String username, String password,
                              String database){
        this(host, username, password, database, 3306);
    }
    
    public ConnectionSettings(String host, String username, String password,
                              String database, int port){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }
}