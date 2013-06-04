package com.eoe.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.eoe.xml.Person;
import com.eoe.xml.model.StudentParser;

public class DomParser implements StudentParser {
	
	@Override
	public List<Person> parse(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		return getRiversFromXml(is);
	}
	
	// 获取全部学生数据
	/**
	 * 参数fileName：为xml文档路径
	 */
	public List<Person> getRiversFromXml(InputStream inStream) {
		List<Person> students = new ArrayList<Person>();
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;
		// 获取DOM解析的工厂
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder(); //获取解析器
			document = builder.parse(inStream); // 加载XML文档
			// 找到根Element
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("student");
			// 遍历根节点所有子节点,students下所有student
			Person student = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				student = new Person();
				// 获取student元素节点
				Element studentElement = (Element) (nodes.item(i));
				// 获取student中id属性值
				student.setId(new Integer(studentElement.getAttribute("id")));
				// 获取student下name标签
				Element introduction = (Element) studentElement
						.getElementsByTagName("name").item(0);
				student.setName(introduction.getFirstChild().getNodeValue());
				// 获取student下age标签
				Element imageUrl = (Element) studentElement.getElementsByTagName(
						"age").item(0);
				student.setAge(new Integer(imageUrl.getFirstChild()
						.getNodeValue()));
				students.add(student);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		
		return students;
	}
}
