grammar tiger;

tokens{Typedef,Void,Char,Int,Struct,Union,If,Else,While,For,Continue,Break,Return,Sizeof}

@lexer::header
{
	package parser;
	import java.util.*;
}


@lexer::members
{
	@Override public void recover(LexerNoViableAltException e) 
	{
		throw new RuntimeException(e);
	}
	Map <String,Integer> keywords=new HashMap <String,Integer>()
	{{
		put("typedef",tigerParser.Typedef);
		put("void",tigerParser.Void);
		put("char",tigerParser.Char);
		put("int",tigerParser.Int);
		put("struct",tigerParser.Struct);
		put("union",tigerParser.Union);
		put("if",tigerParser.If);
		put("else",tigerParser.Else);
		put("while",tigerParser.While);
		put("for",tigerParser.For);
		put("continue",tigerParser.Continue);
		put("break",tigerParser.Break);
		put("return",tigerParser.Return);
		put("sizeof",tigerParser.Sizeof);
	}};
}

@parser::header 
{
	package parser;
}

program: (declaration | function_definition)+ 
	   ;

declaration: Typedef type_specifier declarators ';'
           | type_specifier init_declarators? ';'
           ;
        
function_definition: type_specifier plain_declarator '(' parameters? ')' compound_statement
				   ;

parameters: plain_declaration (',' plain_declaration)* (',' '...')?
          ;

declarators: declarator (',' declarator)* 
           ;

init_declarators: init_declarator (',' init_declarator)*
				;

init_declarator: declarator ('=' initializer)?
			   ;

initializer: assignment_expression
           | '{' initializer (',' initializer)* '}'
           ;
        
type_specifier: Void | Char | Int | typedef_name
              | struct_or_union identifier? '{' (type_specifier declarators ';')+ '}'
              | struct_or_union identifier
              ;
           
struct_or_union: Struct | Union
			   ;

plain_declaration: type_specifier declarator
				 ;

declarator: plain_declarator '(' parameters? ')'
          | plain_declarator ('[' constant_expression ']')*
          ;
       
plain_declarator: '*'* identifier
				;

statement: expression_statement
         | compound_statement
         | selection_statement
         | iteration_statement
         | jump_statement
         ;
      
expression_statement: expression? ';' 
					;

compound_statement: '{' declaration* statement* '}' 
			      ;

selection_statement: If '(' expression ')' statement (Else statement)? 
				   ;

iteration_statement: While '(' expression ')' statement
                   | For '(' expression? ';' expression? ';' expression? ')' statement
                   ;
                
jump_statement: Continue ';'
              | Break ';'
              | Return expression? ';'
              ;
           
expression: assignment_expression (',' assignment_expression)* 
		  ;

assignment_expression: logical_or_expression
                     | unary_expression assignment_operator assignment_expression
                     ;

assignment_operator: '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|='
				   ;

constant_expression: logical_or_expression
				   ;

logical_or_expression: logical_and_expression ('||' logical_and_expression)*
					 ;

logical_and_expression: inclusive_or_expression ('&&' inclusive_or_expression)*
					  ;

inclusive_or_expression: exclusive_or_expression ('|' exclusive_or_expression)*
					   ;

exclusive_or_expression: and_expression ('^' and_expression)*
					   ;

and_expression: equality_expression ('&' equality_expression)*
			  ;

equality_expression: relational_expression (equality_operator relational_expression)*
				   ;

equality_operator: '==' | '!='
				 ;

relational_expression: shift_expression (relational_operator shift_expression)*
					 ;

relational_operator: '<' | '>' | '<=' | '>='
				   ;

shift_expression: additive_expression (shift_operator additive_expression)*
				;

shift_operator: '<<' | '>>'
		      ;

additive_expression: multiplicative_expression (additive_operator multiplicative_expression)*
				   ;

additive_operator: '+' | '-'
				 ;

multiplicative_expression: cast_expression (multiplicative_operator cast_expression)*
						 ;

multiplicative_operator: '*' | '/' | '%'
					   ;

cast_expression: unary_expression
               | '(' type_name ')' cast_expression
               ;

type_name: type_specifier '*'* 
         ;

unary_expression: postfix_expression
                | '++' unary_expression
                | '--' unary_expression
   		        | unary_operator cast_expression
             	| Sizeof unary_expression
	            | Sizeof '(' type_name ')'
	            ;

unary_operator: '&' | '*' | '+' | '-' | '~' | '!'
			  ;

postfix_expression: primary_expression postfix*
 				  ;

postfix: '[' expression ']'
   	   | '(' arguments? ')'
	   | '.' identifier
       | '->' identifier
       | '++'
       | '--' 
       ;

arguments: assignment_expression (',' assignment_expression)*
		 ;

primary_expression: identifier
                  | constant
                  | string
                  | '(' expression ')'
                  ;

constant: integer_constant
        | character_constant
        ;

typedef_name:ID
			;

identifier:ID 
		  ;

integer_constant:DEC
				|OCT
				|HEX
				;

character_constant:CHAR
				  ;

string:STR
	  ;

ID:[A-Za-z_][A-Za-z_0-9]* 
		{ if (keywords.containsKey(getText())) setType(keywords.get(getText())); }
  ;


DEC:[1-9][0-9]*
   ;
   
OCT:'0'[0-7]*
   ;
   
HEX:'0'('x'|'X')[0-9a-fA-F]+
   ;

CHAR: '\'' ('\\'.|~[\\\']) '\''
	;
	
STR:'"' ('\\'.|.)*? '"' 
   ;

WS:[ \t\f\r\n]+ -> skip
  ;

LINE_COMMENT:'//' .*? '\n' -> skip
			;
			
COMMENT:'/*' .*? '*/' -> skip
	   ;

PREPROCESS:'#' .*? '\n' -> skip
		  ;
