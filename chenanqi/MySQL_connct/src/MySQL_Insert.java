import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class MySQL_Insert {
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

            //调用DriverManager类的getConnection()方法获取数据库连接
            conn = DriverManager.getConnection(url, "root", "chenanqi991128");
            System.out.println("成功连接到数据库！");

            //插入数据语句
            String sql3 = "INsert into nowcoder.user_profile values(?,?,?,?,?,?)";
            //创建PreparedStatement对象
            pst = conn.prepareStatement(sql3);
            stmt = conn.createStatement();
            //设置参数
            pst.setInt(1, 7);
            pst.setInt(2, 1128);
            pst.setString(3, "female");
            pst.setInt(4, 24);
            pst.setString(5, "中国地质大学");
            pst.setFloat(6, 4);
            pst.executeUpdate();
            System.out.println("修改成功！");
            stmt.close ();
            conn.close();

    }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
