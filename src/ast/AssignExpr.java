/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class AssignExpr extends Statement {

	public AssignExpr(Expression expr1, Expression expr2) {
		this.expr1 = expr1;
		this.expr2 = expr2;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent("");
		expr1.genJava(pw);

		if( expr2 != null) {
			pw.print(" = ");
			expr2.genJava(pw);
		}
		
		pw.println(";");
	}

	private Expression expr1;
	private Expression expr2;
}
