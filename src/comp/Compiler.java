/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;

import ast.*;
import ast.TypeInt;
import ast.TypeNull;
import ast.TypeString;
import ast.TypeUndefined;
import ast.CompositeExpr;
import ast.Expr;
import ast.Type;
import ast.TypeBoolean;
import lexer.Lexer;
import lexer.Token;


public class Compiler {

	public Compiler() { 
		
	}

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classDec();
			}
			catch ( Throwable e ) {
	            e.printStackTrace();
	            thereWasAnError = true;
	            try {
	                error("Exception '" + e.getClass().getName() + "' was thrown and not caught. "
	                        + "Its message is '" + e.getMessage() + "'");
	            }
	            catch( CompilerError ee) {
	            }
	            return program;
	        }

		}
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		return program;
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			int sizeParamList = metaobjectParamList.size();
			if ( sizeParamList < 2 || sizeParamList > 4 )
				error("Annotation 'cep' takes two, three, or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer) ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) )
				error("The second parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 3 && !( metaobjectParamList.get(2) instanceof String) )
				error("The third parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 4 && !( metaobjectParamList.get(3) instanceof String) )
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	private void classDec() {
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			// open class
			lexer.nextToken();
		}
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Token.ID )
			error("Identifier expected");
		String className = lexer.getStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID )
				error("Identifier expected");
			String superclassName = lexer.getStringValue();

			lexer.nextToken();
		}

		memberList();
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();

	}

	private void memberList() {
		while ( true ) {
			//optional inside method
			qualifier();
			
			if ( lexer.token == Token.VAR ) {
				fieldDec();
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec();
			}
			else {
				break;
			}
		}
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}


	private void next() {
		lexer.nextToken();
	}

	private void check(Token shouldBe, String msg) {
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}
	
	private void checkSemiColon(String msg) {
		if ( lexer.token != Token.SEMICOLON )
			this.signalError.showError(msg, true);
	}

	private void methodDec() {
		lexer.nextToken();
		if ( lexer.token == Token.ID ) {
			// unary method
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters
			lexer.nextToken();
			formalParamDec();
		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		next();
		statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		//clears local table after processing it
		symbolTable.clearLocal();
		
		next();
	}

	private void formalParamDec() {
		type();
		check(Token.ID, "A variable name was expected");
		next();
		while ( lexer.token == Token.COMMA ) {
			next();
			type();
			check(Token.ID, "A variable name was expected");
			next();
		}
	}
	
	private void statementList() {
		  // only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
	}

	private Statement statement() {
		boolean checkSemiColon = true;
		switch ( lexer.token ) {
		case IF:
			ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			returnStat();
			break;
		case BREAK:
			breakStat();
			break;
		case SEMICOLON:
			//next();
			break;
		case REPEAT:
			repeatStat();
			break;
		case VAR:
			localDec();
			break;
		case ASSERT:
			assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				writeStat();
			} else {
				Expr leftside = expr();
				if( lexer.token == Token.ASSIGN ) {
					Token t = lexer.token;
					String possible_var;
					next();
					
					possible_var = lexer.getStringValue();
					Expr rightside = expr();
					if (symbolTable.getLocal(possible_var) != rightside.getType()) {
						String str= "'"+symbolTable.getLocal(possible_var)+"' cannot be assigned to '"+rightside.getType()+"'";
						error(str);
						//ou deveria ser check
					}
					//aux func, not in AST
					CompositeExpr cexp = new CompositeExpr(leftside, t, rightside);
					return cexp;
				}
			//TODO: change
			return null;
			}
		}
		if ( checkSemiColon ) {
			if ( lexer.token == Token.RIGHTPAR ) {
				error("')' unexpected");
			}
			checkSemiColon("';' expected");
			next();
		}
		//TODO: change
		return null;
	}

	private void localDec() {
		//armazena tipos de uma sequencia de declaração de variável
		Type t;
		
		next();
		t = type();
		check(Token.ID, "A variable name was expected");
		while ( lexer.token == Token.ID ) {
			symbolTable.putLocal(Token.ID.toString(), t);
			next();
			
			if ( lexer.token == Token.COMMA ) {
				next();
				check(Token.ID, "A variable name was expected");
			}
			else {
				break;
			}
		}
		if ( lexer.token == Token.ASSIGN ) {
			//TODO: check type after declaration
			next();
			// check if there is just one variable
			expr();
		}

	}

	private void repeatStat() {
		next();
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.UNTIL, "missing keyword 'until'");
		next();
		expr();
	}

	private void breakStat() {
		next();

	}

	private void returnStat() {
		next();
		expr();
	}

	private void whileStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();
	}

	private void ifStat() {
		next();
		expr();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		next();
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				statement();
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}
	}

	private void writeStat() {
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");
		next();
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		next();
		String printName = lexer.getStringValue();
		if ( lexer.token == Token.SEMICOLON )
			error("Command ' Out.print' without arguments");
		expr();
		while ( lexer.token == Token.COMMA ) {
			next();
			expr();
		}
	}

	private Expr expr() {
		Expr expr;
		Expr left;
		Expr right;
		Token t;
		
		left = simpleExpr();
		if ( lexer.token == Token.EQ || lexer.token == Token.LT || lexer.token == Token.GT ||
				lexer.token == Token.GE || lexer.token == Token.LE || lexer.token == Token.NEQ ) {
			t = lexer.token;
			next();
			right= simpleExpr();
			expr = new CompositeExpr(left, t, right);
			
		}
		else {
			
			expr = left;
		}
		return expr;
	}
	
	private Expr simpleExpr() {
		//TODO: fix returns
		Expr e;
		e = sumSubExpr();
		while ( lexer.token == Token.PLUSPLUS ) {
			next();
			e = sumSubExpr();
		}
		return e;
	}
	
	private Expr sumSubExpr() {
		//TODO: fix returns
		Expr e;
		e = term();
		while ( lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR ) {
			next();
			e = term();
		}
		return e;
	}

	private Expr term() {
		//TODO: fix return, maybe signal factor list?
		Expr e;
		e =  signalFactor();
		while ( lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND ) {
			next();
			e = signalFactor();
		}
		return e;
	}

	private Expr signalFactor() {
		if( lexer.token == Token.PLUS || lexer.token == Token.MINUS ) {
			next();
		}
		return factor();
	}
	
	private Expr factor() {
		int numval;
		switch ( lexer.token ) {
		case LEFTPAR:
			next();
			expr();
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			next();
			break;
		case NOT:
			next();
			factor();
			break;
		case NULL:
			next();
			break;
		default:
			if ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING || lexer.token == Token.FALSE 
					|| lexer.token == Token.TRUE ) {
				numval = lexer.getNumberValue();
				next();
				return new LiteralInt(numval);
				
			}
			else if ( lexer.token == Token.ID && lexer.getStringValue().equals("In") ) {
				next();
				if ( lexer.token != Token.DOT )
					error("'.' expected");
				next();
				if ( lexer.token == Token.ID && lexer.getStringValue().equals("readInt") ) {
					next();
				}
				else if ( lexer.token == Token.ID && lexer.getStringValue().equals("readString") ) {
					next();
				}
				else {
					error("'readInt' or 'readString' expected");
				}
			}
			else if ( lexer.token == Token.ID ) {
				next();
				if ( lexer.token == Token.DOT ) {
					next();
					if ( lexer.token == Token.ID && lexer.getStringValue().equals("new") ) {
						next();
					}
					else if ( lexer.token == Token.IDCOLON ) {
						next();
						expr();
						while ( lexer.token == Token.COMMA ) {
							next();
							expr();
						}
					}
					else if ( lexer.token == Token.ID ) {
						next();
					}
				}
			}
			else if ( lexer.token == Token.SUPER ) {
				next();
				if ( lexer.token != Token.DOT )
					error("'.' expected");
				next();
				if ( lexer.token == Token.ID ) {
					next();
				}
				else if ( lexer.token == Token.IDCOLON ) {
					next();
					expr();
					while ( lexer.token == Token.COMMA ) {
						next();
						expr();
					}
				}
			}
			else if ( lexer.token == Token.SELF ) {
				next();
				if ( lexer.token == Token.DOT ) {
					next();
					if ( lexer.token == Token.IDCOLON ) {
						next();
						expr();
						while ( lexer.token == Token.COMMA ) {
							next();
							expr();
						}
					}
					else if ( lexer.token == Token.ID ) {
						next();
						if ( lexer.token == Token.DOT ) {
							next();
							if ( lexer.token == Token.IDCOLON ) {
								next();
								expr();
								while ( lexer.token == Token.COMMA ) {
									next();
									expr();
								}
							}
							else if ( lexer.token == Token.ID ) {
								next();
							}
						}
					}
				}
			}
			else if ( lexer.token == Token.IDCOLON && lexer.getStringValue().equals("print:") ) {
				error("Missing 'Out.'");
			}
			else {
				error("Expression expected");
				//error("Statement expected");
			}		
		}
		//TODO: fix AST returns
		return null;
	}
	
	private void fieldDec() {
		lexer.nextToken();
		type();
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
					if ( lexer.token != Token.ID ) {
						this.error("A field name was expected");
					}
				}
				else {
					break;
				}
			}
			if ( lexer.token == Token.SEMICOLON ) {
				lexer.nextToken();
			}
		}

	}

	private Type type() {
		Type tp;		
		switch(lexer.token){
			case INT:
				tp = new TypeInt();
				break;
			case BOOLEAN:
				tp = new TypeBoolean();
				break;
			case STRING:
				tp = new TypeString();
				break;
			case NULL:
				//TODO: verify if null is creatable
				tp = new TypeNull();
			default:
				tp = new TypeUndefined();
				this.error("A type was expected");
		}
		//iterates token
		next();
		return tp;
	}


	private void qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			next();
		}
		else if ( lexer.token == Token.PUBLIC ) {
			next();
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
		}
		else if ( lexer.token == Token.FINAL ) {
			next();
			if ( lexer.token == Token.PUBLIC ) {
				next();
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				next();
				if ( lexer.token == Token.PUBLIC ) {
					next();
				}
			}
		}
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	public Statement assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		expr();
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		//if ( lexer.token == Token.SEMICOLON )
		//	lexer.nextToken();

		return null;
	}




	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;

}
