package jsonFile;

import lombok.SneakyThrows;

import java.io.*;

public class FileUtils {
    @SneakyThrows
    public static void writeToFile(String fileUrl,String json){
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileUrl, false))){
                bufferedWriter.write(json);
            } catch (Exception e){
                e.printStackTrace();
            }
    }

    public static String  readFromFile(String fileUrl){
        createFile(fileUrl);
        StringBuilder json = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileUrl))){
            String s;
            while ((s = bufferedReader.readLine()) != null){
                json.append(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return json.toString();
    }


    private static void createFile(String fileUrl){
        File file = new File(fileUrl);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}