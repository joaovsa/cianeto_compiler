/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class NotFactor extends Factor {

	public NotFactor(Expr factor) {
		this.factor = factor;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return this.factor.getType();
	}

	@Override
	public void genJava(PW pw) {
		pw.print(" !(");
		factor.genJava(pw);
		pw.print(") ");
	}

	private Expr factor;
}
