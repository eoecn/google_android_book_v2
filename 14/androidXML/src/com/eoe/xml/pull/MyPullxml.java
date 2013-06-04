package com.eoe.xml.pull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.eoe.xml.Person;
import com.eoe.xml.model.StudentParser;

import android.util.Log;
import android.util.Xml;

public class MyPullxml implements StudentParser {

	@Override
	public List<Person> parse(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		return getDate(is);
	}

	public List<Person> getDate(InputStream inStream) {
		List<Person> persons = null;
		Person currentPerson = null;




		try {
			/**
			 * android提供了一个工具类'Xml'
			 * 通过这个工具类可以很方便地去new一个Pull的解析器,返回类型是XmlPullParser
			 **/
//			XmlPullParser xmlPull = Xml.newPullParser();
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();   //创建一个XmlPullParser解析的工厂
			factory.setNamespaceAware(true);
			XmlPullParser xmlPull = factory.newPullParser(); //获取一个解析实例

			xmlPull.setInput(inStream, "UTF-8");    //设置输入流的编码格式
			/**
			 * 触发事件getEventType() =返回事件码 当它遇到某个字符,如果符合xml的语法规范. 它就会出发这个语法所代表的数字
			 **/
			int eventCode = xmlPull.getEventType();

			/**
			 * 解析事件: StartDocument,文档开始 Enddocument,文档结束 每次读到一个字符,就产生一个事件
			 * 只要解析xml文档事件不为空,就一直往下读
			 **/
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: // 文档开始事件,可以做一些数据初始化处理
					persons = new ArrayList<Person>();
					break;

				case XmlPullParser.START_TAG:// 元素开始.
					String name = xmlPull.getName();
					if (name.equalsIgnoreCase("student")) {
						currentPerson = new Person();
						currentPerson.setId(new Integer(xmlPull
								.getAttributeValue(null, "id")));
					} else if (currentPerson != null) {
						if (name.equalsIgnoreCase("name")) {
							currentPerson.setName(xmlPull.nextText());
						} else if (name.equalsIgnoreCase("age")) {
							currentPerson.setAge(new Short(xmlPull.nextText()));
						}
					}
					break;
				case XmlPullParser.END_TAG: // 元素结束,
					if (currentPerson != null
							&& xmlPull.getName().equalsIgnoreCase("student")) {
						persons.add(currentPerson);
						currentPerson = null;
					}
					break;
				}
				eventCode = xmlPull.next();// 进入到一下一个元素.
			}
		} catch (XmlPullParserException e) {
			Log.i("eoe", e.toString());
		} catch (IOException e) {
			Log.i("eoe", e.toString());
		}
		return persons;
	}

}
