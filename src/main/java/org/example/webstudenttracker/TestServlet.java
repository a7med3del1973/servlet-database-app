package org.example.webstudenttracker;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/web_student_tracker")

    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1 : set up the printwriter
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        // 2 : get a connection to the database
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            myConn = dataSource.getConnection();

            // 3 : create a sql statements
            String sql = "SELECT * FROM student";
            myStmt = myConn.createStatement();

            // 4 : execute sql query
            myRs = myStmt.executeQuery(sql);

            // 5 : process the result set
            while (myRs.next()) {
                String email = myRs.getString("email");
                out.println(email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
