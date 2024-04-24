<h3>JDBCTemplate에 사용된 템플릿/전략패턴</h3>

```
public void deleteAll() throws SQLException {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
        con = dataSource.getConnection();
        pstmt = con.prepareStatement("delete from users");
        pstmt.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
    }
}
```
일반적인 Dao클래스중 하나인 메서드이다. 

파란색 부분 --모든 dao메서드에서 중복되는 코드

빨간색 부분 --유일하게 메서드마다 달라지는 부분 (확장이 필요한 부분)

🧐 JdbcTemplate에서 사용하는 전략이란

JdbcTemplate에서는 위처럼 파란색부분 코드는 모든 메서드에서 반복되니깐 template형태로 고정시키고 빨간색 부분만 전략적으로 변경하는거다.

🧐 위 코드에서 파란색 부분에 해당하는 코드를 template형태로 만들기

```
public class MyCustomJdbcTemplate {
    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void myJdbcTemplate(StatementStrategy stmt) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = stmt.makePreparedStatement(con);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
```
🧐 빨간색 부분에 해당하는 부분을 인터페이스로 분리시켜서 다양한 기능 확장가능  

```
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection con)throws SQLException;
}
```
🧐 이렇게 분리시키고 나서 MyCustomJdbcTemplate를 사용하는 클라이언트(UserDao)쪽에서 전략을 구현해 템플릿에 넘기면 됨 보통 익명 객체로 넘김

```
  public void add(User user) throws SQLException {
        template.myJdbcTemplate(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, user.getId());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getPassword());
                return pstmt;
            }
        });
    }
```
🧐 UserDao에서 익명객체를 선언안하게 만들기

익명객체를 선언해서 파라미터로 넣어주는 행위도 중복이니깐 익명객체도 템플릿 클래스에 넣어두고 sql쿼리만 넘기면 더 간단해짐
```
public void excute(final String sql, User user) throws SQLException {
    myJdbcTemplate(new StatementStrategy() {

        @Override
        public PreparedStatement makePreparedStatement(Connection con) throws SQLException {
            PreparedStatement pstmt = con.prepareStatement("insert into users(id,name,password) values(?,?,?)");
           pstmt.setString(1,user.getId());
           pstmt.setString(2,user.getName());
           pstmt.setString(3,user.getPassword());
            return pstmt;
        }
    });
}
```
그럼 UserDao코드에서는 JdbcTemplate처럼 excute만 호출하면 됨 

```
public void add(User user) throws SQLException {
    String sql = "insert into users(id,name,password) values(?,?,?)";
    template.excute(sql,user);
}
```
