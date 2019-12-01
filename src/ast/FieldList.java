/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class FieldList {

	public FieldList(String type, ArrayList<String> idList) {
		this.type = type;
		this.idList = idList;
	}
	
	
	public void genJava(PW pw) {
		pw.printIdent("private " + type + " ");
		
		Iterator<String> it = idList.listIterator();
		pw.print(it.next());
		while(it.hasNext()){
			pw.print(", " + it.next());
		}
		
		pw.println(";");
	}
	
	private String type;
	private ArrayList<String> idList;
}
