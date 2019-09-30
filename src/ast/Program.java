/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<TypeCianetoClass> classList, ArrayList<MetaobjectAnnotation> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}


	public void genJava(PW pw) {
	}

	public void genC(PW pw) {
	}
	
	public ArrayList<TypeCianetoClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectAnnotation> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	public void setMainJavaClassName(String mainJavaClassName) {
		this.mainJavaClassName = mainJavaClassName;
	}
	/**
	the name of the main Java class when the
	code is generated to Java. This name is equal
	to the file name (without extension)
	 */
	private String mainJavaClassName;
	
	private ArrayList<TypeCianetoClass> classList;
	private ArrayList<MetaobjectAnnotation> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}