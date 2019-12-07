/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class ReturnStat extends Statement {

	public ReturnStat(Expr expr) {
		this.expr = expr;
	}

	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent("return ");
		expr.genJava(pw);
		pw.println(";");
	}
	
	private Expr expr;
}
