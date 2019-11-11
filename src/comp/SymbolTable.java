/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package comp;
import ast.Type;
import java.util.HashMap;

public class SymbolTable {
	public HashMap<String, Type> localTable;
	
	public SymbolTable(){
		localTable = new HashMap<String, Type>();
	}
	
	public void putLocal(String s,Type t) {
		this.localTable.put(s, t);
	}
	
	public Type getLocal(String s) {
		return this.localTable.get(s);
	}
	public boolean clearLocal() {
		try {
			this.localTable.clear();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
			
	}
}
