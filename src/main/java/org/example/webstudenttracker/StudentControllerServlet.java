package org.example.webstudenttracker;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serial;
import java.util.List;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private StudentDbUtil studentDbUtil;

    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        // create out student db util ... and pass in the conn pool / datasource
        try {
            studentDbUtil = new StudentDbUtil(dataSource);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // list the student ... in mvc fashion
        try {

            // read command parameter
            String theCommand = request.getParameter("command");

            // if the command is missing, then default to listing students
            if (theCommand == null) {
                theCommand = "LIST";
            }
            // route to the appropriate command
            switch (theCommand) {
                case "LIST":
                    listStudent(request, response);
                    break;
                case "ADD":
                    addStudent(request, response);
                    break;
                default:
                    listStudent(request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read student info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student theStudent = new Student(firstName, lastName, email);

        // add the student to database
        studentDbUtil.addStudent(theStudent);

        // send back to main page (the student list)
        listStudent(request, response);
    }

    private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // get students from db util
        List<Student> students = studentDbUtil.getStudents();
        // add students to the request
        request.setAttribute("STUDENT_LIST", students);

        // send to jsp (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);

    }


}
