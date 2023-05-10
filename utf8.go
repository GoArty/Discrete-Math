package main
import (
    "fmt"
    "unicode/utf8"
)

func encode(UTF32 []rune) []byte {
    var UTF8 []byte
    for _, r := range UTF32 {
        if r <= 0x7F {
            UTF8 = append(UTF8, byte(r))
        } else if r <= 0x7FF {
            UTF8 = append(UTF8, byte(0xC0 | r>>6), byte(0x80 | r & 0x3F))
        } else if r <= 0xFFFF {
            UTF8 = append(UTF8, byte(0xE0 | r>>12), byte(0x80 | r >> 6 & 0x3F), byte(0x80 | r & 0x3F))
        } else {
            UTF8 = append(UTF8, byte(0xF0 | r>>18), byte(0x80 | r >> 12 & 0x3F), byte(0x80 | r >> 6 & 0x3F), byte(0x80|r&0x3F))
        }
    }
    return UTF8
}

func decode(UTF8 []byte) []rune {
    var UTF32 []rune
    for i := 0; i < len(UTF8); {
        r, size := utf8.DecodeRune(UTF8[i:])
        UTF32 = append(UTF32, r)
        i += size
    }
    return UTF32
}

func main() {
    UTF32 := []rune("Привет мир!№%№:);$#%^7")
    fmt.Println("UTF-32:      ", UTF32)
    UTF8 := encode(UTF32)
    fmt.Println("UTF-8:       ", UTF8)
    decodedUTF32 := decode(UTF8)
    fmt.Println("Decoded UTF-8:", decodedUTF32)
    fmt.Println("Is equal:    ", string(UTF32) == string(decodedUTF32))
    fmt.Println("String decoded UTF-8:", string(decodedUTF32))
}
