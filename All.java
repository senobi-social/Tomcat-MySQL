package chapter14;

import tool.Page;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(urlPatterns={"/chapter14/all"})
public class All extends HttpServlet {
  public void doGet (
    HttpServletRequest request, HttpServletResponse response
  ) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    Page.header(out);

    try {

      // コンストラクタを使ってInitialContextオブジェクトを取得
      // JNDIを使うための窓口になる
      // コネクションプールからコネクションを取得
      InitialContext ic = new InitialContext();

      // データソースの取得
      // 因数にはJDNIのリソース名を指定する
      // context.xmlのResource要素のname属性を参照
      DataSource ds = (DataSource)ic.lookup("java:/comp/env/jdbc/book");

      // データベースへの接続を確立
      Connection con = ds.getConnection();

      // SQL文のセット
      PreparedStatement st = con.prepareStatement (
      "select * from temperature_members");

      // SQL文の実行
      ResultSet rs = st.executeQuery();

      // next()で１行ずつデータを取り出す
      // next()は行があればtrue,なければfalseを返す
      while (rs.next()) {
        out.println(rs.getInt("id"));
        out.println(":");
        out.println(rs.getString("name"));
        out.println(":");
        out.println("<br>");

      }

      // PreparedStatementを修了
      st.close();

      // Connectionを終了
      con.close();

    } catch (Exception e ) {
      e.printStackTrace(out);
    }

    Page.footer(out);

  }
}
