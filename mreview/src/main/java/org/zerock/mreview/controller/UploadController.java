package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.MovieImageDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileName ::::::::::::: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            log.info("file ::::::::::::::::: " + file);

            HttpHeaders headers = new HttpHeaders();

            // MIME 처리
            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<List<MovieImageDTO>> uploadFile(MultipartFile[] uploadFiles) {
        List<MovieImageDTO> movieImageDTOList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일인지 체크
            if (uploadFile.getContentType().startsWith("image") == false) {
                log.warn(":::::::::: this file is not image type ::::::::::");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String originalName = uploadFile.getOriginalFilename();
            // 크롬으로 하면 전체 경로로 안들어옴
            // 엣지는 전체 경로로 들어와서 해줘야됨
            // String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName ::::::::::::: " + originalName);

            // 날짜 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 _를 사용해서 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + originalName;
            // 썸네일 파일 이름
            String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + originalName;

            Path savePath = Paths.get(saveName);

            try {
                // 기본 이미지
                uploadFile.transferTo(savePath);

                // 썸네일 이미지
                File thumbnailFile = new File(thumbnailSaveName);
                // 썸네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                movieImageDTOList.add(new MovieImageDTO(originalName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new ResponseEntity<>(movieImageDTOList, HttpStatus.OK);
    }

    @PostMapping("/deleteFile")
    public ResponseEntity<Boolean> deleteFile(String fileName) {

        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");

            File dImgFile = new File(uploadPath + File.separator, srcFileName);
            boolean dImgResult = dImgFile.delete();

            File tImgFile = new File(dImgFile.getParent(), "s_" + dImgFile.getName());
            boolean tImgResult = tImgFile.delete();

            return new ResponseEntity<>(dImgResult && tImgResult ? true : false, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        // file 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

}
