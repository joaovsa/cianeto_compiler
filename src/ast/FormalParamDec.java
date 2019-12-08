/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class FormalParamDec {
	public FormalParamDec(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public void genJava(PW pw) {
		pw.print(type + " " + name);
	}
	
	public String getType() {
		return type;
	}
	
	private String name;
	private String type;

}
