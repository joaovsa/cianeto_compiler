/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

abstract public class Expr extends Statement {
    abstract public void genC( PW pw, boolean putParenthesis );
	@Override
	public void genC(PW pw) {
		this.genC(pw, false);
	}

      // new method: the type of the expression
	//verificar se está na tabela de variaveis
    abstract public Type getType();
}