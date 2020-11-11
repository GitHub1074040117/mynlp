package process.database;

import process.utils.Utils;
import study.language.chinese.word.Word;
import study.language.chinese.word.WordTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/anna_language?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
    private static final String NAME = "root";
    private static final String PASSWORD = "123";
    private static final String VOCABULARY_1 = "vocabulary_1";
    private static final String VOCABULARY_2 = "vocabulary_2";
    private static final String VOCABULARY_3 = "vocabulary_3";
    private static final String VOCABULARY_4 = "vocabulary_4";
    private static final String VOCABULARY_5 = "vocabulary_5";

    private static final int VOCABULARY_1_LENGTH = 1;
    private static final int VOCABULARY_2_LENGTH = 2;
    private static final int VOCABULARY_3_LENGTH = 3;
    private static final int VOCABULARY_4_LENGTH = 4;
    private static final int VOCABULARY_5_LENGTH = 5;

    public static final int COMPLETE = 0; // 标记数据库操作以及完成

    private Connection connection;

    public DB() {
        connection = getConn();
    }


    // 建立连接
    private static Connection getConn() {
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动程序失败！");
            e.printStackTrace();
            return null;
        }
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }
        return null;
    }

    // 关闭连接
    private static void closeDB(Connection conn, Statement stmt, ResultSet set) {
        if(set != null)
        {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeDB(conn, stmt);
    }

    // 关闭连接
    private static void closeDB(Connection conn, Statement stmt) {
        if(stmt != null)
        {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 插入
    public int insert(Word word) {
        String sql;
        Connection conn = null;
        Statement stmt = null;
        String code = word.getValue();
        // 转化当前时间Date为sql格式的字符串
        String date = Utils.toSqlDate(new Date());
        int freq = word.getFreq();
        String tableName = getTableName(word);
        sql = "INSERT INTO "+ tableName +" (word, frequency, updated_at, s, m, memory_value) VALUES ('"+ code +"', "+ freq +", '"+ date +"', "+ word.getS() +", "+ word.getM() +", "+ word.getMemoryValue() +") ";
        try {
            conn = connection;
            assert conn != null;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库插入记录失败！");
        }
        //closeDB(conn, stmt);
        return COMPLETE;
    }

    // 查询词语, 返回查询到的词, 查找不到返回空
    public Word query(Word word) {
        String sql;
        Word resWord = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        String tableName = getTableName(word);
        sql = "SELECT * FROM " + tableName + " WHERE word = '" + word.getValue() + "'";
        try {
            conn = connection;
            assert conn != null;
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                // 将查询到的数据写入Word对象中
                resWord = toWord(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //closeDB(conn, stmt, resultSet);
        return resWord;
    }

    // 获取一张词汇表
    public WordTable fetchWordTable(String table) {
        String sql;
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        sql = "SELECT * FROM " + table;
        ArrayList<Word> words = new ArrayList<>();
        try {
            conn = connection;
            assert conn != null;
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {

                // 将查询到的数据写入Word对象中,然后存入数组
                words.add(toWord(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //closeDB(conn, stmt, resultSet);
        return new WordTable(words);
    }

    // 更新
    public int update(Word word) {
        String sql;
        Connection conn = null;
        Statement stmt = null;
        String date = Utils.toSqlDate(new Date());
        String table = getTableName(word);
        sql = "UPDATE " + table + " SET frequency = "+ word.getFreq() +", m = "+ word.getM() +", s = "+ word.getS() +", updated_at = '"+ date +"', memory_value = "+ word.getMemoryValue() +" WHERE word = '"+ word.getValue() +"'";
        try {
            conn = connection;
            assert conn != null;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库更新记录失败！");
        }

        //closeDB(conn, stmt);
        return COMPLETE;
    }

    // 删除
    public void delete(Word word) {
        String sql;
        Connection conn = null;
        Statement stmt = null;
        String table = getTableName(word);
        sql = "DELETE FROM " + table + " WHERE word = '"+ word.getValue() +"'";
        try {
            conn = connection;
            assert conn != null;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库删除记录失败！");
        }
        //closeDB(conn, stmt);
    }

    // 根据词语长度自动匹配数据库表
    public static String getTableName(Word word) {
        switch (word.getValue().length()) {
            case VOCABULARY_1_LENGTH:
                return VOCABULARY_1;
            case VOCABULARY_2_LENGTH:
                return VOCABULARY_2;
            case VOCABULARY_3_LENGTH:
                return VOCABULARY_3;
            case VOCABULARY_4_LENGTH:
                return VOCABULARY_4;
            case VOCABULARY_5_LENGTH:
                return VOCABULARY_5;
            default:
                System.out.println("数据库词汇表匹配失败！");
                new Exception().printStackTrace();
                return "";
        }
    }

    // 判断词汇是否在数据库中
    public boolean isExist(Word word) {
        return query(word) != null;
    }

    // 将查询结果转化为Word对象
    private static Word toWord(ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("word");
        int fre = resultSet.getInt("frequency");
        double s = resultSet.getDouble("s");
        double m = resultSet.getDouble("m");
        Date updatedAt = resultSet.getTimestamp("updated_at");
        return new Word(value, fre, s, m, updatedAt);
    }
}
