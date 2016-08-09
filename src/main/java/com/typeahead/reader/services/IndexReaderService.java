package com.typeahead.reader.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IndexReaderService {
	
	ObjectMapper mapper;
	
	public IndexReaderService() {
		mapper = new ObjectMapper();
	}
	
	public <T> T read(File file, Class<T> clazz) {
		try {
			return mapper.readValue(file,  clazz);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
