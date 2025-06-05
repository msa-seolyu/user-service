package com.seolyu.userservice.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    /**
     * 파일 확장자 추출
     * @param filename 파일명
     * @return 확장자
     */
    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 파일명 생성 (UUID + 확장자)
     * @param originalFilename 원본 파일명
     * @return
     */
    public static String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID() + "." + extension;
    }
}
