package com.ufu;

public class Main {

    public static void main(String[] args) {
        /*Lexer lexer = new Lexer("/Users/mariocesarmelonbomfim/code.cc");

        while (!lexer.isExausthed()) {
            System.out.printf("%-18s :  %s \n", lexer.currentLexema(), lexer.currentToken());
            lexer.moveAhead();
        }
        System.out.println(lexer.getIdenfiers());

        if (lexer.isSuccessful()) {
            System.out.println("Ok! :D");
        } else {
            System.out.println(lexer.errorMessage());
        }*/


        Parser p = new Parser();
        p.parse();
        System.out.println("The syntax of the source program is correct.");

    }
}
