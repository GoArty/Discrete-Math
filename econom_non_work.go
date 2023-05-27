package main

import (
    "bufio"
    "fmt"
    "os"
    "strconv"
    "strings"
)

type Tag int

const (
    ERROR Tag = 1 << iota
    NUMBER
    VAR
    PLUS
    MINUS
    MUL
    DIV
    LPAREN
    RPAREN
)

var operators = map[string]Tag{
    "+":     PLUS,
    "-":     MINUS,
    "*":     MUL,
    "/":     DIV,
    "(":     LPAREN,
    ")":     RPAREN,
}

type Lexem struct {
    tag      Tag
    image    string
    variable string
    integer  int
    pos      int
}

func lexer(expr string, lexems chan Lexem) {
    defer close(lexems)
    pos := 0
    for pos < len(expr) {
        switch ch := expr[pos]; {
        case ch == ' ':
            pos++
            continue
        case isAlpha(ch):
            i := pos
            for isAlpha(expr[pos]) || isDigit(expr[pos]) {
                pos++
            }
            varname := expr[i:pos]
            lexems <- Lexem{tag: VAR, image: varname, variable: varname, pos: i}
            continue
        case isDigit(ch):
            i := pos
            for isDigit(expr[pos]) {
                pos++
            }
            n, _ := strconv.Atoi(expr[i:pos])
            lexems <- Lexem{tag: NUMBER, image: strconv.Itoa(n), integer: n, pos: i}
            continue
        default:
            if op, ok := operators[string(ch)]; ok {
                lexems <- Lexem{tag: op, image: string(ch), pos: pos}
                pos++
                continue
            } else {
                lexems <- Lexem{tag: ERROR, image: string(ch), pos: pos}
                return
            }
        }
    }
}

func isAlpha(ch byte) bool {
    return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z'
}

func isDigit(ch byte) bool {
    return '0' <= ch && ch <= '9'
}

type Expr struct {
    value int // значение выражения
    pos   int // номер лексемы, с которой начинается выражение
}

func parseExpr(lexems []Lexem, pos int) (Expr, int) {
    t, pos := parseTerm(lexems, pos)
    for pos < len(lexems) {
        switch lexems[pos].tag {
        case PLUS:
            e, _ := parseTerm(lexems, pos+1)
            e.value = t.value+e.value
            pos = t.pos
            t = e
        case MINUS:
            e, _ := parseTerm(lexems, pos+1)
            e.value = t.value-e.value
            pos = t.pos
            t = e
        default:
            return t, pos
        }
    }
    return t, pos
}

func parseTerm(lexems []Lexem, pos int) (Expr, int) {
    f, pos := parseFactor(lexems, pos)
    for pos < len(lexems) {
        switch lexems[pos].tag {
        case MUL:
            e, _ := parseFactor(lexems, pos+1)
            e.value = (f.value)*(e.value)
            pos = f.pos
            f = e

        case DIV:
            e, _ := parseFactor(lexems, pos+1)
            e.value = f.value/e.value
            pos = f.pos
            f = e
        default:
            return f, pos
        }
    }
    return f, pos
}

func parseFactor(lexems []Lexem, pos int) (Expr, int) {
    switch lexems[pos].tag {
    case NUMBER:
        return Expr{value: lexems[pos].integer, pos: pos}, pos + 1
    case VAR:
        var val int
        fmt.Printf("Enter value for %s: ", lexems[pos].variable)
        fmt.Scanln(&val)
        return Expr{value: val, pos: pos}, pos + 1
    case MINUS:
        f, pos := parseFactor(lexems, pos+1)
        return Expr{value: -f.value, pos: f.pos}, pos
    case LPAREN:
        e, pos := parseExpr(lexems, pos+1)
        if lexems[pos].tag != RPAREN {
            return Expr{}, lexems[pos].pos
        }
        return e, pos + 1
    default:
        return Expr{}, lexems[pos].pos
    }
}

func eval(s string) (int, bool) {
    lexems := make(chan Lexem)
    go lexer(s, lexems)

    parsedExpr, pos := parseExpr(func() []Lexem {
        var lx []Lexem
        for l := range lexems {
            lx = append(lx, l)
        }
        return lx
    }(), 0)

    if pos != len(func() []Lexem {
        var lx []Lexem
        for l := range lexems {
            lx = append(lx, l)
        }
        return lx
    }()) {
        return 0, false
    }

    return parsedExpr.value, true
}

func main() {
    scanner := bufio.NewScanner(os.Stdin)
    for {
        fmt.Print("Enter expression: ")
        if !scanner.Scan() {
            break
        }
        s := strings.TrimSpace(scanner.Text())
        if v, ok := eval(s); ok {
            fmt.Println(v)
        } else {
            fmt.Println("error")
        }
    }
}