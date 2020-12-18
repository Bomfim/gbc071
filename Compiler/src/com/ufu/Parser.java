package com.ufu;

public class Parser {
    Lexer lexer = new Lexer("/Users/mariocesarmelonbomfim/code.cc");
    boolean stmtsFlag = true;
    boolean declFlag;
    TreeNode<String> ast;


    private void accept(Token expectedKind) {
        if (lexer.currentToken() == expectedKind)
            lexer.moveAhead();
        else
            handleError("Syntax error: " + lexer.currentToken().name() + " is not expected.");
    }

    private void handleError(String message) {
        System.out.println(message);
        System.exit(0);
    }

    public void parse() {

        System.out.printf("%-18s :  %s \n", lexer.currentLexema(), lexer.currentToken());
        parseProgram();
        if (!lexer.isExausthed())
            handleError("Syntax error");
    }

    private void parseProgram() {
        System.out.println("Enter parseProgram");
        ast = new TreeNode<>("parseProgram");
        accept(Token.TK_PROGRAM);
        accept(Token.TK_ID);
        accept(Token.TK_OPEN_BLOCK);
        parseStatements(ast);
        accept(Token.TK_CLOSE_BLOCK);

        System.out.println("Exit parseProgram");

    }


    private void parseStatements(TreeNode<String> parent) {
        System.out.println("Enter parseStatements");
        TreeNode<String> child = parent.addChild("parseStatements");

        while (stmtsFlag) {   //Statements --> Stmt*
            if (lexer.currentToken() == Token.TK_INT || lexer.currentToken() == Token.TK_REAL || lexer.currentToken() == Token.TK_CHAR) {
                lexer.moveAhead();
                declFlag = true;
                parseDeclarationStatements(child);
                if (lexer.currentToken() != Token.TK_INT && lexer.currentToken() != Token.TK_CHAR && lexer.currentToken() != Token.TK_REAL)
                    stmtsFlag = false;
            }
        }
        parseStatement(child);
        System.out.println("Exit parseStatements");
    }

    private void parseDeclarationStatements(TreeNode<String> parent) {
        TreeNode<String> child = parent.addChild("parseDeclarationStatements");
        System.out.println("Enter parseDeclarationStatements");
        while (declFlag) {
            parseDeclarationStmt(child);
        }
        System.out.println("Exit parseDeclarationStatements");
    }


    private void parseDeclarationStmt(TreeNode<String> parent) {
        TreeNode<String> child = parent.addChild("parseDeclarationStmt");
        System.out.println("Enter parseDeclarationStmt");
        if (lexer.currentToken() == Token.TK_ID) {
            if (lexer.peekNextToken() == Token.TK_COMMA) {
                accept(Token.TK_ID);
                accept(Token.TK_COMMA);
                parseDeclarationStmt(child);
            } else if (lexer.peekNextToken() == Token.TK_ASSIGN) {
                parseAssignment(child);
                declFlag = false;
            } else if (lexer.peekNextToken() == Token.TK_SEMICOLON) {
                accept(Token.TK_ID);
                accept(Token.TK_SEMICOLON);
                declFlag = false;
            }
        } else {
            handleError("Syntax Error");
        }

        System.out.println("Exit parseDeclarationStmt");

    }


    private void parseStatement(TreeNode<String> parent) {
        System.out.println("Enter parseStmt");
        TreeNode<String> child = parent.addChild("parseStmt");
        while (true) {
            if (lexer.currentToken() == Token.TK_ASSIGN || (lexer.peekNextToken() != null && lexer.peekNextToken() == Token.TK_ASSIGN)) {
                parseAssignment(child);
            } else if (lexer.currentToken() == Token.TK_IF) {
                parseConditional(child);
            } else if (lexer.currentToken() == Token.TK_DO || lexer.currentToken() == Token.TK_WHILE) {
                parseLoop(child);
            } else
                break;
        }
        System.out.println("Exit parseStmt");
    }

    private void parseAssignment(TreeNode<String> parent) {
        System.out.println("Enter parseAssignment");
        TreeNode<String> child = parent.addChild("parseAssignment");

        accept(Token.TK_ID);
        accept(Token.TK_ASSIGN);
        parseExpression(child);
        accept(Token.TK_SEMICOLON);

        System.out.println("Exit parseAssignment");
    }

    private void parseConditional(TreeNode<String> parent) {
        System.out.println("Enter parseConditional");
        TreeNode<String> child = parent.addChild("parseConditional");
        accept(Token.TK_IF);
        accept(Token.TK_OPEN_PARENTHESIS);
        parseExpression(child);
        accept(Token.TK_CLOSE_PARENTHESIS);
        accept(Token.TK_THEN);
        accept(Token.TK_OPEN_BLOCK);
        parseStatement(child);
        accept(Token.TK_CLOSE_BLOCK);
        if (lexer.currentToken() == Token.TK_ELSE) {
            accept(Token.TK_ELSE);
            accept(Token.TK_OPEN_BLOCK);
            parseStatement(child);
            accept(Token.TK_CLOSE_BLOCK);
        }

        System.out.println("Exit parseConditional");
    }

    private void parseLoop(TreeNode<String> parent) {
        System.out.println("Enter parseLoop");
        TreeNode<String> child = parent.addChild("parseConditional");

        if (lexer.currentToken() == Token.TK_WHILE) {
            accept(Token.TK_WHILE);
            accept(Token.TK_OPEN_PARENTHESIS);
            parseExpression(child);
            accept(Token.TK_CLOSE_PARENTHESIS);
            accept(Token.TK_OPEN_BLOCK);
            parseStatement(child);
            accept(Token.TK_CLOSE_BLOCK);
        } else if (lexer.currentToken() == Token.TK_DO) {
            accept(Token.TK_DO);
            accept(Token.TK_OPEN_BLOCK);
            parseStatement(child);
            accept(Token.TK_CLOSE_BLOCK);
            accept(Token.TK_WHILE);
            accept(Token.TK_OPEN_PARENTHESIS);
            parseExpression(child);
            accept(Token.TK_CLOSE_PARENTHESIS);
            accept(Token.TK_SEMICOLON);
        }

        System.out.println("Exit parseLoop");
    }

    private void parseExpression(TreeNode<String> parent) {
        System.out.println("Enter parseExpression");
        TreeNode<String> child = parent.addChild("parseExpression");

        if (lexer.currentToken() == Token.TK_OPEN_PARENTHESIS && lexer.peekNextToken() == Token.TK_NUMBER) {
            accept(Token.TK_OPEN_PARENTHESIS);
            parseExpression(child);
            accept(Token.TK_CLOSE_PARENTHESIS);
        }

        if (lexer.currentToken() == Token.TK_NUMBER || lexer.currentToken() == Token.TK_ID) {

            lexer.moveAhead();

            if (lexer.currentToken() == Token.TK_PLUS || lexer.currentToken() == Token.TK_MINUS ||
                    lexer.currentToken() == Token.TK_DIV || lexer.currentToken() == Token.TK_MUL) {
                parseOperation(child);
                parseExpression(child);
            } else if (lexer.currentToken() == Token.TK_GT || lexer.currentToken() == Token.TK_GTE ||
                    lexer.currentToken() == Token.TK_NE || lexer.currentToken() == Token.TK_EQ ||
                    lexer.currentToken() == Token.TK_LE || lexer.currentToken() == Token.TK_LT) {
                parseRelopOperation(child);
                parseExpression(child);
            }
        }
        System.out.println("Exit parseExpression");
    }

    private void parseRelopOperation(TreeNode<String> parent) {
        System.out.println("Enter parseRelopOperation");
        parent.addChild("parseRelopOperation");
        switch (lexer.currentToken()) {
            case TK_GT:
                accept(Token.TK_GT);
                break;

            case TK_GTE:
                accept(Token.TK_GTE);
                break;

            case TK_NE:
                accept(Token.TK_NE);
                break;

            case TK_EQ:
                accept(Token.TK_EQ);
                break;

            case TK_LE:
                accept(Token.TK_LE);
                break;

            case TK_LT:
                accept(Token.TK_LT);
                break;

        }
        System.out.println("Exit parseRelopOperation");
    }

    private void parseOperation(TreeNode<String> parent) {
        System.out.println("Enter parseOperation");
        parent.addChild("parseOperation");
        switch (lexer.currentToken()) {
            case TK_DIV:
                accept(Token.TK_DIV);
                break;

            case TK_MUL:
                accept(Token.TK_MUL);
                break;

            case TK_PLUS:
                accept(Token.TK_PLUS);
                break;

            case TK_MINUS:
                accept(Token.TK_MINUS);
                break;

        }

        System.out.println("Exit parseOperation");
    }

}