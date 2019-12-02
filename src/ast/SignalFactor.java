/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class SignalFactor extends Expr {

	public SignalFactor(Factor factor, String signal) {
		this.factor = factor;
		this.signal = signal;
	}
	public Factor getFactor() {
		return this.factor;
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
		if(signal != null)
			pw.print(signal + " ");
		factor.genJava(pw);
	}

	private Factor factor;
	private String signal;
}
