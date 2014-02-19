package me.flungo.bukkit.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.flungo.myunit.MUTestCase;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Fabrizio
 */
public class MySQLDatabaseTest extends MUTestCase {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String HOST = "localhost";
    private static final int PORT = 3336;
    private static final String DATABASE = "minecraft";

    private final Plugin mockPlugin;

    public MySQLDatabaseTest(String testName) throws ClassNotFoundException, SQLException {
        super(PORT, USERNAME, PASSWORD, testName);
        createDatabase(DATABASE);
        mockPlugin = mock(Plugin.class);
        YamlConfiguration config = new YamlConfiguration();
        config.set("database.username", USERNAME);
        config.set("database.password", PASSWORD);
        config.set("database.host", HOST);
        config.set("database.port", PORT);
        config.set("database.database", DATABASE);
        when(mockPlugin.getConfig()).thenReturn(config);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getConnection method, of class MySQLDatabase.
     */
    public void testGetConnection() {
        try {
            System.out.println("getConnection");
            MySQLDatabase instance = new MySQLDatabaseImpl(mockPlugin);
            Connection expResult = null;
            Connection result = instance.getConnection();
            assertTrue("Connection not valid", result.isValid(10));
        } catch (SQLException ex) {
            fail("SQL exception: " + ex.getMessage());
        }
    }

    /**
     * Test of createTable method, of class MySQLDatabase.
     */
    public void testCreateTable() {
        System.out.println("createTable");
        MySQLDatabase instance = new MySQLDatabaseImpl(mockPlugin);
        boolean result = instance.tableExists(MySQLDatabaseImpl.TEST_TABLE);
        assertTrue(result);
    }

    /**
     * Test of tableExists method, of class MySQLDatabase.
     */
    public void testTableExists() {
        System.out.println("tableExists");
        String name = "notable";
        MySQLDatabase instance = new MySQLDatabaseImpl(mockPlugin);
        boolean expResult = false;
        boolean result = instance.tableExists(name);
        assertEquals(expResult, result);
    }

    public class MySQLDatabaseImpl extends MySQLDatabase {

        private final static String TEST_TABLE = "test";
        private final static String TEST_CREATE = "CREATE TABLE `" + TEST_TABLE + "`\n"
                + "(\n"
                + "id int,\n"
                + "field1 varchar(255),\n"
                + "field2 varchar(255),\n"
                + "field3 varchar(255),\n"
                + "field4 varchar(255)\n"
                + ");";

        public MySQLDatabaseImpl(Plugin plugin) {
            super(plugin);
        }

        @Override
        public void createTable() {
            Statement st = null;
            try {
                Connection conn = getConnection();
                st = conn.createStatement();

                if (!tableExists(TEST_TABLE)) {
                    st.executeUpdate(TEST_CREATE);
                    conn.commit();
                }
            } catch (SQLException ex) {
                //
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                } catch (SQLException ex) {
                    //
                }
            }
        }
    }

}
