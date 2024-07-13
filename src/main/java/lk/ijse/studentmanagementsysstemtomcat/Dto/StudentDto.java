package lk.ijse.studentmanagementsysstemtomcat.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDto implements Serializable {
    private String id;
    private String name;
    private String email;
    private String city;
    private String level;

}
