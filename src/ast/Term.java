/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class Term extends Expr {

	public Term(ArrayList<SignalFactor> sf, ArrayList<String> operator) {
		this.sf = sf;
		this.operator = operator;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void genJava(PW pw) {
		Iterator<SignalFactor> it = sf.listIterator();
		Iterator<String> op = operator.listIterator();

		it.next().genJava(pw);

		while(it.hasNext()) {
			pw.print(" " + op.next() + " ");
			it.next().genJava(pw);
		}
		
	}
	
	private ArrayList<SignalFactor> sf;
	private ArrayList<String> operator;
}
