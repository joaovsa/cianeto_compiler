/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class MethodList {

	public MethodList(boolean priv, String name, String returnType, ArrayList<FormalParamDec> paramDec, 
			ArrayList<Statement> statementList, boolean hasScanner) {
		this.priv = priv;
		this.name = name;
		this.returnType = returnType;
		this.paramDec = paramDec;
		this.statementList = statementList;
		this.hasScanner = hasScanner;
	}
	
	public void genJava(PW pw) {
		if(this.priv == true)
			pw.printIdent("private ");
		else 
			pw.printIdent("public ");

		pw.print(returnType + " ");

		if (name.length() > 0 && name.charAt(name.length() - 1) == ':') {
	        name = name.substring(0, name.length() - 1);
	    }
		
		pw.print(name + "(");
		
		if(paramDec != null) {
			Iterator<FormalParamDec> it = paramDec.listIterator();
			it.next().genJava(pw);
		
			while(it.hasNext()){
				pw.print(", ");
				it.next().genJava(pw);
			}
		}
		
		pw.println(") {");
		pw.add();
		pw.println();
		
		if ( hasScanner )
			pw.printlnIdent("Scanner read = new Scanner(System.in);");
		
		Iterator<Statement> it = statementList.listIterator();
		while(it.hasNext()){
			it.next().genJava(pw);
		}
		
		pw.println();
		pw.sub();
		pw.printlnIdent("}");
	}
	
	public String getName() {
		return name;
	}

	private boolean priv;
	private boolean hasScanner;
	private String name;
	String returnType;
	ArrayList<FormalParamDec> paramDec;
	ArrayList<Statement> statementList;
}
