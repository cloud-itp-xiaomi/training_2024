import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL_test {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL ="jdbc:mysql://localhost:3306/nowcoder?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "chenanqi991128";
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 开始查询...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM user_profile";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("id");
                int device_id = rs.getInt("device_id");
                String university = rs.getString("university");
                String gpa = rs.getString("gpa");
                String gender = rs.getString("gender");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 设备号: " + device_id);
                System.out.print(", 学校名称: " + university);
                System.out.print(", GPA: " + gpa);
                System.out.print(", gender: " + gender);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}