/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class ExprPar extends Factor {

	public ExprPar(Expr expr) {
		this.expr = expr;
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
		pw.print(" ( ");
		expr.genJava(pw);
		pw.print(" ) ");
	}
	
	private Expr expr;
}
