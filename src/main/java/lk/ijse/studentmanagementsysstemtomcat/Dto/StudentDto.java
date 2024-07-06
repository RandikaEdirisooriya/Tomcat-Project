package lk.ijse.studentmanagementsysstemtomcat.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDto {
    private String id;
    private String name;
    private String email;
    private String city;
    private String level;

}
