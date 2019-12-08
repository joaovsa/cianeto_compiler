/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class IfStat extends Statement {
	
	public IfStat(Expr cond, ArrayList<Statement> stat, ArrayList<Statement> elsePart) {
		this.cond = cond;
		this.stat = stat;
		this.elsePart = elsePart;
	}

	public void genC(PW pw) {
	}

	public void genJava(PW pw) {
		pw.printIdent("if (");

		cond.genJava(pw);
		
		pw.println(") {");
		pw.add();
		
		Iterator<Statement> it = stat.listIterator();
		it.next().genJava(pw);
		while(it.hasNext()){
			it.next().genJava(pw);
		}
		
		pw.sub();
		pw.printlnIdent("}");
		
		
		if ( !(elsePart.isEmpty()) ){
			pw.println(" else {");
			pw.add();
			
			Iterator<Statement> itElse = elsePart.listIterator();
			itElse.next().genJava(pw);
			while(itElse.hasNext()){
				itElse.next().genJava(pw);
			}
			
			pw.sub();
			pw.printlnIdent("}");
		}
	}
	
	private Expr cond;
	private ArrayList<Statement> stat;
	private ArrayList<Statement> elsePart;
}
