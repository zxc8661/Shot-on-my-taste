package somt.somt.common.image;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ImageHandler {

    @Value("${spring.upload.image}")
    private String uploadDir;

    public String saveImage(MultipartFile image) throws IOException {
        if(image.isEmpty()){
            return null;
        }

        String fileName = image.getName();

        Path filePath = Paths.get(uploadDir+fileName);

        if(!Files.exists(Paths.get(uploadDir))){
            Files.createDirectories(Paths.get(uploadDir));
        }

        Files.write(filePath,image.getBytes());

        return filePath.toString();
    }

    public void deleteImage(List<String> images) throws IOException{
        for(String imagePath: images){
            Path path = Paths.get(imagePath);
            Files.delete(path);
        }
    }
}
