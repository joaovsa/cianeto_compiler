/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class Expression extends Expr {

	public Expression(SimpleExpr se1, String relation, SimpleExpr se2) {
		this.se1 = se1;
		this.se2 = se2;
		this.relation = relation;
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
		se1.genJava(pw);
		if(relation != null) {
			pw.print(" " + relation + " ");
			se2.genJava(pw);
		}
	}

	private SimpleExpr se1;
	private SimpleExpr se2;
	private String relation;
}