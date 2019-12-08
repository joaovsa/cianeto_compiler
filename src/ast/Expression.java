/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class Expression extends Expr {

	public Expression(Expr se1, String relation, Expr se2) {
		this.se1 = se1;
		this.se2 = se2;
		this.relation = relation;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {		
	}

	@Override
	public Type getType() {				
		return this.se1.getType();
	}

	@Override
	public void genJava(PW pw) {
		se1.genJava(pw);
		if(relation != null) {
			pw.print(" " + relation + " ");
			se2.genJava(pw);
		}
	}

	private Expr se1;
	private Expr se2;
	private String relation;
}