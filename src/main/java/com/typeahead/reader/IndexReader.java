package com.typeahead.reader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.typeahead.exceptions.IndexAlreadyExistException;
import com.typeahead.exceptions.IndexDoesNotExistException;
import com.typeahead.index.Document;
import com.typeahead.index.Index;
import com.typeahead.reader.services.IndexReaderService;
import com.typeahead.writer.IndexWriterUtil;


/**
 * IndexReader is class used to create/read index from disk. 
 * @author ronakkhunt
 */
public class IndexReader {
	
	IndexReaderService readerService;
	public IndexReader() {
		readerService = new IndexReaderService();
		
	}
	
	/**
	 * Create new {@link Index}. 
	 * @param indexName
	 * @return
	 * @throws IndexAlreadyExistException
	 */
	public Index createIndex(String indexName) throws IndexAlreadyExistException {
		Index index = new Index(indexName);;
		IndexWriterUtil writerUtil = new IndexWriterUtil(index);
		try {
			writerUtil.createIndexFiles();
		} catch (IOException e) {
			//TODO: This IOException needs to be handled explicitly
		}
		return index;
	}
	
	/**
	 * Opens the existing {@link Index}
	 * @param indexName
	 * @return
	 * @throws IndexDoesNotExistException 
	 */
	@SuppressWarnings("unchecked")
	public Index openIndex(String indexName) throws IndexDoesNotExistException {
		
		Index index = new Index(indexName);
		IndexWriterUtil writerUtil = new IndexWriterUtil(index);
		
		if(!writerUtil.doesIndexExistance()) {
			throw new IndexDoesNotExistException("Index: "+indexName+" does not exist.");
		}	
		
		File indexDataMap = writerUtil.getDataMapFile();
		File fieldFSTMap = writerUtil.getFieldFSTMapFile();
		File mapping = writerUtil.getMappingFile();
		File metadata = writerUtil.getMetadataFile();
		
		index.setDataMap(readerService.read(indexDataMap, HashMap.class));
		index.setFieldFSTMap(readerService.read(fieldFSTMap, HashMap.class));
		index.recoverMapping(readerService.read(mapping, HashMap.class));
		index.setMetadata(readerService.read(metadata, HashMap.class));
		return index;
	}
	
	/**
	 * Created the {@link Index}, if it does not exist, otherwise opens the Existing {@link Index}
	 * @param indexName
	 * @return
	 */
	public Index createOrOpenIndex(String indexName) {
		return null;
	}
	
	/**
	 * Delete {@link Index}
	 * @param indexName
	 * @throws IndexDoesNotExistException 
	 */
	public void deleteIndex(String indexName) throws IndexDoesNotExistException {
		
		Index index = new Index(indexName);
		IndexWriterUtil writerUtil = new IndexWriterUtil(index );
		
		writerUtil.deleteIndexFiles();
	}
}
