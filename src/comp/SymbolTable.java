/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package comp;

import java.util.Hashtable;

public class SymbolTable {

    public SymbolTable() {
        globalTable = new Hashtable();
        localTable  = new Hashtable();
        funcTable = new Hashtable();
    }
 
    public Object putInGlobal(String key, Object value) {
    	return globalTable.put(key, value);
    }

	public Object putInLocal(String key, Object value) {
	    return localTable.put(key, value);
	}
	
	public Object putInFunc(String key, Object value) {
	    return funcTable.put(key, value);
	}
	 
	public Object getInLocal(Object key) {
	    return localTable.get(key);
	}
	 
	public Object getInGlobal(Object key) {
		return globalTable.get(key);
	}
	
	public Object getInFunc(Object key) {
		return funcTable.get(key);
	}
    
	public void removeLocal() {
		localTable.clear();
	}
	
	public void removeFunc() {
		funcTable.clear();
	}
	
    private Hashtable globalTable, localTable, funcTable;
       
}
