package somt.somt.domain.productThumbnail.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import somt.somt.common.exception.CustomException;
import somt.somt.common.exception.ErrorCode;
import somt.somt.domain.product.entity.Product;
import somt.somt.domain.productThumbnail.entity.ProductThumbnail;
import somt.somt.domain.productThumbnail.repository.ProductThumbnailRepository;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductThumbnailService {

    private final ProductThumbnailRepository productThumbnailRepository;

    @Value("${spring.upload.image}")
     private String BASE_PATH;



    @Transactional
    public void uploadImageFile(List<MultipartFile> imageFiles, Product product) {

        if(imageFiles.stream().allMatch(MultipartFile::isEmpty)){
            throw new CustomException(ErrorCode.IMAGE_FILE_EMPTY);
        }

        if(imageFiles==null || imageFiles.isEmpty()  ){
            throw new CustomException(ErrorCode.IMAGE_FILE_EMPTY);
        }
        Path uploadDir = Paths.get(BASE_PATH + product.getId());
        createFile(uploadDir);
        saveImages(imageFiles,product,uploadDir);
    }

    @Transactional
    public void modifyImageFile(List<MultipartFile> imageFiles, Product product) {
        if(imageFiles.isEmpty()){
            return;
        }



        Path uploadDir = Paths.get(BASE_PATH + product.getId());
        File dir = uploadDir.toFile();
        if (dir.exists()) {
            boolean deleted = FileSystemUtils.deleteRecursively(dir);
            if (!deleted) {
                throw new CustomException(ErrorCode.IMAGE_DELETE_FAIL);
            }
        }

        createFile(uploadDir);

        saveImages(imageFiles,product,uploadDir);

    }



    private void createFile( Path uploadDir){

        try {
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        }catch (Exception e){
            throw new CustomException(ErrorCode.BAD_FILEPATH);
        }

    }


    private void saveImages(List<MultipartFile> imageFiles,Product product, Path uploadDir){
        int count =1;


        for(MultipartFile image : imageFiles){
            String original = image.getOriginalFilename();
            String ext = original != null && original.contains(".")
                    ? original.substring(original.lastIndexOf("."))
                    : "";
            String imageName = product.getId() + product.getProductName() +" image"+ count++ +ext;


            Path target = uploadDir.resolve(imageName);

            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream,target, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e){
                throw  new CustomException(ErrorCode.IMAGE_FILE_SAVE_FAIL);
            }

            productThumbnailRepository.save(ProductThumbnail.builder()
                    .product(product)
                    .imagePath(uploadDir.toString())
                    .createAt(LocalDateTime.now())
                    .build());
        }
    }

}


















