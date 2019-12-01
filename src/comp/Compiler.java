/* 	Joao Vitor de Sa Medeiros Santos	552585 *
 *	Vinicius Silva Salinas				726594 */

package comp;

import java.io.PrintWriter;
import java.util.ArrayList;

import ast.*;
import lexer.Lexer;
import lexer.Token;

public class Compiler {

	public Compiler() { }

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
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList, hasScannerProg);
		boolean thereWasAnError = false;
		while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT ) {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classDec(CianetoClassList);
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
		program.setHasScanner(hasScannerProg);
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

	private void classDec(ArrayList<TypeCianetoClass> CianetoClassList) {
		ArrayList<FieldList> fieldList = new ArrayList<>();;
		ArrayList<MethodList> publicMethodList = new ArrayList<>(), privateMethodList = new ArrayList<>();;
		String superclassName = null;
		
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
			superclassName = lexer.getStringValue();

			lexer.nextToken();
		}

		memberList(fieldList, publicMethodList, privateMethodList);
		if ( lexer.token != Token.END)
			error("'end' expected");
		lexer.nextToken();

		CianetoClassList.add(new TypeCianetoClass(className, fieldList, publicMethodList, privateMethodList, superclassName));
	}

	private void memberList(ArrayList<FieldList> fieldList, ArrayList<MethodList> publicMethodList, 
			ArrayList<MethodList> privateMethodList) {
		while ( true ) {
			boolean priv = false;
			if ( lexer.token == Token.PRIVATE )
				priv = true;
			qualifier();
			if ( lexer.token == Token.VAR ) {
				fieldList.add(fieldDec());
			}
			else if ( lexer.token == Token.FUNC ) {
				if(priv == false)
					publicMethodList.add(methodDec(false));
				else
					privateMethodList.add(methodDec(true));
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

	private MethodList methodDec(boolean priv) {
		String name = null;
		String rt_type = "void";
		ArrayList<FormalParamDec> paramDec = null;
		lexer.nextToken();
		if ( lexer.token == Token.ID ) {
			// unary method
			name = lexer.getStringValue();
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			// keyword method. It has parameters
			name = lexer.getStringValue();
			lexer.nextToken();
			paramDec = formalParamDec();
		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			rt_type = type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		next();
		ArrayList<Statement> statementList = statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		next();
		MethodList methodDec = new MethodList(priv, name, rt_type, paramDec, statementList, hasScanner);
		if (hasScanner == true)
			hasScanner = false;
		if (name.length() > 0 && name.charAt(name.length() - 1) == ':') {
	        name = name.substring(0, name.length() - 1);
	    }
		funcList.add(name);
		return methodDec;
	}

	private ArrayList<FormalParamDec> formalParamDec() {
		ArrayList<FormalParamDec> paramDec = new ArrayList<>();
		String name;
		String type;
		type = type();
		check(Token.ID, "A variable name was expected");
		name = lexer.getStringValue();
		paramDec.add(new FormalParamDec(name, type));
		next();
		while ( lexer.token == Token.COMMA ) {
			next();
			type = type();
			check(Token.ID, "A variable name was expected");
			name = lexer.getStringValue();
			paramDec.add(new FormalParamDec(name, type));
			next();
		}
		return paramDec;
	}
	
	private ArrayList<Statement> statementList() {
		// only '}' is necessary in this test
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		Statement stat;
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			stat = statement();
			if (stat != null)
			statementList.add(stat);
		}
		return statementList;
	}

	private Statement statement() {
		boolean checkSemiColon = true;
		Statement statement = null;
		switch ( lexer.token ) {
		case IF:
			statement = ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			statement = whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			statement = returnStat();
			break;
		case BREAK:
			breakStat();
			break;
		case SEMICOLON:
			//next();
			break;
		case REPEAT:
			statement = repeatStat();
			break;
		case VAR:
			statement = localDec();
			break;
		case ASSERT:
			statement = assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				statement = writeStat();
			} else {
				Expression expr1 = expr();
				Expression expr2 = null;
				if( lexer.token == Token.ASSIGN ) {
					next();
					expr2 = expr();
				}
				statement = new AssignExpr(expr1, expr2);
			}
		}
		if ( checkSemiColon ) {
			if ( lexer.token == Token.RIGHTPAR ) {
				error("')' unexpected");
			}
			checkSemiColon("';' expected");
			next();
		}
		return statement;
	}

	private LocalDec localDec() {
		next();
		String type = type();
		ArrayList<String> idList = new ArrayList<>();
		check(Token.ID, "A variable name was expected");
		while ( lexer.token == Token.ID ) {
			idList.add(lexer.getStringValue());
			next();
			if ( lexer.token == Token.COMMA ) {
				next();
				check(Token.ID, "A variable name was expected");
			}
			else {
				break;
			}
		}
		Expression expr = null;
		if ( lexer.token == Token.ASSIGN ) {
			next();
			// check if there is just one variable
			expr = expr();
		}
		return new LocalDec(type, idList, expr);
	}

	private RepeatStat repeatStat() {
		next();
		ArrayList<Statement> statList = new ArrayList<>();
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statList.add(statement());
		}
		check(Token.UNTIL, "missing keyword 'until'");
		next();
		Expression expr = expr();
		return new RepeatStat(expr, statList);
	}

	private void breakStat() {
		next();

	}

	private ReturnStat returnStat() {
		next();
		Expression expr = expr();
		return new ReturnStat(expr);
	}

	private WhileStat whileStat() {
		next();
		Expression expr = expr();
		ArrayList<Statement> statList = new ArrayList<>();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		next();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statList.add(statement());
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		next();
		return new WhileStat(expr, statList);
	}

	private IfStat ifStat() {
		next();
		Expression cond = expr();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		next();
		ArrayList<Statement> stat = new ArrayList<>();
		ArrayList<Statement> elsePart = new ArrayList<>();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE ) {
			stat.add(statement());
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		next();
		if ( lexer.token == Token.ELSE ) {
			next();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			next();
			while ( lexer.token != Token.RIGHTCURBRACKET ) {
				elsePart.add(statement());
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			next();
		}
		IfStat ifStat = new IfStat(cond, stat, elsePart);
		return ifStat;
	}

	private WriteStat writeStat() {
		next();
		check(Token.DOT, "a '.' was expected after 'Out'");
		next();
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		boolean println = false;
		if ( lexer.getStringValue().equals("println:") )
			println = true;
		next();
		if ( lexer.token == Token.SEMICOLON )
			error("Command ' Out.print' without arguments");
		ArrayList<Expression> expr = new ArrayList<>();
		expr.add(expr());
		while ( lexer.token == Token.COMMA ) {
			next();
			expr.add(expr());
		}
		return new WriteStat(println, expr);
	}

	private Expression expr() {
		SimpleExpr se1 = simpleExpr();
		if ( lexer.token == Token.EQ || lexer.token == Token.LT || lexer.token == Token.GT ||
				lexer.token == Token.GE || lexer.token == Token.LE || lexer.token == Token.NEQ ) {
			String relation = lexer.token.toString();
			next();
			SimpleExpr se2 = simpleExpr();
			return new Expression(se1, relation, se2);
		}
		return new Expression(se1, null, null);
	}
	
	private SimpleExpr simpleExpr() {
		ArrayList<SumSubExpr> sse = new ArrayList<>();
		sse.add(sumSubExpr());
		while ( lexer.token == Token.PLUSPLUS ) {
			next();
			sse.add(sumSubExpr());
		}
		return new SimpleExpr(sse);
	}
	
	private SumSubExpr sumSubExpr() {
		ArrayList<Term> term = new ArrayList<>();
		ArrayList<String> operator = new ArrayList<>();
		term.add(term());
		while ( lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR ) {
			operator.add(lexer.token.toString());
			next();
			term.add(term());
		}
		return new SumSubExpr(term, operator);
	}

	private Term term() {
		ArrayList<SignalFactor> sf = new ArrayList<>();
		ArrayList<String> operator = new ArrayList<>();
		sf.add(signalFactor());
		while ( lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND ) {
			operator.add(lexer.token.toString());
			next();
			sf.add(signalFactor());
		}
		return new Term(sf, operator);
	}

	private SignalFactor signalFactor() {
		String signal = null;
		if( lexer.token == Token.PLUS || lexer.token == Token.MINUS ) {
			signal = lexer.token.toString();
			next();
		}
		Factor factor = factor();
		return new SignalFactor(factor, signal);
	}
	
	private Factor factor() {
		switch ( lexer.token ) {
		case LEFTPAR:
			next();
			Expr expr = expr();
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			next();
			return new ExprPar(expr);
		case NOT:
			next();
			Factor factor = factor();
			return new NotFactor(factor);
		case NULL:
			next();
			return new NullExpr();
		default:
			String id1 = null, id2 = null, idColon = null;
			boolean sup = false, self = false, primary = false, funcId1 = false, funcId2 = false;
			ArrayList<Expression> exprList = new ArrayList<>();
			if ( lexer.token == Token.LITERALINT ) {
				int valor = lexer.getNumberValue();
				next();
				return new LiteralInt(valor);
			}
			else if ( lexer.token == Token.LITERALSTRING ) {
				String valor = lexer.getLiteralStringValue();
				next();
				return new LiteralString(valor);
			}
			else if ( lexer.token == Token.FALSE ) {
				next();
				return new LiteralBoolean(false);
			}
			else if ( lexer.token == Token.TRUE ) {
				next();
				return new LiteralBoolean(true);
			}
			else if ( lexer.token == Token.ID && lexer.getStringValue().equals("In") ) {
				next();
				boolean isInt = false;
				if ( lexer.token != Token.DOT )
					error("'.' expected");
				next();
				if ( lexer.token == Token.ID && lexer.getStringValue().equals("readInt") ) {
					next();
					isInt = true;
				}
				else if ( lexer.token == Token.ID && lexer.getStringValue().equals("readString") ) {
					next();
				}
				else {
					error("'readInt' or 'readString' expected");
				}
				hasScanner = true;
				hasScannerProg = true;
				return new ReadExpr(isInt);
			}
			else if ( lexer.token == Token.ID ) {
				primary = true;
				id1 = lexer.getStringValue();
				if ( funcList.contains(id1) )
					funcId1 = true;
				next();
				if ( lexer.token == Token.DOT ) {
					next();
					if ( lexer.token == Token.ID && lexer.getStringValue().equals("new") ) {
						primary = false;
						next();
						return new ObjectCreation(id1);
					}
					else if ( lexer.token == Token.IDCOLON ) {
						idColon = lexer.getStringValue();
						next();
						exprList.add(expr());
						while ( lexer.token == Token.COMMA ) {
							next();
							exprList.add(expr());
						}
					}
					else if ( lexer.token == Token.ID ) {
						id2 = lexer.getStringValue();
						if ( funcList.contains(id2) )
							funcId2 = true;
						next();
					}
				}
			}
			else if ( lexer.token == Token.SUPER ) {
				sup = true;
				primary = true;
				next();
				if ( lexer.token != Token.DOT )
					error("'.' expected");
				next();
				if ( lexer.token == Token.ID ) {
					id1 = lexer.getStringValue();
					if ( funcList.contains(id1) )
						funcId1 = true;
					next();
				}
				else if ( lexer.token == Token.IDCOLON ) {
					idColon = lexer.getStringValue();
					next();
					exprList.add(expr());
					while ( lexer.token == Token.COMMA ) {
						next();
						exprList.add(expr());
					}
				}
			}
			else if ( lexer.token == Token.SELF ) {
				primary = true;
				self = true;
				next();
				if ( lexer.token == Token.DOT ) {
					next();
					if ( lexer.token == Token.IDCOLON ) {
						idColon = lexer.getStringValue();
						next();
						exprList.add(expr());
						while ( lexer.token == Token.COMMA ) {
							next();
							exprList.add(expr());
						}
					}
					else if ( lexer.token == Token.ID ) {
						id1 = lexer.getStringValue();
						if ( funcList.contains(id1) )
							funcId1 = true;
						next();
						if ( lexer.token == Token.DOT ) {
							next();
							if ( lexer.token == Token.IDCOLON ) {
								idColon = lexer.getStringValue();
								next();
								exprList.add(expr());
								while ( lexer.token == Token.COMMA ) {
									next();
									exprList.add(expr());
								}
							}
							else if ( lexer.token == Token.ID ) {
								id2 = lexer.getStringValue();
								if ( funcList.contains(id2) )
									funcId2 = true;
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
			if ( primary == true ) 
				return new PrimaryExpr(self, sup, id1, funcId1, id2, funcId2, idColon, exprList);
		}
		return null;
	}
	
	private FieldList fieldDec() {
		lexer.nextToken();
		String type = type();
		ArrayList<String> idList = new ArrayList<>();
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				idList.add(lexer.getStringValue());
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
		return new FieldList(type, idList);
	}

	private String type() {
		String type = lexer.getStringValue();
		if ( lexer.token == Token.INT || lexer.token == Token.BOOLEAN ) {
			type = type.toLowerCase();
			next();
		}
		else if ( lexer.token == Token.STRING ) {
			next();
		}
		else if ( lexer.token == Token.ID ) {
			next();
		}
		else {
			this.error("A type was expected");
		}
		return type;
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

	private AssertStat assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		Expression expr = expr();
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

		return new AssertStat(expr, message);
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

	private SymbolTable			symbolTable;
	private ArrayList<String>	funcList = new ArrayList<>();
	private Lexer				lexer;
	private ErrorSignaller		signalError;
	private boolean 			hasScanner = false;
	private boolean 			hasScannerProg = false;
}