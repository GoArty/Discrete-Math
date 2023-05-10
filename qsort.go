package main
import "fmt"

func qsort(n int, less func(i, j int) bool, swap func(i, j int)) {
    qsort_sup(0, n-1, less, swap)
}

func qsort_sup(left, right int, less func(i, j int) bool, swap func(i, j int)) {
    if left >= right {
        return
    }
    mid := left
    i := left+1
    j := right
    for i <= j {
        for i <= right && less(i, mid) {
            i++
        }
        for j >= left && less(mid, j) {
            j--
        }
        if i <= j {
            swap(i, j)
            i++
            j--
        }
    }
    swap(mid, j)
    qsort_sup(left, j-1, less, swap)
    qsort_sup(j+1, right, less, swap)
}



func main() {
    array:=[]int{
     -660,  -666,  -691,  -823,  -744,  -779,  -818,  -676,  -789,  -797,
     -714,  -651,  -679,  -739,  -737,  -708,  -698,  -835,  -749,  -825,
     -843,  -731,  -735,  -678,  -697,  -764,  -749,  -844,  -756,  -686,
     -691,  -685,  -833,  -642,  -645,  -660,  -684,  -792,  -724,  -771,
     -732,  -807,  -817,
}
    qsort(len(array),
        func (i, j int) bool {
            return array[i] < array[j]
        },
        func (i, j int) {
            array[i], array[j] = array[j], array[i]
        })


    fmt.Println(array)
    
}
