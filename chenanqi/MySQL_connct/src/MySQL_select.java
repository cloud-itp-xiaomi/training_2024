import java.sql.*;

public class MySQL_select {
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

            //执行SQL语句
            String sql4 = "SELECT * FROM nowcoder.user_profile ";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql4);
            while (rs.next()) {
                System.out.println("ID:"+rs.getString("id") + " 设备号:" + rs.getString("device_id") + " 学校名称:" +
                        rs.getString("university") + " GPA:" + rs.getString("gpa")+" 性别:" +
                        rs.getString("gender")+" 年龄:"+rs.getString("age"));
                /*
                通过字段检索
                int id  = rs.getInt("id");
                int device_id = rs.getInt("device_id");
                String university = rs.getString("university");
                String gpa = rs.getString("gpa");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");

                输出数据
                System.out.print("ID: " + id);
                System.out.print(", 设备号: " + device_id);
                System.out.print(", 学校名称: " + university);
                System.out.print(", GPA: " + gpa);
                System.out.print(", gender: " + gender);
                System.out.print(",age:" + age);
                System.out.print("\n");  */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
