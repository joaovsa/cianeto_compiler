/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class WhileStat extends Statement {

	public WhileStat(Expression expr, ArrayList<Statement> statList) {
		this.expr = expr;
		this.statList = statList;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent("while(");
		expr.genJava(pw);
		pw.println(") {");
		pw.add();
		
		if (!(statList.isEmpty())) {
			Iterator<Statement> it = statList.listIterator();
			it.next().genJava(pw);
			while(it.hasNext()){
				it.next().genJava(pw);
			}
		}
		
		pw.sub();
		pw.printlnIdent("}");
	}
	
	private Expression expr;
	private ArrayList<Statement> statList;
}
