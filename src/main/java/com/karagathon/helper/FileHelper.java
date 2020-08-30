package com.karagathon.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.karagathon.aws.service.AWSS3Service;

@Component
public class FileHelper {

	@Autowired
	AWSS3Service awsS3Service;

	public List<String> uploadMultipleFiles(List<MultipartFile> files, BucketBeanHelper bucketBean) {

		List<String> filenames = new ArrayList<>();
		files.forEach(file -> {
			if (!file.isEmpty() && !file.getOriginalFilename().trim().isEmpty()) {
				filenames.add(awsS3Service.uploadFile(file, bucketBean));
			}
		});

		return filenames;
	}

	public List<String> moveUploadedFile(List<MultipartFile> files, String destination) {
		Path destinationDir = Paths.get(destination);

		if (!Files.exists(destinationDir)) {
			try {
				Files.createDirectory(destinationDir);
			} catch (IOException e1) {
				//
			}
		}

		// Start Change - JAng - 08/05/2020
		// files.forEach(file -> listFilePaths.add(moveUploadedFile(file,
		// destination)));

		return files.stream().map(file -> moveUploadedFile(file, destination)).collect(Collectors.toList());

		// End Change - JAng - 08/05/2020
	}

	private String moveUploadedFile(MultipartFile file, String destination) {
		final StringBuffer renamedFileName = new StringBuffer()
				.append(FileCollisionAvoidanceHelper.getRenamedString(DateFormatter.format(LocalDateTime.now())))
				.append(FilenameUtils.EXTENSION_SEPARATOR)
				.append(FilenameUtils.getExtension(file.getOriginalFilename()));
		final StringBuffer destinationString = new StringBuffer().append(StringUtils.cleanPath(destination))
				.append(File.separator).append(renamedFileName);

		Path destinationLocation = Paths.get(destinationString.toString());

		System.out.println("Path: " + destinationLocation.toAbsolutePath().toString());

		try {
			Files.copy(file.getInputStream(), destinationLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return renamedFileName.toString();
	}
}
