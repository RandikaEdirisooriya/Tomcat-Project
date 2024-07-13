package lk.ijse.studentmanagementsysstemtomcat.Dao;

import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;

import java.sql.Connection;
import java.sql.SQLException;

public interface StudentDao {
    StudentDto getStudent(String StuId, Connection connection) throws SQLException;
    String SaveStudent(StudentDto studentDto,Connection connection) throws SQLException;
    boolean deleteStudent(String StuId,Connection connection) throws SQLException;
    boolean UpdateStudent(String StuId,StudentDto studentDto,Connection connection) throws SQLException;
}
