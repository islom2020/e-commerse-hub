package reportstoadmin;

import jsonFile.FileUrls;
import lombok.SneakyThrows;
import model.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelFile {
    @SneakyThrows
    public static void parseUserListToExcel(List<User> userList) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("user list info");
        HSSFRow rowHeadline = sheet.createRow(0);
        rowHeadline.createCell(2).setCellValue("All users in our system");
        HSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue("#");
        row.createCell(1).setCellValue("name");
        row.createCell(1).setCellValue("created date");
        row.createCell(1).setCellValue("updated date");
        row.createCell(1).setCellValue("activation status");
        row.createCell(2).setCellValue("username");
        row.createCell(3).setCellValue("location");
        row.createCell(3).setCellValue("phone number");
        row.createCell(4).setCellValue("balance");
        row.createCell(5).setCellValue("chat id");

        int rowIndex = 2;
        for (User user : userList) {
            HSSFRow row1 = sheet.createRow(rowIndex ++);

            HSSFCell cell0 = row1.createCell(0);
            cell0.setCellValue(rowIndex - 2);
//            cell0.setCellStyle(getStyle(workbook));

            HSSFCell cell1 = row1.createCell(1);
            cell1.setCellValue(user.getName());

            HSSFCell cell2 = row1.createCell(2);
            cell2.setCellValue(user.getCreatedDate());

            HSSFCell cell3 = row1.createCell(3);
            cell3.setCellValue(user.getUpdatedDate());

            HSSFCell cell4 = row1.createCell(4);
            cell4.setCellValue(user.isActive());

            HSSFCell cell5 = row1.createCell(5);
            cell5.setCellValue(user.getUsername());

            HSSFCell cell6 = row1.createCell(6);
            cell6.setCellValue(user.getLocation().toString());

            HSSFCell cell7 = row1.createCell(7);
            cell7.setCellValue(user.getPhoneNumber());

            HSSFCell cell8 = row1.createCell(8);
            cell8.setCellValue(user.getBalance());

            HSSFCell cell9 = row1.createCell(9);
            cell9.setCellValue(user.getChatId());
        }



        FileOutputStream fileOutputStream = new FileOutputStream(FileUrls.adminUserReportUrl);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }
}
