/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class LiteralString extends Factor {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }

    
    public Type getType() {
        return Type.stringType;
    }
    
	public void genJava(PW pw) {
		//pw.print(literalString);
		pw.print("\""+ literalString +"\"");
	}
    
    private String literalString;
}
