/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class SumSubExpr extends Expr {

	public SumSubExpr(ArrayList<Term> term, ArrayList<String> operator) {
		this.term = term;
		this.operator = operator;
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

		Iterator<Term> it = term.listIterator();
		Iterator<String> op = operator.listIterator();
		
		it.next().genJava(pw);

		while(it.hasNext()) {
			pw.print(" " + op.next() + " ");
			it.next().genJava(pw);
		}
	
	}

	private ArrayList<Term> term;
	private ArrayList<String> operator;
}
