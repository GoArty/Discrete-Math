package main
import (
   "fmt"
   "unicode"
   "strconv"
   "os"
   "bufio"
)

func reverse(s string) string {
    runes := []rune(s)
    for i, j := 0, len(runes)-1; i < j; i, j = i+1, j-1 {
        runes[i], runes[j] = runes[j], runes[i]
    }
    return string(runes)
}

func polish(str string)(int, bool){
   str = reverse(str)
   rn:=[]rune(str)
   if len(rn) == 0{
      return -1, true
   }

   var numbs [100]int
   k := 0
   open := 0

   for i:=0;i<len(rn);i++{
      fmt.Println(string(rn[i]))
      switch {
      case unicode.IsDigit(rn[i]):
         num, err :=strconv.Atoi(string(rn[i]))
         if err != nil {
            return -3, true
         }
         if !(num>-1 && num<10){
            return -4, true
         }
         numbs[k]=num
         k++
      case rn[i] == '+':
         if k<2 {
            return -2, true
         }
         numbs[k-2] = numbs[k-1]+numbs[k-2]
         k--
      case rn[i] == '-':
         if k<2 {
            return -2, true
         }
         numbs[k-2] = numbs[k-1]-numbs[k-2]
         k--
      case rn[i] == '*':
         if k<2 {
            return -2, true
         }
         numbs[k-2] = numbs[k-1]*numbs[k-2]
         k--
      case rn[i] == ')':
         open++
      case rn[i] == '(':
         open--
      case rn[i] == ' ':
      default:
         return -5, true
      }
   }
   if open!=0{
      return -6, true
   }
   if k==1{
      return numbs[0],false
   }
   return -7, true
}

func main(){
   scanner := bufio.NewScanner(os.Stdin)
   scanner.Scan()
   str := scanner.Text()
   answer, err := polish(str)
   if err{
      fmt.Println(answer,err)
   }else{
      fmt.Println(answer)
   }
}
