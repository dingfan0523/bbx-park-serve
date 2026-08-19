
package com.cgnpc.bbxpark.restaurant.service;


import com.amazonaws.util.IOUtils;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.ImageValidator;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import com.cgnpc.bbxpark.config.minio.model.FileModel;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


@Slf4j
@Service
public class DishesGalleryServiceApp {

	/**
	 * http客户端.
	 */
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private FileCenterService fileCenterService;

	@Autowired
	private IDishesGalleryService bbxDishesGalleryService;

    private static final List<Charset> COMMON_CHARSETS = Arrays.asList(
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("GB2312"),
            Charset.forName("Shift_JIS"), // 如果可能收到日文压缩包
            Charset.forName("ISO-8859-1")  // 最后的保底，通常不会失败
    );

	/**
	 * 菜品库服务地址.
	 */

	/**
	 * 导入菜品库.
	 * @Param param 压缩包文件
	 * @Return
	 */
	public CudResult<Boolean> importDishes(MultipartFile file){
		// 检查文件格式
        String fileName = file.getOriginalFilename();
        AssertUtils.isTrue(fileName == null || isSupportedArchiveFormat(fileName),"上传文件格式错误，请重试");
		Long headerTenantId = WebFrameworkUtils.getHeaderTenantId();
		// 保存上传的压缩包到临时目录
		Path tempDir = createTempDirectory();
		Path tempFile = tempDir.resolve(fileName);
		List<FileModel> fileModels = new ArrayList<>();
		try {
			file.transferTo(tempFile);
			// 解压压缩包
			List<File> extractedFiles = extractArchive(tempFile.toFile(), tempDir.toFile());
			for (File file1 : extractedFiles) {
				// 校验文件格式
				if (ImageValidator.isValidImageFormat(file1)) {
					try (
							InputStream inputStream = new FileInputStream(file1)) {
						String encodedFileName = URLEncoder.encode(file1.getName(), StandardCharsets.UTF_8.name());
						MultipartFile multipartFile = new MockMultipartFile(encodedFileName,encodedFileName,null, inputStream);
						FileModel upload = fileCenterService.upload(multipartFile, null, headerTenantId);
						String suffixFileName = ImageValidator.removeFileExtension(file1);
						upload.setFullPath(suffixFileName);
						fileModels.add(upload);
					}
				}
			}
		} catch (IOException | ArchiveException e) {
			throw new RuntimeException("上传压缩包失败",e);
		}finally {
			// 清理临时文件
			cleanupTempFiles(tempFile, tempDir);
		}
		if (fileModels.isEmpty()) {
			return new CudResult<>();
		}

		bbxDishesGalleryService.importDishes(fileModels);
		// 上传解压后的文件
		return CudResult.success(true);
	}

	/**
	 * 创建临时目录
	 * @return 临时目录路径
	 */
	private Path createTempDirectory() {
		try {
			Path projectRoot = Paths.get("").toAbsolutePath();
			return Files.createTempDirectory(projectRoot, "upload");
		} catch (IOException e) {
			throw new RuntimeException("创建临时目录失败", e);
		}
	}

	/**
	 * 清理临时文件
	 * @param tempFile 上传压缩包的临时文件路径
	 * @param tempDir 上传压缩包的临时目录路径
	 */
	private void cleanupTempFiles(Path tempFile, Path tempDir) {
		try {
			if (tempFile != null && Files.exists(tempFile)) {
				Files.deleteIfExists(tempFile);
			}
			if (tempDir != null && Files.exists(tempDir)) {
				deleteDirectory(tempDir);
			}
		} catch (IOException e) {
			throw new RuntimeException("清理临时文件失败", e);
		}
	}


	/**
	 * 删除目录
	 * @param directory 目录路径
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void deleteDirectory(Path directory) throws IOException {
		if (Files.exists(directory)) {
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	/**
	 * 将文件转换为MultipartFile
	 * @param file 文件
	 * @return MultipartFile
	 * @throws IOException
	 */
	private MultipartFile convertFileToMultipartFile(File file) throws IOException {
		FileInputStream inputStream = new FileInputStream(file);
		return new MockMultipartFile(file.getName(), file.getName(), null, inputStream);
	}

	/**
	 * 判断是否支持的压缩包格式
	 * @param fileName
	 * @return
	 */
	private boolean isSupportedArchiveFormat(String fileName) {
		return fileName.endsWith(".zip") || fileName.endsWith(".rar") || fileName.endsWith(".7z");
	}

	/**
	 * 解压压缩包
	 * @param archiveFile 压缩包文件
	 * @param destinationDir 解压目录
	 * @return 解压后的文件列表
	 * @throws IOException
	 * @throws ArchiveException
	 */
    private List<File> extractArchive(File archiveFile, File destinationDir) throws IOException, ArchiveException {
        String fileName = archiveFile.getName().toLowerCase();
        if (fileName.endsWith(".zip")) {
            return extractZip(archiveFile, destinationDir);
        } else if (fileName.endsWith(".rar")) {
            return extractRar(archiveFile, destinationDir);
        } else {
            throw new ArchiveException("不支持的压缩格式");
        }
    }
//	private List<File> extractArchive(File archiveFile, File destinationDir) throws IOException, ArchiveException {
//		List<File> extractedFiles = new ArrayList<>();
//		if (!archiveFile.exists()) {
//			throw new RuntimeException("文件不存在");
//		}
//		if (!archiveFile.canRead()) {
//			throw new RuntimeException("没有读取文件的权限");
//		}
//        // TODO 压缩包文件
//		try (RandomAccessFile randomAccessFile = new RandomAccessFile(archiveFile, "r");
//             IInArchive inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile))) {
//
//			ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
//			for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
//				if (!item.isFolder()) {
//					File outputFile = new File(destinationDir, item.getPath());
//					outputFile.getParentFile().mkdirs();
//					try (OutputStream os = new FileOutputStream(outputFile)) {
//						item.extractSlow((ISequentialOutStream) data -> {
//							try {
//								os.write(data);
//							} catch (IOException e) {
//								throw new RuntimeException("写入输出流时出错");
//							}
//							return data.length;
//						});
//					}
//					extractedFiles.add(outputFile);
//				} else {
//					File dir = new File(destinationDir, item.getPath());
//					dir.mkdirs();
//				}
//			}
//		} catch (IOException e) {
//			throw new RuntimeException("解压文件时出错");
//		}
//		return extractedFiles;
//	}




    private List<File> extractZip(File archiveFile, File destinationDir) throws IOException {
        List<File> extractedFiles = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(archiveFile)) { // 不指定编码，稍后用 rawName 自己解码
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                // 关键：从原始字节解码文件名
                String entryName = decodeFileName(entry.getRawName());
                File outputFile = new File(destinationDir, entryName);

                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    outputFile.getParentFile().mkdirs();
                    try (InputStream is = zipFile.getInputStream(entry);
                         OutputStream os = Files.newOutputStream(outputFile.toPath())) {
                        IOUtils.copy(is, os);
                    }
                    extractedFiles.add(outputFile);
                }
            }
        }
        return extractedFiles;
    }

    private List<File> extractRar(File archiveFile, File destinationDir) throws IOException {
        List<File> extractedFiles = new ArrayList<>();
        try (Archive archive = new Archive(archiveFile)) {
            List<FileHeader> fileHeaders = archive.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                if (fileHeader.isDirectory()) {
                    File dir = new File(destinationDir, fileHeader.getFileName());
                    dir.mkdirs();
                } else {
                    File outputFile = new File(destinationDir, fileHeader.getFileName());
                    outputFile.getParentFile().mkdirs();
                    try (OutputStream os = new FileOutputStream(outputFile)) {
                        archive.extractFile(fileHeader, os);
                    }
                    extractedFiles.add(outputFile);
                }
            }
        } catch (RarException e) {
            throw new IOException("解压RAR文件时出错", e);
        }
        return extractedFiles;
    }

    private String decodeFileName(byte[] rawName) {
        // 如果全部是 ASCII，直接用 UTF-8 解码（兼容 ASCII）
        boolean allAscii = true;
        for (byte b : rawName) {
            if (b < 0) { // 非 ASCII 字符（byte 值 > 127）
                allAscii = false;
                break;
            }
        }
        if (allAscii) {
            return new String(rawName, StandardCharsets.US_ASCII);
        }

        // 尝试多种编码，选择第一个解码成功的
        for (Charset cs : COMMON_CHARSETS) {
            try {
                CharsetDecoder decoder = cs.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);
                decoder.decode(ByteBuffer.wrap(rawName));
                // 如果没抛出异常，说明解码成功，返回字符串
                return new String(rawName, cs);
            } catch (CharacterCodingException e) {
                // 解码失败，尝试下一个编码
            }
        }
        // 如果所有编码都失败（理论上不会，因为 ISO-8859-1 不会失败），返回默认编码的结果（可能乱码）
        return new String(rawName, Charset.defaultCharset());
    }
}
