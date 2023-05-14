package main
import "fmt"
func add(a, b []int32, p int) []int32 {
    if(a == nil || b == nil || p<1){
        return nil;
    }

    n_a, n_b := len(a), len(b)

    sum := make([]int32, 0)

    var carry int32 = 0
    i, j := 0, 0
    for i < n_a && j < n_b {
        digit := a[i] + b[j] + carry
        sum = append(sum, digit%int32(p))
        carry = digit / int32(p)
        i++
        j++
    }

    for i < n_a {
        digit := a[i] + carry
        sum = append(sum, digit%int32(p))
        carry = digit / int32(p)
        i++
    }

    for j < n_b {
        digit := b[j] + carry
        sum = append(sum, digit%int32(p))
        carry = digit / int32(p)
        j++
    }

    if carry > 0 {
        sum = append(sum, carry)
    }
    return sum
}

func main() {
    a := []int32{1, 2, 3, 4, 2, 4}
    b := []int32{4, 5, 6}
    p := 10
    result := add(a, b, p)
    fmt.Println("p =", p, ": ", result)

    p = 9
    result = add(a, b, p)
    fmt.Println("p =", p, ": ", result)

    p = 3
    result = add(a, b, p)
    fmt.Println("p =", p, ": ", result)

    p = 0
    result = add(a, b, p)
    fmt.Println("p =", p, ": ", result)

    p = 10
    result = add(nil, b, p)
    fmt.Println("p =", p, ": ", result)

    p = 10
    result = add(a, nil, p)
    fmt.Println("p =", p, ": ", result)
}