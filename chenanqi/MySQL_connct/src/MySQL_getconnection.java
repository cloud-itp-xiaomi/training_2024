import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_getconnection {
    public static void main(String[] args) {
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        } catch (ClassNotFoundException e1) {
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }

        String url = "jdbc:mysql://localhost:3306/nowcoder?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";;
        Connection conn = null ;
        Statement stmt = null;
        try {
            //调用DriverManager类的getConnection()方法获取数据库连接
            conn = DriverManager.getConnection(url, "root", "chenanqi991128");
            System.out.println("成功连接到数据库！");
            stmt = conn.createStatement();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}