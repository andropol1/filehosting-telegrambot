package ru.andropol1.dto;

public interface DocumentData {
	String getDocName();
	String getMimeType();
	byte[] getFileAsArrayOfBytes();
}