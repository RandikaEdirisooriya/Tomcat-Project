package lk.ijse.studentmanagementsysstemtomcat.Util;

import java.util.UUID;

public class UtilProcess {
   public static String genereteId(){
       return UUID.randomUUID().toString();
   }
}
