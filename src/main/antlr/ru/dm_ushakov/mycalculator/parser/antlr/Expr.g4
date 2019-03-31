grammar Expr;

@header{
package ru.dm_ushakov.mycalculator.parser.antlr;
}

line        : expr EOF;

expr        : '(' expr ')' # parenthesis
            | assignFunc # assignFuncExpr
            | assignVar # assignVarExpr
            | expr op=('=='|'>='|'<='|'>'|'<') expr # comparation
            | expr '?' expr ':' expr # ternary
            | expr op=('*'|'/') expr # mulDiv
            | expr op=('+'|'-') expr # plusMinus
            | funcCall # funcCallExpr
            | ID # variable
            | NUM # number
            | '-' NUM # negativeNumber
            ;

assignVar  : ID '=' expr;

assignFunc : funcPattern '=' expr;

funcPattern: ID '(' (ID (',' ID)*)? ')';

funcCall: ID '(' (expr (',' expr)*)? ')';

ID          : [a-zA-Z][a-zA-Z0-9]*;
NUM         : [0-9]+ ('.' [0-9]+)?;
WS          : [ \t]+ -> skip;

ASSIGN      : '=';
MUL         : '*';
DIV         : '/';
PLUS        : '+';
MINUS       : '-';

COMP_EQ     : '==';
COMP_GTE    : '>=';
COMP_LTE    : '<=';
COMP_GT     : '>';
COMP_LT     : '<';

