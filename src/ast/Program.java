/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.*;
import comp.CompilationError;

public class Program {

	public Program(ArrayList<TypeCianetoClass> classList, ArrayList<MetaobjectAnnotation> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList, boolean hasScanner) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
		this.hasScanner = hasScanner;
	}


	public void genJava(PW pw) {
		if ( hasScanner ) {
			pw.printlnIdent("import java.util.Scanner;");
			pw.println();
		}
		//pw.printIdent("public class ");
		pw.printIdent("class ");
		pw.print(mainJavaClassName);
		pw.println(" {");
		pw.add();
		pw.printlnIdent("public static void main(String []args) {");
		pw.add();
		pw.printlnIdent("new Program().run();");
		pw.sub();
		pw.printlnIdent("}");
		pw.sub();
		pw.printlnIdent("}");
		pw.println();
		
		/*Iterator<MetaobjectAnnotation> it = metaobjectCallList.listIterator();
		while(it.hasNext()){
			it.next().genJava(pw);
		}*/
		
		Iterator<TypeCianetoClass> it = classList.listIterator();
		while(it.hasNext()){
			it.next().genJava(pw);
		}
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

	public void setHasScanner(boolean hasScanner) {
		this.hasScanner = hasScanner;
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
	private boolean hasScanner;
	
}