package com.oneliang.test.jxl;

import java.io.File;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableWorkbook;

public class Test {
	public static void main(String[] args){
		try {
			String path=Test.class.getResource("/").getPath()+"com/lwx/test/jxl";
			String excelFile=path+"/test.xls";
			String newExcelFile=path+"/test_copy.xls";
			Workbook workbook=Workbook.getWorkbook(new File(excelFile));
			WritableWorkbook writableWorkbook=Workbook.createWorkbook(new File(newExcelFile),workbook);
			Sheet[] sheets=writableWorkbook.getSheets();
			for(Sheet sheet:sheets){
				int columns=sheet.getColumns();
				for(int i=0;i<columns;i++){
					Cell[] cells=sheet.getColumn(i);
					for(Cell cell:cells){
						CellType cellType=cell.getType();
						if(cellType.equals(CellType.LABEL)){
							Label label=(Label)cell;
							String value=label.getString();
							label.setString(value);
						}
					}
				}
			}
			writableWorkbook.write();
			writableWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
