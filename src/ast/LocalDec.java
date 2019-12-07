/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class LocalDec extends Statement {

	public LocalDec(String type, ArrayList<String> idList, Expr expr) {
		this.type = type;
		this.idList = idList;
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent(type + " ");
		
		Iterator<String> it = idList.listIterator();
		pw.print(it.next());
		while(it.hasNext()){
			pw.print(", " + it.next());
		}
		
		if( expr != null ) {
			pw.print(" = ");
			expr.genJava(pw);
		}
		
		pw.println(";");
	}

	private String type;
	private ArrayList<String> idList;
	private Expr expr;
}
