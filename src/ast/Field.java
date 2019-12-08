/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

public class Field {

	public Field(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	String name;
	String type;
}
