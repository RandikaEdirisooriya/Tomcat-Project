package lk.ijse.studentmanagementsysstemtomcat.Controller;

import jakarta.json.JsonException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;
import lk.ijse.studentmanagementsysstemtomcat.Dao.impl.StudentDaoImpl;
import lk.ijse.studentmanagementsysstemtomcat.Util.UtilProcess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/Student",
initParams = {
        @WebInitParam(name = "driver-class",value = "com.mysql.cj.jdbc.Driver"),
        @WebInitParam(name = "dbURL",value = "jdbc:mysql://localhost:3306/tomcatDB?createDatabaseIfNotExist=true"),
        @WebInitParam(name = "dbUserName",value = "root"),
        @WebInitParam(name = "dbPassword",value = "1234")
}

)
public class StudentController extends HttpServlet {
    Connection connection;
    static String SAVE_STUDENT = "INSERT INTO students(id,name,city,email,level)VALUE(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE students SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";

    @Override
    public void init() throws ServletException {
        try {

            var driverclass = getServletContext().getInitParameter("driver-class");
            var dbURL = getServletContext().getInitParameter("dbURL");
            var dbUserName = getServletContext().getInitParameter("dbUserName");
            var dbPassword = getServletContext().getInitParameter("dbPassword");

            /*get config from servlet*/
            /*  *//*meka echchara hoda widihak nemei*//*
            var driverclass = getServletConfig().getInitParameter("driver-class");
            var dbURL = getServletConfig().getInitParameter("dbURL");
            var dbUserName = getServletConfig().getInitParameter("dbUserName");
            var dbPassword = getServletConfig().getInitParameter("dbPassword");*/


            Class.forName(driverclass);
            this.connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!"application/json".equalsIgnoreCase(req.getContentType())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected content type: application/json");
            return;
        }
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            StudentDto studentDTO = jsonb.fromJson(req.getReader(), StudentDto.class);
            studentDTO.setId(UtilProcess.genereteId());
            var saveData = new StudentDaoImpl();
            writer.write(saveData.SaveStudent(studentDTO, connection));
        } catch (JsonException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var studentId = req.getParameter("id");
        var dataProcess = new StudentDaoImpl();
        try (var writer = resp.getWriter()) {
            var student = dataProcess.getStudent(studentId, connection);
            System.out.println(student);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(student, writer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }







    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!"application/json".equalsIgnoreCase(req.getContentType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected content type: application/json");
                return;
            }

            String stuId = req.getParameter("id");
            if (stuId == null || stuId.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty student ID");
                return;
            }

            StudentDaoImpl dataProcess = new StudentDaoImpl();
            try (var reader = req.getReader(); var writer = resp.getWriter()) {
                Jsonb jsonb = JsonbBuilder.create();
                StudentDto studentDto;
                try {
                    studentDto = jsonb.fromJson(reader, StudentDto.class);
                } catch (Exception e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data");
                    return;
                }

                boolean isUpdated = dataProcess.UpdateStudent(stuId, studentDto, connection);
                resp.setContentType("application/json");

                if (isUpdated) {
                    writer.println("Updated");
                } else {
                    writer.println("Not Updated");
                }
                jsonb.toJson(isUpdated, writer);
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }
        } catch (IOException e) {
            throw new ServletException("IO error", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required for deletion");
            return;
        }
        StudentDaoImpl dataProcess = new StudentDaoImpl();
        try (var writer= resp.getWriter()){


            boolean isdeleted = dataProcess.deleteStudent(studentId, connection);

            if (isdeleted) {
                writer.println("Deleted");
            } else {
                writer.println("Not Deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the student");
        }
    }
}
