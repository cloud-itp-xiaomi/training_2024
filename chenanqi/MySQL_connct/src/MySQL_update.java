import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQL_update {
    //throws Exception 声明该方法可能抛出异常,或者参考Getconnection.java，加上try catch
    public static void main(String[] args) throws Exception {
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");

            String url = "jdbc:mysql://localhost:3306/nowcoder?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
            ;
            Connection conn = null;
            Statement stmt = null;
            PreparedStatement pst = null;

            //调用DriverManager类的getConnection()方法获取数据库连接
            conn = DriverManager.getConnection(url, "root", "chenanqi991128");
            System.out.println("成功连接到数据库！");

            //修改数据代码
            String sql2 = "UPDATE nowcoder.user_profile SET age = ? WHERE id = ?";
            pst = conn.prepareStatement(sql2);
            stmt = conn.createStatement();
            pst.setInt(1, 20);
            pst.setInt(2, 1);
            pst.executeUpdate();
            System.out.println("修改成功！");
            stmt.close ();
            conn.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}