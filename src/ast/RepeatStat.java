/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class RepeatStat extends Statement {

	public RepeatStat(Expr expr, ArrayList<Statement> statList) {
		this.expr = expr;
		this.statList = statList;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printlnIdent("do {");
		pw.add();
		
		if (!(statList.isEmpty())) {
			Iterator<Statement> it = statList.listIterator();
			it.next().genJava(pw);
			while(it.hasNext()){
				it.next().genJava(pw);
			}
		}
		
		pw.sub();
		pw.printIdent("} while(");
		
		expr.genJava(pw);
		pw.println(");");
	}
	
	private Expr expr;
	private ArrayList<Statement> statList;
}
