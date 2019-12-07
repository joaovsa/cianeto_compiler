/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class SimpleExpr extends Expr {

	public SimpleExpr(ArrayList<Expr> sse) {
		this.sse = sse;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {		
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public void genJava(PW pw) {

		Iterator<Expr> it = sse.listIterator();
		
		it.next().genJava(pw);
		while(it.hasNext()) {
			pw.print(" + ");
			it.next().genJava(pw);
		}
		
	}

	private ArrayList<Expr> sse;
}
