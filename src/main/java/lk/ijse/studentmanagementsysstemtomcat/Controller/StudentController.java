package lk.ijse.studentmanagementsysstemtomcat.Controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/Student")
public class StudentController extends HttpServlet {
    Connection connection;
    static String SAVE_STUDENT = "INSERT INTO students(id, name, city, email, level) VALUES (?, ?, ?, ?, ?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id = ?";

    @Override
    public void init() throws ServletException {
        try {
            var driverClass = getServletContext().getInitParameter("driver-class");
            var dbURL = getServletContext().getInitParameter("dbURL");
            var dbUserName = getServletContext().getInitParameter("dbUserName");
            var dbPassword = getServletContext().getInitParameter("dbPassword");

            Class.forName(driverClass);
            this.connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

        } catch (ClassNotFoundException | SQLException e) {
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
            // persist student data

            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STUDENT);
            for (StudentDto stu : studentDTO) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, stu.getName());
                preparedStatement.setString(3, stu.getEmail());
                preparedStatement.setString(4, stu.getCity());
                preparedStatement.setString(5, stu.getLevel());

                if (preparedStatement.executeUpdate() != 0) {
                    resp.getWriter().write("Save student");
                } else {
                    resp.getWriter().write("Unable to save student");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var studentDto = new StudentDto();
        String stuId = req.getParameter("id");
        try (var writer = resp.getWriter()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT);
            preparedStatement.setString(1, stuId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                studentDto.setId(resultSet.getString("id"));
                studentDto.setName(resultSet.getString("name"));
                studentDto.setEmail(resultSet.getString("email"));
                studentDto.setCity(resultSet.getString("city"));
                studentDto.setLevel(resultSet.getString("level"));
            }
            System.out.println(studentDto);
            writer.write(studentDto.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!"application/json".equalsIgnoreCase(req.getContentType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expected content type: application/json");
                return;
            }

            Jsonb jsonb = JsonbBuilder.create();
            StudentDto studentDto = jsonb.fromJson(req.getReader(), StudentDto.class);

            if (studentDto.getId() == null || studentDto.getId().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required for update");
                return;
            }

            String UPDATE_STUDENT = "UPDATE students SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT);
            preparedStatement.setString(1, studentDto.getName());
            preparedStatement.setString(2, studentDto.getEmail());
            preparedStatement.setString(3, studentDto.getCity());
            preparedStatement.setString(4, studentDto.getLevel());
            preparedStatement.setString(5, studentDto.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                resp.getWriter().write("Student updated successfully");
            } else {
                resp.getWriter().write("Unable to update student");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the student");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required for deletion");
            return;
        }

        try {
            String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT);
            preparedStatement.setString(1, studentId);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                resp.getWriter().write("Student deleted successfully");
            } else {
                resp.getWriter().write("Unable to delete student");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the student");
        }
    }
}
