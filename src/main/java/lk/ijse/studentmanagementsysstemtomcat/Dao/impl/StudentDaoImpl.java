package lk.ijse.studentmanagementsysstemtomcat.Dao.impl;

import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementsysstemtomcat.Dao.StudentDao;
import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;
import lk.ijse.studentmanagementsysstemtomcat.Util.UtilProcess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDaoImpl implements StudentDao {
    static String SAVE_STUDENT = "INSERT INTO students(id,name,city,email,level)VALUE(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id = ?";
    static String UPDATE_STUDENT = "UPDATE students SET name = ?, email = ?, city = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM students WHERE id = ?";

    @Override
    public StudentDto getStudent(String StuId, Connection connection) throws SQLException {
        var studentDTO = new StudentDto();
        try {
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, StuId);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                studentDTO.setId(resultSet.getString("id"));
                studentDTO.setName(resultSet.getString("name"));
                studentDTO.setCity(resultSet.getString("city"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setLevel(resultSet.getString("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentDTO;
    }




    @Override
    public String SaveStudent(StudentDto studentDto, Connection connection) throws SQLException {
        try {
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1, studentDto.getId());
            ps.setString(2, studentDto.getName());
            ps.setString(3, studentDto.getCity());
            ps.setString(4, studentDto.getEmail());
            ps.setString(5, studentDto.getLevel());
            if (ps.executeUpdate() != 0) {
                return "Save student successfully";
            } else {
                return "Save student failed";
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public boolean deleteStudent(String StuId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT);
        preparedStatement.setString(1, StuId);

        int rowsDeleted = preparedStatement.executeUpdate();
        if (rowsDeleted > 0) {
           return true;
        } else {
          return false;
        }
    }

    @Override
    public boolean UpdateStudent(String StuId, StudentDto studentDto, Connection connection) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STUDENT);
        preparedStatement.setString(1, studentDto.getName());
        preparedStatement.setString(2, studentDto.getEmail());
        preparedStatement.setString(3, studentDto.getCity());
        preparedStatement.setString(4, studentDto.getLevel());
        preparedStatement.setString(5, StuId);

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
          return  true;

        } else {
          return false;
        }
    }
}
