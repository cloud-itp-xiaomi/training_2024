import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_delete {
    public static void main(String[] args) throws Exception{
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");

            String url = "jdbc:mysql://localhost:3306/nowcoder?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
            ;
            Connection conn = null;
            Statement stmt = null;
            PreparedStatement pst = null;

            conn = DriverManager.getConnection(url, "root", "chenanqi991128");
            System.out.println("成功连接到数据库！");

            //删除数据
            String sql1 = "DELETE FROM nowcoder.user_profile WHERE university =?" ;
            //使用PreparedStatement预编译SQL语句，提高安全性
            pst = conn.prepareStatement(sql1);
            stmt = conn.createStatement();
            pst.setString(1, "北京师范大学");
            pst.executeUpdate();
            System.out.println("已删除数据！");

            stmt.close();
            conn.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
