/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class WriteStat extends Statement {

	public WriteStat(boolean println, ArrayList<Expr> expr) {
		this.println = println;
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent("System.out.print");
		if ( println == true )
			pw.print("ln");
		pw.print("(");
		
		Iterator<Expr> it = expr.listIterator();
		it.next().genJava(pw);
		while(it.hasNext()){
			pw.print(" + ");
			it.next().genJava(pw);
		}
		
		pw.println(");");
	}

	private boolean println;
	private ArrayList<Expr> expr;
}
