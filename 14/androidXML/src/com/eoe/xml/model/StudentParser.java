package com.eoe.xml.model;

import java.io.InputStream;
import java.util.List;

import com.eoe.xml.Person;

public interface StudentParser {
	/** 
     * 解析输入流 得到Student对象集合 
     */  
    public List<Person> parse(InputStream is) throws Exception;  
}
