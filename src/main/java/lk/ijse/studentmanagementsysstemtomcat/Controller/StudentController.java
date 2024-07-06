package lk.ijse.studentmanagementsysstemtomcat.Controller;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/Student")
public class StudentController extends HttpServlet {
Connection connection;
static String SAVE_STUDENT="INSERT INTO students(id,name,city,email,level)VALUE(?,?,?,?,?)";
    @Override
    public void init() throws ServletException {
        try {

        var driverclass = getServletContext().getInitParameter("driver-class");
        var dbURL = getServletContext().getInitParameter("dbURL");
        var dbUserName = getServletContext().getInitParameter("dbUserName");
        var dbPassword = getServletContext().getInitParameter("dbPassword");


            Class.forName(driverclass);
           this.connection  = DriverManager.getConnection(dbURL, dbUserName, dbPassword);


        } catch (ClassNotFoundException |SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!"application/json".equalsIgnoreCase(req.getContentType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected content type: application/json");
                return;
            }
            String id = UUID.randomUUID().toString();
            Jsonb jsonb = JsonbBuilder.create();
            List<StudentDto> studentDTO = jsonb.fromJson(req.getReader(), new ArrayList<StudentDto>() {
            }.getClass().getGenericSuperclass());

            studentDTO.forEach(System.out::println);
            //persist student data

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STUDENT);
            for (StudentDto stu: studentDTO){
                preparedStatement.setString(1,stu.getId());
                preparedStatement.setString(2,stu.getName());
                preparedStatement.setString(3,stu.getEmail());
                preparedStatement.setString(4,stu.getCity());
                preparedStatement.setString(5,stu.getLevel());

                if (preparedStatement.executeUpdate() !=0){
                    resp.getWriter().write("Save student");
                }else {
                    resp.getWriter().write("unable to save student");
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :get student
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :update student
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :delete student
    }
}
