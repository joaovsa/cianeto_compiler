/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class ReadExpr extends Factor {

	public ReadExpr(boolean isInt) {
		this.isInt = isInt;
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
		if ( isInt )
			pw.print("read.nextInt()");
		else
			pw.print("read.next()");
	}

	private boolean isInt;
}
