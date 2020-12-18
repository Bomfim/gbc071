package com.ufu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Token {

    TK_NUMBER("-?\\d+(\\.\\d+)?(E[+-]?\\d+)?"),
    TK_PROGRAM("programa"),

    //Arithimetic
    TK_MINUS("-"),
    TK_PLUS("\\+"),
    TK_MUL("\\*"),
    TK_DIV("/"),

    //Relational
    TK_LE("<="),
    TK_GTE(">="),
    TK_EQ("=="),
    TK_NE("<>"),
    TK_LT("<"),
    TK_GT(">"),

    //Symbols
    TK_ASSIGN(":\\="),
    TK_SEMICOLON(";"),
    TK_COMMA(","),
    TK_OPEN_PARENTHESIS("\\("),
    TK_CLOSE_PARENTHESIS("\\)"),
    TK_OPEN_BLOCK("\\{"),
    TK_CLOSE_BLOCK("\\}"),
    TK_COMMENT("\\[\\w*\\]"),

    //Conditional
    TK_ELSE("senao"),
    TK_THEN("entao"),
    TK_IF("se"),

    //Loops
    TK_DO("faca"),
    TK_WHILE("enquanto"),


    //Types
    TK_INT("int"),
    TK_REAL("real"),
    TK_CHAR("char"),

    //Keywords
    TK_ID("\\w+");

    private final Pattern pattern;

    Token(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}