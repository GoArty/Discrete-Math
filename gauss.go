package main

import (
    "fmt"
    "os"
    "strings"
)

func abs(a int) int{
    if a>=0{
        return a
    }else{
        return -a
    }
}

func gcd(a, b int) int {
    if a < 0 {
        a = -a
    }
    for a != 0 {
        a, b = b%a, a
    }
    return b
}

type Rational struct {
    n, d int
}

func NewRational(val int) Rational {
    return Rational{val, 1}
}

func (r Rational) String() string {

    return fmt.Sprintf("%d/%d", r.n, r.d)
}

func (r Rational) normalize() Rational {
    if r.n != 0 {
        result_gcd := gcd(abs(r.n), abs(r.d))
        r.n /= result_gcd
        r.d /= result_gcd
        if r.d < 0 {
            r.n, r.d = -r.n, -r.d 
        }
    } else {
        r.d = 1
    }
    return r
}

func (r Rational) add(other Rational) Rational {
    return Rational{r.n*other.d + other.n*r.d, r.d*other.d}.normalize()
}

func (r Rational) sub(other Rational) Rational {
    return Rational{r.n*other.d - other.n*r.d, r.d*other.d}.normalize()
}

func (r Rational) mul(other Rational) Rational {
    return Rational{r.n * other.n, r.d * other.d}.normalize()
}

func (r Rational) div(other Rational) Rational {
    return Rational{r.n * other.d, r.d * other.n}.normalize()
}

type Matrix [][]Rational

func NewMatrix(rows, cols int) Matrix {
    m := make(Matrix, rows)
    for i := range m {
        m[i] = make([]Rational, cols)
    }
    return m
}

func (m Matrix) String() string {
    var sb strings.Builder
    for i, row := range m {
        for j, x := range row {
            if j == 0 {
                sb.WriteString("| ")
            }
            sb.WriteString(x.normalize().String())
            if j < len(row)-1 {
                sb.WriteString("\t")
            }
        }
        sb.WriteString(" |\n")
        if i < len(m)-1 {
            sb.WriteString("\n")
        }
    }
    return sb.String()
}

func (m Matrix) SwapRows(i, j int) {
    m[i], m[j] = m[j], m[i]
}

func (m Matrix) ScaleRow(i int, scalar Rational) {
    for j := range m[i] {
        m[i][j] = m[i][j].mul(scalar)
    }
}

func (m Matrix) AddMultipleOfRow(dest, src int, scalar Rational) {
    for j := range m[dest] {
        m[dest][j] = m[dest][j].add(m[src][j].mul(scalar))
    }
}

func (m Matrix) Solve() ([]Rational, bool) {

    n := len(m)
    for i := 0; i < n; i++ {
        max := i
        for j := i + 1; j < n; j++ {
            if m[j][i].n != 0 {
                max = j
                break
            }
        }
        if max != i {
            m.SwapRows(max, i)
        }
        if m[i][i].n == 0 {
            return nil, false
        }
        m.ScaleRow(i, NewRational(1).div(m[i][i]))
        for j := i + 1; j < n; j++ {
            m.AddMultipleOfRow(j, i, m[j][i].mul(NewRational(-1)))
        }
    }
    for i := n - 1; i >= 0; i-- {
        if m[i][i].n == 0 {
            return nil, false
        }
        for j := i - 1; j >= 0; j-- {
            m.AddMultipleOfRow(j, i, m[j][i].mul(NewRational(-1)))
        }
    }
    solutions := make([]Rational, n)
    for i := range solutions {
        solutions[i] = m[i][n]
    }
    return solutions, true
}

func main() {
    var n int
    fmt.Fscan(os.Stdin, &n) 
    m := NewMatrix(n, n+1)
    for i := 0; i < n; i++ {
        for j := 0; j< n +1; j++{
            var sup int
            fmt.Fscan(os.Stdin, &sup) 
            m[i][j] = Rational{sup, 1}
        }
    }
    if solutions, ok := m.Solve(); ok {
        for _, sol := range solutions {
            fmt.Println(sol.normalize())
        }
    } else {
        fmt.Println("No solution")
    }
}