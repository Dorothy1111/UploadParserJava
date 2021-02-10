package com.example.demo.service;

import com.example.demo.util.UploadUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import org.apache.poi.ss.util.NumberToTextConverter;

@Service
public class UploadService {
	
	private final UploadUtil uploadUtil;
	
	public UploadService(UploadUtil uploadUtil) {
		this.uploadUtil = uploadUtil;
	}

	public Map<Integer,  List<String>> upload(MultipartFile file) throws Exception {

		Path tempDir = Files.createTempDirectory("");

		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

		file.transferTo(tempFile);

		Workbook workbook = WorkbookFactory.create(tempFile);

		Sheet sheet = workbook.getSheetAt(0);

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
			data.put(i, new ArrayList<String>());
			for (Cell cell : row) {
				switch (cell.getCellType()) {
					case STRING:
						//TODO: Add metadata information
						data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
						break;

					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							data.get(i).add(cell.getDateCellValue() + "");
						} else {
							data.get(i).add(cell.getNumericCellValue() + "");
						}
						break;

					case BOOLEAN: data.get(i).add(cell.getBooleanCellValue() + "");
					break;

					case FORMULA: data.get(i).add(cell.getCellFormula() + "");
					break;

					default: data.get(new Integer(i)).add(" ");
				}
			}
			i++;
		}



//		Supplier<Stream<Row>> rowStreamSupplier = uploadUtil.getRowStreamSupplier(sheet);
//
//		Row headerRow = rowStreamSupplier.get().findFirst().get();
//
//		List<String> headerCells = uploadUtil.getStream(headerRow)
//				.map(Cell::getNumericCellValue)
//				.map(String::valueOf)
//				.collect(Collectors.toList());
//
//		int colCount = headerCells.size();
//
//		return rowStreamSupplier.get()
//				.skip(1)
//				.map(row -> {
//
//					List<String> cellList = uploadUtil.getStream(row)
//							.map(Cell::getStringCellValue)
//							.collect(Collectors.toList());
//
//					return uploadUtil.cellIteratorSupplier(colCount)
//							 .get()
//							 .collect(toMap(headerCells::get, cellList::get));
//		})
//		.collect(Collectors.toList());

		//TODO: read on serializing data to json
		return  data;
	}

}
