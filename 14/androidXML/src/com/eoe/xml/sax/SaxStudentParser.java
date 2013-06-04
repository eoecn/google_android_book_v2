package com.eoe.xml.sax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;
import com.eoe.xml.Person;
import com.eoe.xml.model.StudentParser;

public class SaxStudentParser implements StudentParser {

	@Override
	public List<Person> parse(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		SAXParserFactory factory = SAXParserFactory.newInstance(); // 取得SAXParserFactory对象
		SAXParser parser = factory.newSAXParser(); // 利用获取到的对象创建解析器实例
		MyHandler handler = new MyHandler(); // 实例化自定义Handler
		parser.parse(is, handler); // 根据自定义Handler规则解析输入流
		return handler.getPeoples();
	}

	// 需要重写DefaultHandler的方法
	private class MyHandler extends DefaultHandler {

		private List<Person> students;
		private Person student;
		private StringBuilder builder;

		// 返回解析后得到的Book对象集合
		public List<Person> getPeoples() {
			return students;
		}
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			students = new ArrayList<Person>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("student")) {
				student = new Person();
				student.setId(new Integer(attributes.getValue("id")));
			}
			builder.setLength(0); // 将字符长度设置为0 以便重新开始读取元素内的字符节点
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length); // 将读取的字符数组追加到builder中
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equalsIgnoreCase("name")) {
				student.setName(builder.toString());
			} else if (localName.equalsIgnoreCase("age")) {
				student.setAge(new Integer(builder.toString()));
			} else if (localName.equalsIgnoreCase("student")) {
				students.add(student);
			}
		}
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}
	}
}
