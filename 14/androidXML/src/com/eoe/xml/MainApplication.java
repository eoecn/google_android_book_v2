
package com.eoe.xml;

import java.util.List;

import android.app.Application;


public class MainApplication extends Application {
	
	private List<Person>  students;
	
	public List<Person> getStudents() {
		return students;
	}

	public void setStudents(List<Person> students) {
		this.students = students;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
}
