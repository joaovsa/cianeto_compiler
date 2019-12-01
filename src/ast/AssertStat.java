/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class AssertStat extends Statement {

	public AssertStat(Expression expr, String message) {
		this.expr = expr;
		this.message = message;
	}
	
	@Override
	public void genC(PW pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genJava(PW pw) {
		pw.printIdent("assert ");
		expr.genJava(pw);
		pw.print(" : \"");
		pw.print(message);
		pw.println("\";");
	}

	private Expression expr;
	private String message;
}
