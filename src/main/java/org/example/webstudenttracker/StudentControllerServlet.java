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
                case "ADD":
                    addStudent(request, response);
                    break;
                case "LOAD":
                    loadStudent(request, response);
                    break;
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                default:
                    listStudent(request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // read student info from form data
        String theStudentId = request.getParameter("studentId");

        // delete student from database
        studentDbUtil.deleteStudent(theStudentId);

        // send them back to "list students" page
        listStudent(request, response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // read student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student
        Student theStudent = new Student(id, firstName, lastName, email);


        // perform update on database
        studentDbUtil.updateStudent(theStudent);

        // send them back to the "list student" page
        listStudent(request, response);

    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read student id from data
        String theStudentId = request.getParameter("studentId");


        // get student from database
        Student theStudent = studentDbUtil.getStudent(theStudentId);

        // place student in request attribute
        request.setAttribute("THE_STUDENT", theStudent);

        // send to jsp page : update-student-form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");

        dispatcher.forward(request, response);
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
